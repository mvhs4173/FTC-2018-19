package org.firstinspires.ftc.teamcode;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;


public class Vision {
    CameraBridgeViewBase viewBase;

    public Vision() {

        viewBase = (JavaCamera2View) FtcRobotControllerActivity.cameraViewBase;
    }
}
