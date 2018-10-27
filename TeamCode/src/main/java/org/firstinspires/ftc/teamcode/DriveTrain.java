package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    private DcMotor left, right;

    /**
     * @param newLeft Left Motor
     * @param newRight Right Motor
     */
    DriveTrain(DcMotor newLeft, DcMotor newRight){
        left = newLeft;
        right = newRight;
    }

    /**
     * @param x The horizontal value of the joystick
     * @param y The vertical value of the joystick
     */
    void driveWithJoyStick(double x, double y){
        double
            X=-x, //Forward on JoyStick is negative
            V=(100-Math.abs(X))*(y /100)+ y,
            W=(100-Math.abs(y))*(X/100)+X,
            L=(V-W)/2,
            R=(V+W)/2;
        left.setPower(L);
        right.setPower(R);
    }

    int[] getEncoderPos() {
        return new int[]{left.getCurrentPosition(),right.getCurrentPosition()};
    }
}