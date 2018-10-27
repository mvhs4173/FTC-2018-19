package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;

@SuppressWarnings("unused")
public class TouchSensor {
    private DigitalChannel touchSensor;

    public TouchSensor(DigitalChannel touchSensor) {
        this.touchSensor = touchSensor;
    }

    public boolean OnOrOff(){
         return touchSensor.getState();
    }
}