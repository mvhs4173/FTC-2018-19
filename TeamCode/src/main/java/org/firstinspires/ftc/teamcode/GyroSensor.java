package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@SuppressWarnings("unused")
public class GyroSensor {
    private BNO055IMU gyroSensor;

    public GyroSensor(BNO055IMU imu) {
        this.gyroSensor = imu;
    }

   // Gets the heading in degrees
    double getHeading (){
        Orientation orientation = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return (double) orientation.thirdAngle;
    }
    // Gets the yaw in degrees
    double getYaw (){
        Orientation orientation = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return (double) orientation.firstAngle;
    }
    // Gets the roll in degrees
    double getroll (){
        Orientation orientation = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return (double) orientation.secondAngle;
    }
}
