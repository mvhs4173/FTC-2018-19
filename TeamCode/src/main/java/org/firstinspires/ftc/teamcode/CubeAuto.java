package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.core.Point;

import ftc.vision.FrameGrabber;
import ftc.vision.FrameGrabber.DetectionMode;
import ftc.vision.ObjectDetectionResult;

@Autonomous(name = "CubeAuto", group = "Auto")
public class CubeAuto extends OpMode {

    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    FrameGrabber vision;//The thing that gets the next frame from the camera
    Point lastObjectPosition = new Point(-1, -1);
    double lastDetectionTime = 0.0;//The last time that the camera detected an object in the frame
    Timer timer;

    //PID//
    double lastLoopTime = 0.0;//The time of last loop()
    double allowedError = 5.0;
    double pFactor = 0.003;
    double dFactor = 0.5;
    double lastError = 0.0;
    ////////

    Point screenDimensions;


    //Vision vision;
    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
        vision = FtcRobotControllerActivity.frameGrabber;
        timer = new Timer();

    }

    @Override
    public void init_loop() {
        //Wait until vision is initialized
        if (vision != null) {
            vision.setDetectionMode(DetectionMode.GOLD_CUBE);//Tell the camera to look for gold cube
            vision.setScreenOrientation(FrameGrabber.ScreenOrientation.PORTRAIT);//Tell the camera we want portrait mode

        }

    }

    boolean aligned = false;//Made true after the robot has aligned itself with golden cube
    double pixelPerInch = 13.0/30.0;//13 pixels divided by 30 inches
    double distanceToCube = -1.0;//-1 meaning we have not calculated a distance

    double leftOriginDist = 0.0;//How far the left motor has traveled used as a relative point so that we can 'reset' encoders
    double rightOriginDist = 0.0;


    @Override
    public void loop() {
        double currentLoopTime = System.currentTimeMillis()/1000;//The current time of the loop in seconds since unix epoch
        double currentError = 0.0;


        screenDimensions = vision.getScreenDimensions();
        double currentTimeSeconds = System.currentTimeMillis()/1000;//Current time, in seconds, since unix epoch
        //Make sure vision has been initialized
        if (vision != null) {
            boolean usedLastPosition = false;//Say that we have not used the position found in last frame

            //Get information that the camera found
            ObjectDetectionResult processingResult = vision.getLastProcessingResult();//Get the information about the last analyzed frame
            Point cubePosition = processingResult.getObjectPosition();

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
            double newSpeed = 0.0;
            double targetPosition = screenDimensions.x/2;

            //Make sure the cube is in view of the camera
            if (cubePosition.x > -1 && !aligned) {
                currentError = targetPosition - cubePosition.x;//How far away the robot is from the target

                //Calculate the derivative part of PD loop
                double derivativeNumber = ((currentError - lastError)/(currentLoopTime - lastLoopTime)) * dFactor;
                //Calculate the proportional part of PD loop
                double proportionalNumber = currentError * pFactor;

                newSpeed = derivativeNumber + proportionalNumber;//Calculate the new speed for the motors

                //If the robot is not lined up
                if (Math.abs(currentError) > allowedError) {
                        driveTrain.left.setPower(newSpeed);
                        driveTrain.right.setPower(-newSpeed);
                } else {
                    //If the robot has been aligned for the past 20 iterations then say its finished
                    //This is to prevent overshoot when we cut power to the motor
                    if (Math.abs(newSpeed) <= 0.02) {
                        aligned = true;//Say the robot is aligned and stop the motors
                        driveTrain.left.setPower(0.0);
                        driveTrain.right.setPower(0.0);
                    }
                }
            }

            //If the robot has not been aligned with the cube yet
            if (!aligned) {
                //Turn in place
                driveTrain.left.setPower(newSpeed);
                driveTrain.right.setPower(-newSpeed);
                distanceToCube = processingResult.getObjectSize().height/pixelPerInch + 3.0;//Estimates roughly how far away the cube is
                rightOriginDist = driveTrain.getRightInchesDriven();
                leftOriginDist = driveTrain.getLeftInchesDriven();
            }

            //If the robot is aligned with the cube prepare to move forward
            /*if (aligned) {
                double inchesDriven = ((driveTrain.getLeftInchesDriven() - leftOriginDist) + (driveTrain.getRightInchesDriven() - rightOriginDist))/2;

                if (inchesDriven < distanceToCube) {
                    driveTrain.right.setPower(0.5);
                    driveTrain.left.setPower(0.5);
                }else {
                    driveTrain.right.setPower(0);
                    driveTrain.left.setPower(0);
                }
                telemetry.addData("Inches to cube", distanceToCube);
                telemetry.addData("Inches driven", inchesDriven);

            }*/

            lastLoopTime = System.currentTimeMillis()/1000;
            lastError = currentError;
            telemetry.addData("Right Speed", driveTrain.right.getPower());
            telemetry.addData("Left Speed", driveTrain.left.getPower());
            telemetry.addData("Cube Aligned: ", aligned);
            telemetry.addData("Turn speed", newSpeed);
            telemetry.addData("Cube Position:", String.valueOf(cubePosition.x) + ", " + String.valueOf(cubePosition.y));
            telemetry.update();
        }
    }
}