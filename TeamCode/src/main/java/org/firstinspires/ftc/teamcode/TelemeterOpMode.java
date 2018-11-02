package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Loop", group = "Auto")
public class TelemeterOpMode extends OpMode {

    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    //Collector collector;
    Hanger extender;
    MarkerArm markerArm;
    ToggleButton up = new ToggleButton(), down = new ToggleButton(), dispense = new ToggleButton();

    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
        //collector = new Collector(hardware.collectorMotor, hardware.armMotor);
        extender = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
        markerArm = new MarkerArm(hardware.markerServo);
        markerArm.returnToOrigin();
    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {
        driveTrain.driveWithJoyStick(gamepad1.left_stick_x, gamepad1.left_stick_y);
        if (gamepad1.dpad_up && extender.task == Hanger.Task.Float) {
            extender.init(Hanger.Task.HANG);
        } else if (gamepad1.dpad_down && extender.task == Hanger.Task.Float){
            extender.init(Hanger.Task.DROP);
        }
        extender.execute();
        if (gamepad1.dpad_left) extender.release();
        if (gamepad1.dpad_right) extender.grip();
        if (gamepad1.a) markerArm.release();
        //if (gamepad1.x) collector.runCollector();
        //if (gamepad1.b) collector.stopCollector();
        //if (gather.wasJustClicked(gamepad1.order)) collector.runArmToPosition(Collector.Positions.GATHER);
        //if (dispense.wasJustClicked(gamepad1.y)) collector.runArmToPosition(Collector.Positions.DISPENSE);
        //telemetry.addData("Motor Position", collector.getCurrentPosition());
        telemetry.addData("stop state", extender.getState()[0]);
        telemetry.addData("lower state", extender.getState()[1]);
        telemetry.addData("POS", extender.getPosition());
        telemetry.addData("pos", extender.getEncoder());
        telemetry.addData("mode", extender.getMode());
        telemetry.update();
    }
}