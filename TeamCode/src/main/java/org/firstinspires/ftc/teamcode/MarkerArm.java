package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;



public class MarkerArm {
    Servo markerArmServo;
    double currentPos;
    double origin = 0.1;
    ToggleButton increaseValue = new ToggleButton(), decreaseValue = new ToggleButton();

    MarkerArm(Servo markerArmServo) {
        this.markerArmServo = markerArmServo;
        returnToStow();
    }

    public void returnToStow() {
        currentPos = origin;
        markerArmServo.setPosition(currentPos);
    }

    public void setPosition(double position) {
        markerArmServo.setPosition(position);
    }

    Timer t = new Timer();
    public void intiTime(double time){
        t.init(time);
    }

    public boolean release() {
        currentPos = 0.9;
        markerArmServo.setPosition(currentPos);
        return t.isTimerUp();
    }

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