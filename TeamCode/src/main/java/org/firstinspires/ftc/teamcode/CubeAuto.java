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
    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    FrameGrabber vision;
    Point lastObjectPosition = new Point(-1, -1);
    double lastDetectionTime = 0.0;//The last time that the camera detected an object in the frame
    Timer timer;

    double allowedError = 5.0;
    double pFactor = 0.008;

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
        //hardware.compass.resetHeading();

        if (vision != null) {
            vision.setDetectionMode(DetectionMode.GOLD_CUBE);//Tell the camera to look for gold cube
            vision.setScreenOrientation(FrameGrabber.ScreenOrientation.PORTRAIT);//Tell the camera we want landscape mode

        }

        //telemetry.addData("Raw Compass", hardware.compass.getRawHeading());
        //telemetry.addData("heading", hardware.compass.getHeading());
        //telemetry.addData("OriginAngle", hardware.compass.getOriginalRawHeading());

        telemetry.update();
    }

    boolean aligned = false;
    double pixelPerInch = 13.0/30.0;//13 pixels divided by 30 inches
    double distanceToCube = -1.0;

    double leftOriginDist = 0.0;
    double rightOriginDist = 0.0;

    @Override
    public void loop() {
        screenDimensions = vision.getScreenDimensions();
        //telemetry.addData("Cube Height", vision.lastHeight);
        double currentTimeSeconds = System.currentTimeMillis()/1000;//Current time, in seconds, since unix epoch
        //Make sure vision has been initialized
        if (vision != null) {
            boolean usedLastPosition = false;

            //Get information that the camera found
            ObjectDetectionResult processingResult = vision.getLastProcessingResult();//Get the information about the last analyzed frame
            Point cubePosition = processingResult.getObjectPosition();

            //If the camera could not find the cube in this frame then check how long ago we had a position
            if (cubePosition.x == -1 && currentTimeSeconds - lastDetectionTime <= 0.3) {
                cubePosition= lastObjectPosition;
                usedLastPosition = true;
            }

            //Dont update if we didnt find the cube in this frame
            if (!usedLastPosition) {
                lastObjectPosition = cubePosition;
                lastDetectionTime = currentTimeSeconds;
            }
            double newSpeed = 0.0;
            double targetPosition = screenDimensions.x/2;

            //Make sure the cube is in view of the camera
            if (cubePosition.x > -1) {
                double currentError = targetPosition - cubePosition.x;//How far away the robot is from the target

                //If the robot is not lined up
                if (Math.abs(currentError) > allowedError) {
                    newSpeed = currentError * pFactor;
                } else {
                    aligned = true;
                }
            }

            //Make the robot turn in place to align itself with the target
            if (!aligned) {
                driveTrain.left.setPower(newSpeed);
                driveTrain.right.setPower(-newSpeed);
                distanceToCube = processingResult.getObjectSize().height/pixelPerInch + 3.0;//Estimates roughly how far away the cube is
                rightOriginDist = driveTrain.getRightInchesDriven();
                leftOriginDist = driveTrain.getLeftInchesDriven();
            }

            //If the robot is aligned with the cube prepare to move forward
            if (aligned) {
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

            }



            telemetry.addData("Cube Aligned: ", aligned);
            telemetry.addData("Turn speed", newSpeed);
            telemetry.addData("Cube Position:", String.valueOf(cubePosition.x) + ", " + String.valueOf(cubePosition.y));
            telemetry.update();
        }
    }
}