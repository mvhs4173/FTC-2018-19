package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Loop", group = "Auto")
public class TelemeterOpMode extends OpMode {

    //Objects
    Hardware hardware = new Hardware();
    private DriveTrain driveTrain;
    private Collector collector;
    private Hanger extender;
    private ToggleButton up = new ToggleButton(), gather = new ToggleButton(), dispense = new ToggleButton();

    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor);
        collector = new Collector(hardware.collectorMotor, hardware.armMotor);
        extender = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
    }

    @Override
    public void start(){
        extender.drop();
    }

    @Override
    public void loop() {
        driveTrain.driveWithJoyStick(gamepad1.left_stick_x, gamepad1.left_stick_y);
        if (up.wasJustClicked(gamepad1.dpad_up)) extender.hang();
        if (gamepad1.x) collector.runCollector();
        if (gamepad1.b) collector.stopCollector();
        if (gather.wasJustClicked(gamepad1.a)) collector.runArmToPosition(Collector.Positions.GATHER);
        if (dispense.wasJustClicked(gamepad1.y)) collector.runArmToPosition(Collector.Positions.DISPENSE);
        telemetry.addData("Motor Position", collector.getCurrentPosition());
        telemetry.addData("stop state", extender.getState()[0]);
        telemetry.addData("lower state", extender.getState()[1]);
        telemetry.update();
    }
}