package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Collector {
    private DcMotor armMotor,collectorMotor;
    private int targetPosition,
                    currentPosition,
                    error;

    /**
     *
     * @param collectorMotor motor to run the collector
     * @param armMotor motor to move the arm
     */
    public Collector(DcMotor collectorMotor,
                     DcMotor armMotor) {
        this.armMotor = armMotor;
        this.collectorMotor = collectorMotor;
        this.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void moveArmMotorAtPower(double inputPower) {
        armMotor.setPower(inputPower);
    }

    public void runCollector(){
        collectorMotor.setPower(1);
    }

    public void stopArmMotor() {
        armMotor.setPower(0);
    }

    public void stopCollector(){
        collectorMotor.setPower(0);
    }

    public void setElbowTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
        armMotor.setTargetPosition(this.targetPosition);
    }

    public void runMotorToPosition() {
        currentPosition = armMotor.getCurrentPosition();
        error = targetPosition - currentPosition;
        armMotor.setPower(0.5*error);
    }

}