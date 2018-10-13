package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CollectorArm {
    private DcMotor elbowMotor;


    public CollectorArm(DcMotor collectorMotor,
                     DcMotor collectorMotor2) {
        this.elbowMotor = collectorMotor;

    }

    public void ArmMotor(double inputPower) {
        elbowMotor.setPower(inputPower);
    }

    public void stopMotor() {
        elbowMotor.setPower(0);
    }
    public void setElbowMotor() {
        elbowMotor.setTargetPosition();
    }

}