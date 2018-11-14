package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous(name = "VuforiaTest", group = "Auto")
public class VuforiaTest extends OpMode {
    //Objects

    //Vision vision;
    private String VUFORIA_KEY  = "AcalE4b/////AAAAGWCjBuQmt0HzpsK0qwOJVdAsGbKL06QQfIIWj3G6VwzPpNpJyqme11mznF/h1afzpP5XOk0r+EO/zaodgxeONnViGyICjH2LLMDHELV5HCiF4gacqS5GzSIIhkohOYX7YaVsh++cz1CYcTz+0CmEFdak4lwfvXD/zfWtWg1qO1+5aQ3p4FlULjVoWA49P3sQrGKJZ9l8qPPUs+kfVHebIy33XCsiqcqP/HvqbSXZaSNMr5lFkVAXSIh0SHDCfWwJu5ek+2gPf0MwyZXawYrKpX4eL8nsDZNmiK09qvkNaYVECGUMQobeAyl4ufP/HJl11UivYgGeuPVwJb5t6YUpFZHeIfyrC8/doYNXdzNLJ6Eq";
    private VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private VuforiaLocalizer vuforia = null;
    private VuforiaTrackables pictures = null;

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

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(pictures);


    }

    @Override
    public void init_loop(){

    }

    double desiredAngle = 0;
    @Override
    public void loop() {

    }
}