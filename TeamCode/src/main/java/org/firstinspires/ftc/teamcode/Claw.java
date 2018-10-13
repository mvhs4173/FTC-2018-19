package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*
Use of the 3 positions: open, closed and starting point to fit in the box
Sets position of the claw on a scale of 0 to 1
Sets claw to starting position (setOrigin)
When moved from origin, code allows claw to return to the starting position
 */

public class Claw {
    private Servo clawServo;
    private DcMotor extensionMotor;
    private RogersButton decreaseValue,
                         increaseValue;
    double origin = 0.4;
    private double currentPos;

    public Claw(Servo clawServo,
                DcMotor extensionMotor) {
        decreaseValue = new RogersButton();
        increaseValue = new RogersButton();
        this.clawServo = clawServo;
        this.extensionMotor = extensionMotor;
        currentPos = origin;
    }

    public void Grip() {
        currentPos = 0.48;
        clawServo.setPosition(currentPos); // on scale of 0 to 1
    }

    public void setOrigin(double newOrigin) {
        origin = newOrigin;
    }

    public void returnToOrigin() {
        clawServo.setPosition(origin);
    }

    public void Release() {
        currentPos = 0.3;
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
}