package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This is our opmode use for testing new code
 */
@TeleOp(name = "Testing_TeleOp", group = "Auto")
public class Testing_TeleOp extends OpMode {

    //Objects, add the necessary classes needed to test
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    Hanger extender;
    MarkerArm markerArm;

    /**
     * This code is used to catch errors and send them back to help troubleshoot
     * @param e the error received
     * @return What the error says after being formatted
     */
    public String FormatStackTrace(Exception e) {
        String msg = "Exception: " + e.toString();
        for(StackTraceElement ste : e.getStackTrace()) {
            msg = msg + " -> " + ste.toString();
        }
        return msg;
    }

    /**
     * Called once
     * We set up our classes with the hardware.
     */
    @Override
    public void init() {
        try {
            hardware.init(hardwareMap);
            driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.imu);
            extender = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
            markerArm = new MarkerArm(hardware.markerServo);
            markerArm.setPosition(0);
            extender.task = Hanger.Task.Float;
        }catch(Exception e) {
            telemetry.addData("Error", FormatStackTrace(e));
            telemetry.update();
            Log.getStackTraceString(e);
            Log.e("Caught Error", FormatStackTrace(e), e);
            throw e;
        }
    }

    @Override
    public void start(){

    }

    /**
     * Add the code needed to test in this method
     */
    @Override
    public void loop() {
        try {

            // What to send back to the phone, add what you need
            telemetry.addData("stop state", extender.getState()[0]);
            telemetry.addData("lower state", extender.getState()[1]);
            telemetry.addData("POS", extender.getPosition());
            telemetry.addData("pos", extender.getEncoder());
            telemetry.addData("mode", extender.getMode());
            telemetry.update();
        }catch(Exception e) {
            telemetry.addData("Error", FormatStackTrace(e));
            telemetry.update();
            Log.getStackTraceString(e);
            Log.e("Caught Error", FormatStackTrace(e), e);
            throw e;
        }
    }
}