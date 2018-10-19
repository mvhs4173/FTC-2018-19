package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Auto", group = "Auto")
public class AutonomusOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    Collector collector;
    Hanger hanger;

    Vision vision;
    @Override
    public void init() {

        //hardware.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (vision == null) {
            vision = new Vision();
        }
    }
}
