package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;

@SuppressWarnings("unused")
public class ColourSensor {
    private ColorSensor colorSensor;


    public ColourSensor (ColorSensor sensor) {
        this.colorSensor = sensor;
    }

    double[] colors(){
        double green = colorSensor.green();
        double red = colorSensor.red();
        double blue = colorSensor.blue();

        return new double[]{
                blue,green,red
        };
    }
}
