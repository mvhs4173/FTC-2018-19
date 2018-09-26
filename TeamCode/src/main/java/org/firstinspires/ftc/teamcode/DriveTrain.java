package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    DcMotor left, right;


    DriveTrain(DcMotor newFront, DcMotor newRight){
        left = newFront;
        right = newRight;
    }

    void DriveWithJoyStick(double x,double y){
        double X,Y,V,W,L,R;
        X=x;
        Y=y;
        V=(100-Math.abs(X))*(Y/100)+Y;
        W=(100-Math.abs(Y))*(X/100)+X;
        L=(V-W)/2;
        R=(V+W)/2;
        left.setPower(L);
        right.setPower(R);
    }
}
