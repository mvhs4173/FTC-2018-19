package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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