package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Loop", group = "Auto")
public class TelemeterOpMode extends OpMode {

    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    Hanger extender;
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
            extender = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
            markerArm = new MarkerArm(hardware.markerServo);
            markerArm.setPosition(0);
            markerArm.returnToStow();
            extender.task = Hanger.Task.Float;
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
        if (up.wasJustClicked(gamepad1.dpad_up)&&!(gamepad1.left_trigger == 1)) {
            extender.init(Hanger.Task.HANG);
        } else if (down.wasJustClicked(gamepad1.dpad_down)&&!(gamepad1.left_trigger == 1)) {
            extender.init(Hanger.Task.DROP);
        }
        if (up.wasJustClicked(gamepad1.dpad_up)&&(gamepad1.left_trigger == 1)) extender.extendHook();
        if (down.wasJustClicked(gamepad1.dpad_down)&&(gamepad1.left_trigger == 1)) extender.retractHook();
        if (gamepad1.y) extender.init(Hanger.Task.RESET);
        extender.execute(gamepad1.a);
        if (gamepad1.b) extender.release();
        if (gamepad1.x) extender.grip();
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