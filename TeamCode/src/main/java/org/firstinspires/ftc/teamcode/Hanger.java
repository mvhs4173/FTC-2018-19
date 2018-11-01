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
    private double origin = 0.3;
    private double currentPos;
    private DigitalChannel lowerLim;
    Timer psi = new Timer();
    private boolean hasBeenPressed;

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
        hasBeenPressed = false;
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

    private enum Order{
        INIT,
        RUN,
        DONE
    }

    Order order;
    int target;
    int error;
    private void retractHook() {
        switch (order) {
            case INIT:
                if (extensionMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                    extensionMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
                target = 0;
                extensionMotor.setTargetPosition(target);
                order = Order.RUN;
                break;
            case RUN:
                error = target - extensionMotor.getCurrentPosition();
                extensionMotor.setPower(0.5 * error);
                if ((error == 0) || (lowerLim.getState())) order = Order.DONE;
                break;
            case DONE:
                extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                stopHook();
                if (hasBeenPressed) {
                    hasBeenPressed = false;
                }
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

    Task task;
    void init(Task task1){
        task = task1;
    }
    void execute(){
        switch(task){
            case DROP:
                if (!hasBeenPressed){
                    extendHook();
                }
                if (stopExtender.getState() && !hasBeenPressed){
                    hasBeenPressed = true;
                    stopHook();
                    release();
                    psi.init(1);
                    order = Order.INIT;
                }
                if (psi.isTimerUp()) {
                    retractHook();
                    if (order == Order.DONE) {
                        psi.disable();
                        task = Task.Float;
                    }
                }
                break;
            case HANG:
                if (!hasBeenPressed){
                    extendHook();
                    release();
                }
                if (stopExtender.getState() && !hasBeenPressed){
                    hasBeenPressed = true;
                    stopHook();
                    grip();
                    psi.init(1);
                    order = Order.INIT;
                }
                if (psi.isTimerUp()) {
                    retractHook();
                    if (order == Order.DONE) {
                        psi.disable();
                        task = Task.Float;
                    }
                }
                break;
            case Float:
                break;
            default:
        }
    }
}