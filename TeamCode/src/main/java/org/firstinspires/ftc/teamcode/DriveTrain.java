package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    DcMotor left, right;


    DriveTrain(DcMotor newFront, DcMotor newRight){
        left = newFront;
        right = newRight;
    }

    /**
     *
     * @param x They hrizontal value of the jostick
     * @param y vitiacl
     */
    void DriveWithJoyStick(double x,double y){
        double
            X=x,
            Y=y,
            V=(100-Math.abs(X))*(Y/100)+Y,
            W=(100-Math.abs(Y))*(X/100)+X,
            L=(V-W)/2,
            R=(V+W)/2;
        left.setPower(L);
        right.setPower(R);
    }
}
