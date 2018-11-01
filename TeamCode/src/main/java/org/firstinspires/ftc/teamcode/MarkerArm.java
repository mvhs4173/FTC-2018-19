package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class MarkerArm {
    Servo minionServo;
    double currentPos;
    double origin = 0.5;
    ToggleButton increaseValue = new ToggleButton(), decreaseValue = new ToggleButton();

    MarkerArm(Servo minionServo) {
        this.minionServo = minionServo;
    }

    public void returnToOrigin() {
        currentPos = origin;
        minionServo.setPosition(currentPos);
    }

    public boolean release() {
        Timer t = new Timer();
        t.init(0.5);
        currentPos = 0;
        minionServo.setPosition(currentPos);
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
        minionServo.setPosition(currentPos);
    }
}
