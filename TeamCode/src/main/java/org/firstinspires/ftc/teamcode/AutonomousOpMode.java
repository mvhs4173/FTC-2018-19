package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Auto", group = "Auto")
public class AutonomousOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    DriveTrain DriveTrain;
    Collector Collector;
    Hanger Hanger;

    Vision vision;

    public void init() {
        //hardware.init(hardwareMap);
    }

    public void loop() {
        if (vision == null) {
            vision = new Vision();
        }
    }
}