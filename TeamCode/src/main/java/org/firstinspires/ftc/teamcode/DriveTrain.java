package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * This is the class that controls driving the robot
 */
public class DriveTrain {
    // Objects needed for functioning
    DcMotor left, right;
    Imu imu;
    double linePFactor = 0.05;
    double lineAllowableAngleError = 0.1;
    double lineMaxMultiplyNumber = 3;
    private double wheelDiameter = 4;
    private double clicksPerRev = 280;
    private double clicksPerInch = clicksPerRev/(Math.PI*wheelDiameter); // clicks/rev * (inch/rev)^-1

    //Speed controller Variables
    double rightSpeedP = 0.001;
    double speedP = 0.0001;//Speed proprotional factor
    double lastInchesDrivenLeft = 0.0;
    double lastInchesDrivenRight = 0.0;
    double currentRightPower = 0.0;
    double currentLeftPower = 0.0;
    int loopIterationsPerSecond = 1000/20;

    //What is considered '0' on the encoders
    private int leftOriginPosition = 0;
    private int rightOriginPosition = 0;

    /**
     * The Constructor
     * @param newLeft Left Motor from the hardware
     * @param newRight Right Motor from the hardware
     */
    DriveTrain(DcMotor newLeft, DcMotor newRight, Imu imu){
        left = newLeft;
        right = newRight;
        lastInchesDrivenLeft = getLeftInchesDriven();
        lastInchesDrivenRight = getRightInchesDriven();
        this.imu = imu;
    }

    /**
     * used in the teleop for driving manually
     * @param x The horizontal value of the joystick
     * @param y The vertical value of the joystick
     */
    void driveWithJoyStick(double x, double y){
        //Forward on JoyStick is negative due to the way it was built
        double
            Y = Math.signum(y)*Math.pow(Math.abs(y), 2.5),
            V=(1-Math.abs(x))*(Y /1)+ Y,
            W=(1-Math.abs(Y))*(x /1)+ x,
            L=(V-W)/2,
            R=(V+W)/2;
        left.setPower(-L);
        right.setPower(-R);
    }

    /**
     * Used to check the positions of the motors in clicks from the starting position
     * @return 0 is the left motor index, 1 is the right motor index
     */
    int[] getEncoderPos() {
        return new int[]{left.getCurrentPosition() - leftOriginPosition, right.getCurrentPosition() - rightOriginPosition};
    }

    /**
     * used to reset the position of the motor to 0
     */
    public void resetLeftEncoder() {
        leftOriginPosition = left.getCurrentPosition();
        lastInchesDrivenLeft = 0.0;
    }

    /**
     * used to reset the position of the motor to 0
     */
    public void resetRightEncoder() {
        rightOriginPosition = right.getCurrentPosition();
        lastInchesDrivenRight = 0.0;
    }

    /**
     * this is a method from last year
     * used to help drive in a straight line
     * @param desiredHeading the angle from the imu you want to stay pointed
     * @return tells us if we are going at the right angle
     */
    public boolean maintainHeading(double desiredHeading) {
        double currentHeading = this.imu.getHeading();
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

    /**
     * used for when we want to stop the motors
     * typically called after a command is finished
     */
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
        //double[] newMotorSpeeds = calculateMotorSpeedsForStraightLine(1, desiredHeading, bno.getHeading());
        if (maintainHeading(desiredHeading)) {
            left.setPower(/*newMotorSpeeds[0]*/-0.5*error[0]);
            right.setPower(/*newMotorSpeeds[1]*/-0.5*error[1]);
        } else {
            stopMotors();
        }
        return error[0] == 0;
    }

    /**
     * how far has the left side moved
     * @return in inches the distance traveled
     */
    public double getLeftInchesDriven() {
        double ticks = left.getCurrentPosition();
        double revolutions = ticks/clicksPerRev;
        double inchesDriven = revolutions * wheelDiameter;
        return inchesDriven;
    }

    /**
     * how far has the right side moved
     * @return in inches the distance traveled
     */
    public double getRightInchesDriven() {
        double ticks = right.getCurrentPosition();
        double revolutions = ticks/clicksPerRev;
        double inchesDriven = revolutions * wheelDiameter;
        return inchesDriven;
    }

    /**
     * Drive inches per second, must be called at 20ms intervals
     */
    public void driveLeftIPS(double ips) {
        double currentIPS = getLeftSpeedIPS();
        double error = ips - currentIPS;

        double newPower = left.getPower() + (error * speedP);
        CubeAuto.t.addData("Left Adjustment: ", error*speedP);
        left.setPower(newPower);
    }

    /**
     * Drive inches per second, must be called at 20ms intervals
     */
    public void driveRightIPS(double ips) {
        double currentIPS = getRightSpeedIPS();
        double error = ips - currentIPS;

        double newPower = right.getPower() + (error * rightSpeedP);
        CubeAuto.t.addData("Right Power: ", newPower);
        right.setPower(newPower);
    }

    /**
     * the factor at which to tune proportionally
     * @param pFactor typically a value less than 1
     */
    public void setSpeedPFactor(double pFactor) {
        speedP = pFactor;
    }

    /**
     * What pFactor do you currently have
     * @return a value less than 1
     */
    public double getSpeedPFactor() {
        return speedP;
    }

    /**
     * how fast the left side is currently traveling
     * @return in inches per second
     */
    public double getLeftSpeedIPS() {
        double currentInchesDriven = getLeftInchesDriven();
        double speed = (currentInchesDriven - lastInchesDrivenLeft) * loopIterationsPerSecond;
        lastInchesDrivenLeft = currentInchesDriven;
        return speed;
    }

    /**
     * how fast the right side is currently traveling
     * @return in inches per second
     */
    public double getRightSpeedIPS() {
        double currentInchesDriven = getRightInchesDriven();
        double speed = (currentInchesDriven - lastInchesDrivenRight) * loopIterationsPerSecond;
        lastInchesDrivenRight = currentInchesDriven;
        return speed;
    }

    /**
     * used for turning the robot automatically
     * @param desiredAngle which way do you want to point
     */
    void rotateToAngle(double desiredAngle) {
        double error = desiredAngle - imu.getHeading();
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