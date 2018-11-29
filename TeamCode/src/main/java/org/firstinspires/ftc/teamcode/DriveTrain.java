package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    DcMotor left, right;
    Compass  compass;
    double linePFactor = 0.05;
    double lineAllowableAngleError = 0.1;
    double lineMaxMultiplyNumber = 3;
    private double wheelDiameter = 4;
    private double clicksPerRev = 280;
    private double clicksPerInch = clicksPerRev/(Math.PI*wheelDiameter); // clicks/rev * (inch/rev)^-1

    /**
     * @param newLeft Left Motor
     * @param newRight Right Motor
     */
    DriveTrain(DcMotor newLeft, DcMotor newRight, Compass compass){
        left = newLeft;
        right = newRight;
        this.compass = compass;
    }

    /**
     * @param x The horizontal value of the joystick
     * @param y The vertical value of the joystick
     */
    void driveWithJoyStick(double x, double y){
        //Forward on JoyStick is negative
        double
            Y = Math.signum(y)*Math.pow(Math.abs(y), 2.5),
            V=(1-Math.abs(x))*(Y /1)+ Y,
            W=(1-Math.abs(Y))*(x /1)+ x,
            L=(V-W)/2,
            R=(V+W)/2;
        left.setPower(L);
        right.setPower(R);
    }

    int[] getEncoderPos() {
        return new int[]{left.getCurrentPosition(),right.getCurrentPosition()};
    }

    public boolean maintainHeading(double desiredHeading) {
        double currentHeading = this.compass.getHeading();
        double allowableError = 1;//degrees

        double pFactor = 0.00009;
        double error = Degrees.subtract(desiredHeading, currentHeading);

        //If the robot is too far off the desired angle then continue with P loop
        if (Math.abs(error) > allowableError) {
            double newSpeed = error * pFactor;

            if (newSpeed > .5){
                newSpeed = 0.5;
            } else if (newSpeed < -0.5) {
                newSpeed = -0.5;
            }
            left.setPower(-newSpeed);
            right.setPower(newSpeed);

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
     * For driving in a straight line
     * @param distance how far to drive in inches
     * @param desiredHeading in degrees
     */
    public boolean driveDistance(double distance, double desiredHeading) {
        double[] currentPosition = {getEncoderPos()[0], getEncoderPos()[1]};
        double[] error = {(distance * clicksPerInch)-currentPosition[0],(distance * clicksPerInch)-currentPosition[1]};
        //double[] newMotorSpeeds = calculateMotorSpeedsForStraightLine(1, desiredHeading, compass.getHeading());
        if (maintainHeading(desiredHeading)) {
            left.setPower(/*newMotorSpeeds[0]*/-0.5*error[0]);
            right.setPower(/*newMotorSpeeds[1]*/-0.5*error[1]);
        } else {
            stopMotors();
        }
        return error[0] == 0;
    }

    public double getLeftInchesDriven() {
        return getEncoderPos()[0];
    }

    public double getRightInchesDriven() {
        return getEncoderPos()[1];
    }

    void rotateToAngle(double desiredAngle) {
        double error = desiredAngle - compass.getHeading();
        if (error > 0) {
            left.setPower(error * -0.05);
            right.setPower(error *  0.05);
        } else if (error < 0) {
            left.setPower(error * 0.05);
            right.setPower(error * -0.05);
        } else {
            left.setPower(0);
            right.setPower(0);
        }
    }

    /**
     * With the given information this method will calculate the appropriate speeds that the motors on the left and right side of the robot will need to rotate at to maintain a straight line in the direction specified
     * @param desiredSpeed A number that will be multiplied by the final result to increase the speeds of the motors
     * @param desiredHeading The direction in Degrees that you want the robot to face when driving in a line
     * @param currentHeading The current heading of the robot in degrees (Must be an angle between -180 and +180 where a positive angle is a clockwise turn
     * @return A double array with 2 indexes containing the new speeds that should be applied to the left and right motors of the robot, The first index is the new speed for the Left Drive Unit
     */
    private double[] calculateMotorSpeedsForStraightLine(double desiredSpeed, double desiredHeading, double currentHeading) {
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