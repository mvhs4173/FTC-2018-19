package org.firstinspires.ftc.teamcode;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.Executors;


public class Vision {
    VideoCapture videoStream;//The video stream we will be using to get frames from

    public Vision() {
        videoStream = new VideoCapture();
    }
}
