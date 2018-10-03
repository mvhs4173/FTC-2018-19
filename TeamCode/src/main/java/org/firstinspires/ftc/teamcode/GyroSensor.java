package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class GyroSensor {
    BNO055IMU gyroSensor;

    public GyroSensor(BNO055IMU imu) {
        this.gyroSensor = imu;
    }

   // Gets the heading in degrees
    double getHeading (){
        Orientation orientation = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double heading = orientation.thirdAngle;
        return heading;
    }
    // Gets the yaw in degrees
    double getYaw (){
        Orientation orientation = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double yaw = orientation.firstAngle;
        return yaw;
    }
    // Gets the roll in degrees
    double getroll (){
        Orientation orientation = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double roll=orientation.secondAngle;
        return roll;
    }
}
