package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;

public class TouchSensor {
    DigitalChannel touchSensor;

    public TouchSensor(DigitalChannel touchSensor) {
        this.touchSensor = touchSensor;
    }

    public boolean OnOrOff(){
         return touchSensor.getState();
    }
}