package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by ROBOT18 on 9/28/2017.
 */

public class DriveTrain {
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftRear = null;
    DcMotor rightRear = null;
    Gamepad gamepad1 = null;
    public DriveTrain(Gamepad newgamepad,
                      DcMotor newLeftFrontMotor,
                      DcMotor newLeftBackMotor,
                      DcMotor newRightFrontMotor,
                      DcMotor newRightBackMotor) {
        leftFront = newLeftFrontMotor;
        rightFront = newRightFrontMotor;
        leftRear = newLeftBackMotor;
        rightRear = newRightBackMotor;
        gamepad1 = newgamepad;
    }

    public void setDrivePower(double x1, double y, double x2){
        double y1 = -y; // the game pad gives a negative number for forward. the math is looking for a positive for forward.
        double x = x1;
        double r = Math.hypot(x, y1);
        double robotAngle = Degrees.atan2(y1, x1) - 35; //Math.PI / 4;
        double rightX = x2*1;
        final double v1 = r * Degrees.cos(robotAngle) + rightX;
        final double v2 = r * Degrees.sin(robotAngle) - rightX;
        final double v3 = r * Degrees.sin(robotAngle) + rightX;
        final double v4 = r * Degrees.cos(robotAngle) - rightX;

        leftFront.setPower(v1*(2/Math.sqrt(2)));
        rightFront.setPower(v2*(2/Math.sqrt(2)));
        leftRear.setPower(v3*(2/Math.sqrt(2)));
        rightRear.setPower(v4*(2/Math.sqrt(2)));
    }

    /**
     *
     * @param theta in degress. 90 is forward
     * @param power 0 - 1
     */
    public void driveInDirection(double theta, double power){
        theta += 10;//To componsate for error in strafing
        leftFront.setPower(Degrees.cos(theta-45)*power);
        rightFront.setPower(Degrees.sin(theta-45)*power);
        leftRear.setPower(Degrees.sin(theta-45)*power);
        rightRear.setPower(Degrees.cos(theta-45)*power);
    }

    /**
     *
     * @param power Positive power is rotate right
     *              Negative power is rotate left
     */
    public void rotateInPlace(double power){
        leftFront.setPower(+power);
        rightFront.setPower(-power);
        leftRear.setPower(+power);
        rightRear.setPower(-power);
    }

    public void stopMotor(){
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }
	
	public boolean maintainHeading(double desiredHeading, Compass compass) {
        double currentHeading = compass.getHeading();
        double allowableError = 1;//degrees

        double pFactor = 0.07;
        double error = Degrees.subtract(desiredHeading, currentHeading);

        //If the robot is too far off the desired angle then continue with P loop
        if (Math.abs(error) > allowableError) {
            double newSpeed = error * pFactor;

            leftFront.setPower(newSpeed);
            leftRear.setPower(newSpeed);
            rightFront.setPower(-newSpeed);
            rightRear.setPower(-newSpeed);

            return false;
        }else {
            return true;
        }
    }

    double getSpeeds(int n){
        double[] newArray = new double[4];
        newArray[0] = leftFront.getPower();
        newArray[1] = leftRear.getPower();
        newArray[2] = rightFront.getPower();
        newArray[3] = rightRear.getPower();
        return newArray[n];
    }
}
