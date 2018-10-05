package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Collector {
    private DcMotor collectorMotor;
    private DcMotor collectorMotor2;

    public Collector(DcMotor collectorMotor,
                     DcMotor collectorMotor2) {
        this.collectorMotor = collectorMotor;
        this.collectorMotor2 = collectorMotor2;
    }

    public void Intake(double inputPower) {
        collectorMotor.setPower(inputPower);
    }

    public void Arm(double inputPower) {
        collectorMotor2.setPower(inputPower);
    }

    public void stopMotor() {
        collectorMotor.setPower(0);
        collectorMotor2.setPower(0);
    }
}