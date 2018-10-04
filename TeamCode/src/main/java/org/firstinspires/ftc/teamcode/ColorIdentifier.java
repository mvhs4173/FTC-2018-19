package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorIdentifier {
    private ColorSensor colorSensor;

    public ColorIdentifier(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    public int[] getRGB() {
        int redValue = colorSensor.red();
        int greenValue = colorSensor.green();
        int blueValue = colorSensor.blue();

        int[] RGB = {redValue, greenValue, blueValue};

        return RGB;
    }

    boolean isRed() {
        return (getRGB()[0]>150);
    }

    boolean isGreen() {
        return (getRGB()[1]>150);
    }

    boolean isBlue() {
        return (getRGB()[2]>150);
    }
}