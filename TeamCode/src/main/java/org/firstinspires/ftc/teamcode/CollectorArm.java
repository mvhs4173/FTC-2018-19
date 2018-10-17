package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CollectorArm {
    private DcMotor elbowMotor;
    private int targetPosition,
                    currentPosition,
                    error;

    public CollectorArm(DcMotor collectorMotor,
                        DcMotor collectorMotor2) {
        this.elbowMotor = collectorMotor;
        elbowMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void ArmMotor(double inputPower) {
        elbowMotor.setPower(inputPower);
    }

    public void stopMotor() {
        elbowMotor.setPower(0);
    }

    public void setElbowTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
        elbowMotor.setTargetPosition(this.targetPosition);
    }

    public void runMotor() {
        currentPosition = elbowMotor.getCurrentPosition();
        error = targetPosition - currentPosition;
        elbowMotor.setPower(0.5*error);
    }

}