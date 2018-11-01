package org.firstinspires.ftc.teamcode;

/**
 * Created by ROBOT18 on 10/26/2017.
 */

@SuppressWarnings("unused")
public class Degrees {

    static double cos(double x){
        return Math.cos(degreesToRadians(x));
    }

    public static double acos(double x){
        return radiansToDegrees(Math.acos(x));
    }

    private static double degreesToRadians(double deg){
        return deg*(Math.PI/180);
    }

    private static double radiansToDegrees(double rad){
        return rad*(180/Math.PI);
    }

    public static double sin(double x) {
        return Math.sin(degreesToRadians(x));
    }

    public static double asin(double x) {
        return radiansToDegrees(Math.asin(x));
    }

    public static double tan(double x) {
        return Math.tan(degreesToRadians(x));
    }

    public static double atan2(double y, double x){
        return radiansToDegrees(Math.atan2(y, x));
    }

    public static double subtract(double x, double y){
        double d = x - y;
        if (d > 180){
            d = d - 360;
        }
        if (d < -180){
            d = d + 360;
        }
        return d;
    }
}
