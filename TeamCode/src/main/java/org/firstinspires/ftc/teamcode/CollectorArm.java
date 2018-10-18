package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CollectorArm {
    private DcMotor armMotor;
    private int targetPosition,
                    currentPosition,
                    error;

    /**
     *
     * @param collectorMotor motor to run the collector
     * @param armMotor motor to move the arm
     */
    public CollectorArm(DcMotor collectorMotor,
                        DcMotor armMotor) {
        this.armMotor = collectorMotor;
        this.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void moveArmMotorAtPower(double inputPower) {
        armMotor.setPower(inputPower);
    }

    public void stopMotor() {
        armMotor.setPower(0);
    }

    public void setElbowTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
        armMotor.setTargetPosition(this.targetPosition);
    }

    public void runMotor() {
        currentPosition = armMotor.getCurrentPosition();
        error = targetPosition - currentPosition;
        armMotor.setPower(0.5*error);
    }

}