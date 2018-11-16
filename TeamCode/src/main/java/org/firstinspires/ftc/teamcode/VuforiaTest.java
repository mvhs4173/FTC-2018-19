package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.vuforia.Trackable;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import java.util.ArrayList;
import java.util.List;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous(name = "VuforiaTest", group = "Auto")
public class VuforiaTest extends OpMode {
    //Objects

    //Vision vision;
    private String VUFORIA_KEY  = "AcalE4b/////AAAAGWCjBuQmt0HzpsK0qwOJVdAsGbKL06QQfIIWj3G6VwzPpNpJyqme11mznF/h1afzpP5XOk0r+EO/zaodgxeONnViGyICjH2LLMDHELV5HCiF4gacqS5GzSIIhkohOYX7YaVsh++cz1CYcTz+0CmEFdak4lwfvXD/zfWtWg1qO1+5aQ3p4FlULjVoWA49P3sQrGKJZ9l8qPPUs+kfVHebIy33XCsiqcqP/HvqbSXZaSNMr5lFkVAXSIh0SHDCfWwJu5ek+2gPf0MwyZXawYrKpX4eL8nsDZNmiK09qvkNaYVECGUMQobeAyl4ufP/HJl11UivYgGeuPVwJb5t6YUpFZHeIfyrC8/doYNXdzNLJ6Eq";
    private VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private VuforiaLocalizer vuforia = null;
    private VuforiaTrackables pictures = null;

    private OpenGLMatrix lastLocation = null;
    private boolean TargetVisible = false;

    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

    List<VuforiaTrackable> allTrackables;

    @Override
    public void init() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        pictures = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = pictures.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = pictures.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = pictures.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = pictures.get(3);
        backSpace.setName("Back-Space");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(pictures);

        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));

        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90));

        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90));

        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));

        final int Camera_Forward_Displacemen = 0; //How far forward the camera is from the center of the robot in mm.
        final int Camera_Vertical_Displacement = 0; //How far the camera is from the ground in mm.
        final int Camera_Left_Displacement = 0; //How far left the camera is from the center of the robot in mm.

        OpenGLMatrix phonLocationOnRobot = OpenGLMatrix
                .translation(Camera_Forward_Displacemen, Camera_Vertical_Displacement, Camera_Left_Displacement)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));
        for (VuforiaTrackable trackable: allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phonLocationOnRobot, parameters.cameraDirection);
        }
        telemetry.addData(">", "Press Play To Start Tracking");
        telemetry.update();


    }

    @Override
    public void init_loop(){

    }

    double desiredAngle = 0;
    @Override
    public void loop() {
        TargetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
             if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                 telemetry.addData("VisableTarget", trackable.getName());
                 TargetVisible = true;

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
             }
        }
        if (TargetVisible) {
            VectorF translation =  lastLocation.getTranslation();
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
            translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2), mmPerInch);

            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
        }
        else {
                    telemetry.addData("Visable Target", "none");
        }
            telemetry.update();
        }
    }