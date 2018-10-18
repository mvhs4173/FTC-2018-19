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

    public void runArmToGather() {
        setElbowTargetPosition(0);
        currentPosition = armMotor.getCurrentPosition();
        error = this.targetPosition - currentPosition;
        armMotor.setPower(0.5*error);
        if (currentPosition == targetPosition) stopArmMotor();
    }

    public void runArmToDispence(){
        setElbowTargetPosition(4000);
        currentPosition = armMotor.getCurrentPosition();
        error = this.targetPosition - currentPosition;
        armMotor.setPower(0.5*error);
        if (currentPosition == targetPosition) stopArmMotor();
    }

    public int getCurerntPosition(){
        return armMotor.getCurrentPosition();
    }
}