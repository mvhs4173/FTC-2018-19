package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 */
public class HardwarePushbot {
    /* Public OpMode members. */
    //You should use the acronyms for future reference, don't worry, it's fixed//

    public DcMotor leftFrontMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightBackMotor = null;
    public DcMotor collectorMotor = null;
    public DcMotor collectorMotor2 = null;
    public BNO055IMU imu = null;
    public Servo clawServo = null;

    /* local OpMode members. */
    HardwareMap hwMap  =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushbot(){
    }

    /* Initialize standard HardwarePushbot interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to HardwarePushbot map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFrontMotor = hwMap.dcMotor.get("Front Left Motor");

        imu = hwMap.get(BNO055IMU.class, "imu");

        rightFrontMotor  = hwMap.dcMotor.get("Front Right Motor");

        collectorMotor = hwMap.dcMotor.get("Collector Motor");

        collectorMotor2 = hwMap.dcMotor.get("Collector Motor 2");

        leftFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        collectorMotor.setDirection(DcMotor.Direction.FORWARD);
        collectorMotor2.setDirection(DcMotor.Direction.FORWARD);

        // Define and Initialize Servos
        clawServo = hwMap.servo.get("Claw Servo");
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
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