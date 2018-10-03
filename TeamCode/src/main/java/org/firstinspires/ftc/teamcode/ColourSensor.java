package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColourSensor {
    ColorSensor colorSensor;


    public ColourSensor (ColorSensor sensor) {
        this.colorSensor = sensor;
    }

    double[] colors(){
        double green = colorSensor.green();
        double red = colorSensor.red();
        double blue = colorSensor.blue();
        double[]colours={
                blue,green,red
        };

        return colours;
    }
}
