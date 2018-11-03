package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

/*
Use of the 3 positions: open, closed and starting point to fit in the box
Sets position of the hanger on up scale of 0 to 1
Sets hanger to starting position (setOrigin)
When moved from origin, code allows hanger to return to the starting position
 */

public class Hanger {
    private Servo clawServo;
    private DcMotor extensionMotor;
    private ToggleButton decreaseValue,
                         increaseValue;
    private DigitalChannel stopExtender;
    private double origin = 0.1;
    private double currentPos;
    private DigitalChannel lowerLim;
    Timer psi = new Timer();
    private Order retractOrder;
    private int target;
    Task task;
    public Order dropOrder;
    private Order hangOrder;

    /**
     * @param hookServo servo to control the grasping
     * @param extensionMotor motor to control the extension
     */
    Hanger(Servo hookServo,
           DcMotor extensionMotor,
           DigitalChannel stopExtender,
           DigitalChannel lowerLim) {
        decreaseValue = new ToggleButton();
        increaseValue = new ToggleButton();
        this.clawServo = hookServo;
        this.extensionMotor = extensionMotor;
        this.stopExtender = stopExtender;
        this.lowerLim = lowerLim;
        currentPos = origin;
    }

    public void grip() {
        currentPos = 0.3;
        clawServo.setPosition(currentPos); // on scale of 0 to 1
    }

    public void setOrigin(double newOrigin) {
        origin = newOrigin;
    }

    public void returnToOrigin() {
        clawServo.setPosition(origin);
    }

    public void release() {
        currentPos = 0.1;
        clawServo.setPosition(currentPos);
    }

    /**
     * Use this method to find your servo limits
     * @param decrease button to decrease servo position
     * @param increase button to increase servo position
     */
    public void moveServo(boolean decrease, boolean increase) {
        if (decreaseValue.wasJustClicked(decrease)) {
            currentPos += 0.01;
        } else if (increaseValue.wasJustClicked(increase)) {
            currentPos -= 0.01;
        }
        if (currentPos > 1){
            currentPos = 1;
        } else if (currentPos < 0){
            currentPos = 0;
        }
        clawServo.setPosition(currentPos);
    }

    public void extendHook(){
        if (extensionMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        extensionMotor.setPower(1);
    }

    public void stopHook(){
        extensionMotor.setPower(0);
    }

    public void retractHook() {
        switch (retractOrder) {
            case INIT:
                if (extensionMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                    extensionMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
                target = 0;
                extensionMotor.setTargetPosition(target);
                retractOrder = Order.RUN;
                break;
            case RUN:
                int error = target - extensionMotor.getCurrentPosition();
                extensionMotor.setPower(0.5 * error);
                if ((error == 0) || (lowerLim.getState())) retractOrder = Order.DONE;
                break;
            case DONE:
                extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                stopHook();
        }
    }

    public boolean[] getState(){
        return new boolean[]{stopExtender.getState(),lowerLim.getState()};
    }

    public double getPosition() {
        return clawServo.getPosition();
    }

    double getEncoder() {
        return extensionMotor.getCurrentPosition();
    }

    DcMotor.RunMode getMode() {
        return extensionMotor.getMode();
    }

    enum Task{
        HANG,
        DROP,
        Float
    }

    public enum Order{
        INIT,
        RUN,
        DONE
    }

    void init(Task task1){
        task = task1;
        dropOrder = Order.INIT;
        hangOrder = Order.INIT;
    }
    void execute(){
        switch(task){
            case DROP:
				switch(dropOrder) {
                    case INIT:
                        extendHook();
                        if (stopExtender.getState() || (extensionMotor.getCurrentPosition() > 5000)) { // 5000 is max on encoder as a backup
                            stopHook();
                            release();
                            retractOrder = Order.INIT;
                            dropOrder = Order.RUN;
                        }
                        break;
                    case RUN:
                        retractHook();
                        if (retractOrder == Order.DONE) dropOrder = Order.DONE;
                        break;
                    case DONE:
                        stopHook();
                        task = Task.Float;
				}
                break;
            case HANG:
                switch(hangOrder) {
                    case INIT:
                        extendHook();
                        if (stopExtender.getState() || (extensionMotor.getCurrentPosition() > 5000)) { // 5000 is max on encoder as a backup
                            stopHook();
                            grip();
                            retractOrder = Order.INIT;
                            hangOrder = Order.RUN;
                        }
                        break;
                    case RUN:
                        retractHook();
                        if (retractOrder == Order.DONE) hangOrder = Order.DONE;
                        break;
                    case DONE:
                        stopHook();
                        task = Task.Float;
                }
                break;
            case Float:
                break;
            default:
        }
    }
}