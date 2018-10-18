package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Autonomus", group = "Auto")
public class TelemeterOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    Collector collector;
    Hanger extender;
    ToggleButton a = new ToggleButton(),
                 x = new ToggleButton();



    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor);
        collector = new Collector(hardware.collectorMotor, hardware.armMotor);
        extender = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop);
    }

    @Override
    public void start(){
        extender.release();
    }

    @Override
    public void loop() {
        driveTrain.driveWithJoyStick(gamepad1.left_stick_x, gamepad1.left_stick_y);
        if (a.wasJustClicked(gamepad1.a)) extender.extendHook();
        if (gamepad1.x) collector.runCollector();
        if (gamepad1.dpad_down) collector.stopCollector();
    }
}
