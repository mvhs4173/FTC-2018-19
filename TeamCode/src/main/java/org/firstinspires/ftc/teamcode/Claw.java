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
    double origin = 0.5;

    public Claw(Servo clawServo,
                DcMotor extensionMotor) {
        this.clawServo = clawServo;
        this.extensionMotor = extensionMotor;
    }

    public void Grip(double inputPosition) {
        clawServo.setPosition(inputPosition); // on scale of 0 to 1
    }

    public void setOrigin(double newOrigin) {
        origin = newOrigin;
    }

    public void returnToOrigin() {
        clawServo.setPosition(origin);
    }

    public void Release(double inputPosition) {
        clawServo.setPosition(inputPosition);
    }
}