package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    DcMotor left, right;
    double linePFactor = 0.05;
    double lineAllowableAngleError = 0.1;
    double lineMaxMultiplyNumber = 3;

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
            X=-x,
            Y=y, //Forward on JoyStick is negative
            V=(100-Math.abs(X))*(Y/100)+Y,
            W=(100-Math.abs(Y))*(X/100)+X,
            L=(V-W)/2,
            R=(V+W)/2;
        left.setPower(L);
        right.setPower(R);
    }

    int[] getEncoderPos() {
        return new int[]{left.getCurrentPosition(),right.getCurrentPosition()};
    }

    public boolean maintainHeading(double desiredHeading, Compass compass) {
        double currentHeading = compass.getHeading();
        double allowableError = 1;//degrees

        double pFactor = 0.07;
        double error = Degrees.subtract(desiredHeading, currentHeading);

        //If the robot is too far off the desired angle then continue with P loop
        if (Math.abs(error) > allowableError) {
            double newSpeed = error * pFactor;

            left.setPower(newSpeed);
            right.setPower(-newSpeed);

            return false;
        }else {
            return true;
        }
    }

    public void stopMotors() {
        left.setPower(0);
        right.setPower(0);
    }

    /**
     *
     * @param compass compass to use for heading
     * @param time how long to DRIVE for
     */
    public boolean driveDistance(Compass compass, double time) {
        Timer timer = new Timer();
        timer.init(time);
        double[] newMotorSpeeds = calculateMotorSpeedsForStraightLine(1, 90, compass.getHeading());
        if (!timer.isTimerUp()) {
            left.setPower(newMotorSpeeds[0]);
            right.setPower(newMotorSpeeds[1]);
        } else {
            stopMotors();
        }
        return timer.isTimerUp();
    }

    /**
     * With the given information this method will calculate the appropriate speeds that the motors on the left and right side of the robot will need to rotate at to maintain a straight line in the direction specified
     * @param desiredSpeed A number that will be multiplied by the final result to increase the speeds of the motors
     * @param desiredHeading The direction in Degrees that you want the robot to face when driving in a line
     * @param currentHeading The current heading of the robot in degrees (Must be an angle between -180 and +180 where a positive angle is a clockwise turn
     * @return A double array with 2 indexes containing the new speeds that should be applied to the left and right motors of the robot, The first index is the new speed for the Left Drive Unit
     */
    public double[] calculateMotorSpeedsForStraightLine(double desiredSpeed, double desiredHeading, double currentHeading) {
        double newRightSpeed;
        double newLeftSpeed;
        double angleError = Degrees.subtract(currentHeading, desiredHeading);
        boolean isErrorNegative = angleError < 0;
        angleError = Math.sqrt(Math.abs(angleError));
        if (isErrorNegative) {
            angleError = -angleError;
        }
        double multiplyNumber = angleError * linePFactor;
        //Make sure the number is not too large or too small
        multiplyNumber = Math.min(lineMaxMultiplyNumber, Math.max(multiplyNumber, -lineMaxMultiplyNumber));
        //SmartDashboard.putNumber("CurrentAngle", currentAngle);
        //SmartDashboard.putNumber("Desired angle", desiredHeading);
        //SmartDashboard.putNumber("Turn Error", angleError);
        newLeftSpeed = (1 - multiplyNumber) * desiredSpeed;
        newRightSpeed = (1 + multiplyNumber) * desiredSpeed;
        //SmartDashboard.putNumber("Requested Left Speed",  newLeftSpeed);
        //SmartDashboard.putNumber("Requested Right Speed",  newRightSpeed);
        return new double[]{newLeftSpeed, newRightSpeed};
    }
}