package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

/*
Use of the 3 positions: open, closed and starting point to fit in the box
Sets position of the hanger on up scale of 0 to 1
Sets hanger to starting position (setOrigin)
When moved from origin, code allows hanger to return to the starting position
 */

/**
 * This class Controls our hanging mechanism.
 */
public class Hanger {
    private Servo clawServo;
    private DcMotorEx extensionMotor;
    private ToggleButton decreaseValue,
                         increaseValue;
    private DigitalChannel upperLim;
    private double origin = 0.1;
    private double currentPos;
    private DigitalChannel lowerLim;
    Timer psi = new Timer();
    public Order retractOrder;
    private int target;
    Task task;
    public Order dropOrder;
    private Order hangOrder;
    private int encoderLim = 2400; //the upper bound on the encoder

    /**
     * @param hookServo servo to control the grasping
     * @param extensionMotor motor to control the extension
     * @param lowerLim The limit switch to stop decent of the claw, also resets encoder
     * @param upperLim the limit switch to stop accent of the motor, encoder is used as backup 
     */
    Hanger(Servo hookServo,
           DcMotorEx extensionMotor,
           DigitalChannel upperLim,
           DigitalChannel lowerLim) {
        decreaseValue = new ToggleButton();
        increaseValue = new ToggleButton();
        this.clawServo = hookServo;
        this.extensionMotor = extensionMotor;
        this.upperLim = upperLim;
        this.lowerLim = lowerLim;
        currentPos = origin;
        extensionMotor.setMotorEnable();
    }

    /**
     * Closes the Claw on the hanger
     * use moveServo method to find an appropriate value 
     */
    public void grip() {
        currentPos = 0.3;
        clawServo.setPosition(currentPos); // on scale of 0 to 1
    }

    /**
     * If needed you can reset the origin 
     * @param newOrigin new desired origin position
     */
    public void setOrigin(double newOrigin) {
        origin = newOrigin;
    }

    /**
     * returns the servo to the origin position set. 
     * This can be use for starting position.
     */
    public void returnToOrigin() {
        clawServo.setPosition(origin);
    }

    /**
     * Opens the Claw on the hanger
     * use moveServo method to find an appropriate value 
     */
    public void release() {
        currentPos = 0.05;
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

    /**
     * This extends the linear slide. 
     * before setting the power we check to make sure we are in the right mode
     * if not we change it prior to running.
     */
    public void extendHook(){
        if (extensionMotor.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
            extensionMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        extensionMotor.setPower(1);
    }

    public void setExtensionMotorPower(double power) {
        extensionMotor.setPower(power);
    }

    /**
     * This is a backup method to control the extension motor manually
     * @param up button to specify up
     * @param down button to specify down
     */
    public void runManually(boolean up, boolean down){
        if (up) extensionMotor.setPower(1);
        else if (down) extensionMotor.setPower(-1);
        else stopHook();
    }

    /**
     * This method is used to stop the motor.
     * Usually call this at the end of a task
     */
    public void stopHook(){
        extensionMotor.setPower(0);
    }

    /**
     * For when the arm gets initialized mid way we can pull it back to reset. 
     * @return tells us if the command is finished
     */
    public boolean resetZero() {
        if (extensionMotor.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
            extensionMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        if (lowerLim.getState()){
            extensionMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            stopHook();
            return true;
        } else {
            extensionMotor.setPower(-0.5);
            return false;
        }
    }

    /**
     * This retracts the hook 
     * We use a switch statement to verify things are called in the right order
     * Sets the mode if not set 
     */
    public void retractHook() {
        switch (retractOrder) {
            case INIT:
                if (extensionMotor.getMode() != DcMotorEx.RunMode.RUN_TO_POSITION) {
                    extensionMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                }
                target = -encoderLim; // TODO: 12/14/2018 the retraction is currently counting twice as fast
                extensionMotor.setTargetPosition(target);
                retractOrder = Order.RUN;
                break;
            case RUN:
                int error = target - extensionMotor.getCurrentPosition();
                extensionMotor.setPower(0.5 * error);
                if ((error == 0) || (lowerLim.getState())) retractOrder = Order.DONE;
                break;
            case DONE:
                extensionMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                stopHook();
        }
    }

    /**
     * Tells you the state of the limit switches on the robot
     * Index 0 is the upper Switch
     * Index 1 is the lower Switch
     * @return True if pressed
     */
    public boolean[] getState(){
        return new boolean[]{upperLim.getState(),lowerLim.getState()};
    }

    /**
     * Used to verify the right position
     * @return The servos position from 0 to 1
     */
    public double getPosition() {
        return clawServo.getPosition();
    }

    /**
     * Used for checking the motors position
     * @return Motor position in clicks
     */
    double getEncoder() {
        return extensionMotor.getCurrentPosition();
    }

    /**
     * Used to check the mode currently set
     * @return The current RunMode of the motor.
     */
    DcMotorEx.RunMode getMode() {
        return extensionMotor.getMode();
    }

    /**
     * Our list used in the main Switch in the execute function
     */
    enum Task{
        HANG,
        DROP,
        Float,
        RESET
    }

    /**
     * Used in the sub Switches in the Execute function
     */
    public enum Order{
        INIT,
        WAIT,
        RUN,
        DONE
    }

    /**
     * Initializes the Variables for running execute
     * prevents null objects
     * @param task1 The desired Operation Hang or Drop
     */
    void init(Task task1){
        task = task1;
        dropOrder = Order.INIT;
        hangOrder = Order.INIT;
    }

    /**
     * our main call function. we put this in our loop of the TeleOp.
     * automatically preforms the desired task Hang or Drop
     * @param button the button you want to use to say you are in position
     */
    void execute(boolean button){
        switch(task){
            case DROP:
				switch(dropOrder) {
                    case INIT:
                        extendHook();
                        if (upperLim.getState() || (extensionMotor.getCurrentPosition() > encoderLim)) { // encoderLim is max on encoder as a backup
                            stopHook();
                            release();
                            psi.init(1);
                            retractOrder = Order.INIT;
                            dropOrder = Order.RUN;
                        }
                        break;
                    case RUN:
                        if (psi.isTimerUp()) {
                            retractHook();
                        }
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
                        if (upperLim.getState() || (extensionMotor.getCurrentPosition() > encoderLim)) { // encoderLim is max on encoder as a backup
                            stopHook();
                            grip();
                            retractOrder = Order.INIT;
                            hangOrder = Order.WAIT;
                        }
                        break;
                    case WAIT:
                        if (button) hangOrder = Order.RUN;
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
            case RESET:
                if (resetZero()) task = Task.Float;
                break;
            case Float:
                break;
            default:
        }
    }
}