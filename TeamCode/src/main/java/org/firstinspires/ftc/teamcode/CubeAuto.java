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


    //Vision vision;
    @Override
    public void init() {
        //hardware.init(hardwareMap);
        //driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
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

    @Override
    public void loop() {
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

            telemetry.addData("Cube Position:", String.valueOf(cubePosition.x) + ", " + String.valueOf(cubePosition.y));
            telemetry.update();
        }
    }
}