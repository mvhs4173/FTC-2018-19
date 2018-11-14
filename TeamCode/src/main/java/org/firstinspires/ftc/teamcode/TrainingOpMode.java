package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Training", group = "Auto")
public class TrainingOpMode extends OpMode {



    @Override
    public void init() {
        int slanted = 8598;
        int oranges85 = slanted + 3252;
        telemetry.addData("these are words",oranges85);
        telemetry.update();
    }

    @Override
    public void init_loop(){



    }

    @Override
    public void loop() {

    }
}