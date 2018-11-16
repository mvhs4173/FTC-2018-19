package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an OpMode.
 * This class can be used to define all the specific hardware for up single robot.
 * In this case that robot is up PushBot.
 * See Testing_TelemeterOp and others classes starting with "PushBot" for usage examples.
 * This hardware class assumes the following device names have been configured on the robot
 * Note: All names are lower case and some have single spaces between words.
 * Motor channel: Left drive motor: "left_drive"
 * Motor channel: Right drive motor: "right_drive"
 */
public class Hardware {
    /* Public OpMode members. */
    //You should use the acronyms for future reference, don't worry, it's fixed//
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    //public DcMotor collectorMotor = null;
    //public DcMotor armMotor = null;
    public DcMotor extensionMotor = null;
    public BNO055IMU imu = null;
    public Servo hookServo = null;
    Servo markerServo;
    public DigitalChannel extenderStop = null;
    Compass compass = null;

    /* local OpMode members. */
    private HardwareMap hwMap  =  null;
    private ElapsedTime period  = new ElapsedTime();
    public DigitalChannel extenderLowerLim;

    /* Constructor */
    public Hardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        imu = hwMap.get(BNO055IMU.class, "imu");
        compass = new Compass(imu);

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("Left Motor");
        rightMotor = hwMap.dcMotor.get("Right Motor");
        //collectorMotor = hwMap.dcMotor.get("Collector Motor");
        //armMotor = hwMap.dcMotor.get("Arm Motor");
        extensionMotor = hwMap.dcMotor.get("Extension Motor");

        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        //collectorMotor.setDirection(DcMotor.Direction.FORWARD);
        //armMotor.setDirection(DcMotor.Direction.FORWARD);
        extensionMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //collectorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //collectorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and Initialize Servos
        hookServo = hwMap.servo.get("Hanger Servo");
        extenderStop = hwMap.digitalChannel.get("Extender Switch");
        extenderLowerLim = hwMap.digitalChannel.get("E Lower Lim");

        markerServo = hwMap.servo.get("Minion Servo");
    }

    /***
     * waitForTick implements up periodic delay. However, this acts like up metronome with up regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {
        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}