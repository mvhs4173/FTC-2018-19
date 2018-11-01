package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


/**
 * Created by ROBOT16 on 10/31/2017.
 */

@SuppressWarnings("unused")
public class Compass {
    private BNO055IMU compass;
    private double originalRawHeading = 0;
    private double originPitch = 0;

    Compass(BNO055IMU compass) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        compass.initialize(parameters);

        this.compass = compass;
    }

    //Returns the robots heading in angles

    /**
     *
     * @return Returns the current direction in degrees that the robot is facing. Positive means the robot is facing to the right of origin, Negative means its facing to the left of origin
     */
    public double getHeading() {
        return Degrees.subtract(getRawHeading(), originalRawHeading);//Positive angle is up right turn negative angle is up left turn
    }

    /**
     *
     * @return Returns the current pitch of the robot in degrees. Positive means its tilted to the right of origin
     */
    public double getPitch() {
        Orientation angles = compass.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return Degrees.subtract(angles.thirdAngle, originPitch);//Positive angle is up right pitch negative angle is left pitch
    }

    //Set the current heading to zero

    /**
     * Sets the current heading of the robot as the origin Heading
     */
    public void resetHeading() {
        originalRawHeading = getRawHeading();
    }

    public double getOriginalRawHeading() {
        return originalRawHeading;
    }

    //Set the current pitch to zero

    /**
     * Set thes current Pitch of the robot as the origin pitch
     */
    public void resetPitch() {
        originPitch = getHeading();
    }

    public double getRawHeading(){
        Orientation angles = compass.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return -angles.firstAngle;
    }

}
