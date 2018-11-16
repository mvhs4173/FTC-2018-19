package ftc.vision;

import android.util.Log;
import android.view.SurfaceView;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32FC3;

/**
 * Created by vandejd1 on 8/30/16.
 * FTC Team EV 7393
 */
public class FrameGrabber implements CameraBridgeViewBase.CvCameraViewListener {

    ElementRecognizer processor;

    public FrameGrabber(CameraBridgeViewBase cameraBridgeViewBase, int frameWidth, int frameHeight) {

        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setMinimumWidth(frameWidth);
        cameraBridgeViewBase.setMinimumHeight(frameHeight);
        cameraBridgeViewBase.setMaxFrameSize(frameWidth, frameHeight);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        processor = new ElementRecognizer();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        Mat image = inputFrame;

        ObjectDetectionResult cubeResult = processor.yellowCubeFilter(image);
        Point cubePosition = cubeResult.getObjectPosition();

        try {
            if (FtcRobotControllerActivity.seekBar1 != null && FtcRobotControllerActivity.resultText != null) {
                double L = Math.floor(FtcRobotControllerActivity.seekBar1.getProgress()*2.55);
                double A = Math.floor(FtcRobotControllerActivity.seekBar2.getProgress()*2.55);
                double B = Math.floor(FtcRobotControllerActivity.seekBar3.getProgress()*2.55);
                //FtcRobotControllerActivity.resultText.setText(String.valueOf(L) + ", " + String.valueOf(A) + ", " + String.valueOf(B));
                FtcRobotControllerActivity.resultText.setText(String.valueOf(cubePosition.x) + ", " + String.valueOf(cubePosition.y));
            }
        }catch (Throwable e) {
            Log.d("VISION ERROR", e.toString());
        }





        return cubeResult.getImage();
    }
}
