package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;

@SuppressWarnings("unused")
public class ColorIdentifier {
    private ColorSensor colorSensor;

    public ColorIdentifier(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    private int[] getRGB() {
        int redValue = colorSensor.red();
        int greenValue = colorSensor.green();
        int blueValue = colorSensor.blue();

        return new int[]{redValue, greenValue, blueValue};
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