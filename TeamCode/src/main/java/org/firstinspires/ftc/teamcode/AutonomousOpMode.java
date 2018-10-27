package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@SuppressWarnings("unused")
@Autonomous(name = "Auto", group = "Auto")
public class AutonomousOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    private DriveTrain DriveTrain;
    private Collector Collector;
    private Hanger Hanger;

    private Vision vision;

    public void init() {
        //hardware.init(hardwareMap);
    }

    public void loop() {
        if (vision == null) {
            vision = new Vision();
        }
    }
}