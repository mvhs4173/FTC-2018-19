package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Autonomus", group = "Auto")
public class AutonomusOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    CollectorArm collectorArm;
    Hanger hanger;


    @Override
    public void init() {
        hardware.init(hardwareMap);
    }

    @Override
    public void loop() {

    }
}