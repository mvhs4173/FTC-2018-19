package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Autonomus", group = "Auto")
public class TelemeterOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    CollectorArm collectorArm;
    Hanger extender;



    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor);
        collectorArm = new CollectorArm(hardware.collectorMotor, hardware.armMotor);
        extender = new Hanger(hardware.hookServo, hardware.extensionMotor);
    }

    @Override
    public void loop() {

    }
}
