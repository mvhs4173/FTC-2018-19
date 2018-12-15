package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;


/**
 * This class controls the mechanism to claim the depot with our marker
 */
public class MarkerArm {
    // The objects needed
    Servo markerArmServo;
    double currentPos;
    double origin = 0.1;
    Timer t = new Timer();
    ToggleButton increaseValue = new ToggleButton(), decreaseValue = new ToggleButton();

    /**
     * the Constructor
     * @param markerArmServo What servo is controlling the marker
     */
    MarkerArm(Servo markerArmServo) {
        this.markerArmServo = markerArmServo;
        returnToStow();
    }

    /**
     * Moves the servo to the start position
     */
    public void returnToStow() {
        currentPos = origin;
        markerArmServo.setPosition(currentPos);
    }

    /**
     * sets the desired position for the servo
     * @param position weher to move the servo, between 0 and 1
     */
    public void setPosition(double position) {
        markerArmServo.setPosition(position);
    }

    /**
     * Sets a timer to give the servo time to move
     * @param time how long to wait before returning true
     */
    public void intiTime(double time){
        t.init(time);
    }

    /**
     * throws the marker in to the depot
     * @return tells us if the command is done
     */
    public boolean release() {
        currentPos = 0.9;
        markerArmServo.setPosition(currentPos);
        return t.isTimerUp();
    }

    /**
     * What is the current position of the servo
     * @return a number between 0 and 1
     */
    double getCurrentPos() {
        return currentPos;
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
        markerArmServo.setPosition(currentPos);
    }
}