package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Loop", group = "Auto")
public class TelemeterOpMode extends OpMode {

    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    Hanger hanger;
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
            driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.imu);
            hanger = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
            markerArm = new MarkerArm(hardware.markerServo);
            markerArm.setPosition(0);
            markerArm.returnToStow();
            hanger.task = Hanger.Task.Float;
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
        if (up.wasJustClicked(gamepad1.dpad_up)) {
            hanger.init(Hanger.Task.HANG);
        } else if (down.wasJustClicked(gamepad1.dpad_down)) {
            hanger.init(Hanger.Task.DROP);
        }
        if (gamepad1.y) hanger.init(Hanger.Task.RESET);
        hanger.execute(gamepad1.a);
        if (gamepad1.b) hanger.release();
        if (gamepad1.x) hanger.grip();
        telemetry.addData("stop state", hanger.getState()[0]);
        telemetry.addData("lower state", hanger.getState()[1]);
        telemetry.addData("Claw Pos", hanger.getPosition());
        telemetry.addData("Motor pos", hanger.getEncoder());
        telemetry.addData("mode", hanger.getMode());
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