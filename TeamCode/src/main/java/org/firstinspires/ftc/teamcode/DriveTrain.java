package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    DcMotor left, right;


    DriveTrain(DcMotor newFront, DcMotor newRight){
        left = newFront;
        right = newRight;
    }

    void DriveWithJoyStick(double x,double y){
        double
            X=x,
            Y=-y, //Forward on JoyStick is negative
            V=(100-Math.abs(X))*(Y/100)+Y,
            W=(100-Math.abs(Y))*(X/100)+X,
            L=(V-W)/2,
            R=(V+W)/2;
        left.setPower(L);
        right.setPower(R);
    }

    int[] getEncoderPos() {
        int[] temp = {left.getCurrentPosition(),right.getCurrentPosition()};
        return temp;
    }
}
