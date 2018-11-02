package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Loop", group = "Auto")
public class TelemeterOpMode extends OpMode {

    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    //Collector collector;
    Hanger1 extender;
    MarkerArm markerArm;
    ToggleButton up = new ToggleButton(), down = new ToggleButton(), dispense = new ToggleButton();

    public String FormatStackTrace(Exception e) {
        String msg = "Exception: " + e.toString();
        for(StackTraceElement ste : e.getStackTrace()) {
            msg = msg + " -> " + ste.toString();
        }
        return msg;
    }

    @Override
    public void init() {
        try {
            hardware.init(hardwareMap);
            driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
            //collector = new Collector(hardware.collectorMotor, hardware.armMotor);
            extender = new Hanger1(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
            markerArm = new MarkerArm(hardware.markerServo);
            markerArm.setPosition(0);
            extender.task = Hanger1.Task.Float;
        }catch(Exception e) {
            telemetry.addData("Say", FormatStackTrace(e));
            telemetry.update();
            throw e;
        }
    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {
        try {
        driveTrain.driveWithJoyStick(gamepad1.left_stick_x, gamepad1.left_stick_y);
        if (gamepad1.dpad_up) {
            if (up.wasJustClicked(gamepad1.dpad_up)) extender.hasBeenPressed = false;
            extender.init(Hanger1.Task.HANG);
        } else if (gamepad1.dpad_down){
            if (down.wasJustClicked(gamepad1.dpad_down)) extender.hasBeenPressed = false;
            extender.init(Hanger1.Task.DROP);
        }
        extender.execute();
        if (gamepad1.y) extender.extendHook();
        if (gamepad1.a) extender.retractHook();
        extender.moveServo(gamepad1.dpad_left, gamepad1.dpad_right);
        //if (gamepad1.dpad_left) extender.release();
        //if (gamepad1.dpad_right) extender.grip();
        if (gamepad1.x) markerArm.release();
        if (gamepad1.b) markerArm.returnToStow();
        //if (gamepad1.x) collector.runCollector();
        //if (gamepad1.b) collector.stopCollector();
        //if (gather.wasJustClicked(gamepad1.a)) collector.runArmToPosition(Collector.Positions.GATHER);
        //if (dispense.wasJustClicked(gamepad1.y)) collector.runArmToPosition(Collector.Positions.DISPENSE);
        //telemetry.addData("Motor Position", collector.getCurrentPosition());
        telemetry.addData("stop state", extender.getState()[0]);
        telemetry.addData("lower state", extender.getState()[1]);
        telemetry.addData("POS", extender.getPosition());
        telemetry.addData("pos", extender.getEncoder());
        telemetry.addData("mode", extender.getMode());
        telemetry.update();
        }catch(Exception e) {
            telemetry.addData("Say", FormatStackTrace(e));
            telemetry.update();
            Log.getStackTraceString(e);
            Log.e("null Pointer", FormatStackTrace(e), e);
            throw e;
        }
    }
}