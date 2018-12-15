package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;

import ftc.vision.FrameGrabber;
import ftc.vision.FrameGrabber.DetectionMode;
import ftc.vision.ObjectDetectionResult;

@Autonomous(name = "CubeAuto", group = "Auto")
public class CubeAuto extends OpMode {

    public static Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    FrameGrabber vision;//The thing that gets the next frame from the camera
    Point lastObjectPosition = new Point(-1, -1);
    double lastDetectionTime = 0.0;//The last time that the camera detected an object in the frame
    Timer timer;

    //PID//
    double lastLoopTime = 0.0;//The time of last loop()
    double allowedError = 5.0;
    double pFactor = 0.0015;
    double dFactor = 0.0;
    double lastError = 0.0;
    ////////

    Point screenDimensions;
    ToggleButton aButton = new ToggleButton(),
    yButton = new ToggleButton(),
    xButton = new ToggleButton(),
    bButton = new ToggleButton();
    public static Telemetry t;

    //Vision vision;
    @Override
    public void init() {
        t = telemetry;
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.imu);
        vision = FtcRobotControllerActivity.frameGrabber;

        timer = new Timer();
    }

    @Override
    public void init_loop() {
        //Wait until vision is initialized
        if (vision != null) {
            vision.setDetectionMode(DetectionMode.GOLD_CUBE);//Tell the camera to look for gold cube
            vision.setScreenOrientation(FrameGrabber.ScreenOrientation.LANDSCAPE);//Tell the camera we want portrait mode
        }

    }

    boolean aligned = false;//Made true after the robot has aligned itself with golden cube
    double pixelPerInch = 13.0/30.0;//13 pixels divided by 30 inches
    double distanceToCube = -1.0;//-1 meaning we have not calculated a distance


    @Override
    public void loop() {
        double currentLoopTime = System.currentTimeMillis() / 1000;//The current time of the loop in seconds since unix epoch
        double currentError = 0.0;
        double newSpeed = 0.0;

        boolean xPressed = xButton.wasJustClicked(gamepad1.x);
        boolean bPressed = bButton.wasJustClicked(gamepad1.b);
        boolean aPressed = aButton.wasJustClicked(gamepad1.a);
        boolean yPressed = yButton.wasJustClicked(gamepad1.y);

        Point cubePosition = new Point(0, 0);

        //Increase or decrease pFactor by one hundredth
        if (xPressed) {
            pFactor -= 0.01;
        } else if (bPressed) {
            pFactor += 0.01;
        }

        //Increase or decrease pFactor by one thousandth
        if (aPressed) {
            pFactor -= 0.001;
        } else if (yPressed) {
            pFactor += 0.001;
        }


        screenDimensions = vision.getScreenDimensions();
        double currentTimeSeconds = System.currentTimeMillis() / 1000;//Current time, in seconds, since unix epoch
        //Make sure vision has been initialized
        if (vision != null) {
            boolean usedLastPosition = false;//Say that we have not used the position found in last frame

            //Get information that the camera found
            ObjectDetectionResult processingResult = vision.getLastProcessingResult();//Get the information about the last analyzed frame
            cubePosition = processingResult.getObjectPosition();

            //If the camera could not find the cube in this frame then check how long ago we had a position
            if (cubePosition.x == -1 && currentTimeSeconds - lastDetectionTime <= 0.3) {
                cubePosition= lastObjectPosition;
                usedLastPosition = true;//Say we are using the position found in the last frame
            }

            //Dont update if we didnt find the cube in this frame
            if (!usedLastPosition) {
                lastObjectPosition = cubePosition;
                lastDetectionTime = currentTimeSeconds;
            }

            double targetPosition = screenDimensions.x/2;

            //Make sure the cube is in view of the camera
            if (cubePosition.x > -1 && !aligned) {
                currentError = targetPosition - cubePosition.x;//How far away the robot is from the target

                //Calculate the derivative part of PD loop
                double derivativeNumber = ((currentError - lastError)/(currentLoopTime - lastLoopTime)) * dFactor;
                //Calculate the proportional part of PD loop
                double proportionalNumber = currentError * pFactor;

                newSpeed = proportionalNumber;//Calculate the new speed for the motors



                //If the robot is not lined up
                if (Math.abs(currentError) > allowedError) {
                        driveTrain.left.setPower(proportionalNumber);
                        driveTrain.right.setPower(-proportionalNumber);
                } else {
                    //If the robot has been aligned for the past 20 iterations then say its finished
                    //This is to prevent overshoot when we cut power to the motor
                    if (Math.abs(newSpeed) <= 0.01) {
                        aligned = true;//Say the robot is aligned and stop the motors

                        distanceToCube = processingResult.getObjectSize().height/pixelPerInch + 3.0;

                        driveTrain.left.setPower(0.0);
                        driveTrain.right.setPower(0.0);

                        driveTrain.resetLeftEncoder();
                        driveTrain.resetRightEncoder();
                    }
                };
            }

            if (aligned) {
                double inchesDriven = ((driveTrain.getLeftInchesDriven()) + (driveTrain.getRightInchesDriven()))/2;

                if (Math.abs(inchesDriven) < 40.0) {
                    driveTrain.right.setPower(-1);
                    driveTrain.left.setPower(-1);
                }else {
                    driveTrain.right.setPower(0);
                    driveTrain.left.setPower(0);
                }
                telemetry.addData("Inches to cube", distanceToCube);
                telemetry.addData("Inches driven", inchesDriven);
            }

            lastLoopTime = System.currentTimeMillis() / 1000;
            lastError = currentError;


        }
//      telemetry.addData("Current Error:", currentError);
      //telemetry.addData("Cube Aligned: ", aligned);
  //    telemetry.addData("Turn speed", newSpeed);
    //  telemetry.addData("Cube Position:", String.valueOf(cubePosition.x) + ", " + String.valueOf(cubePosition.y));
        telemetry.update();

    }
}