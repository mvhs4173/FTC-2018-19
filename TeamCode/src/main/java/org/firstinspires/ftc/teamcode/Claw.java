package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo clawServo;
    double origin = 0.5;

    public Claw(Servo clawServo) {
        this.clawServo = clawServo;
    }

    public void Grip(double inputPosition) {
        clawServo.setPosition(inputPosition);
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
