package ftc.vision;

import android.util.Log;
import android.view.SurfaceView;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
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
        //Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2BGR);
        try {
            if (FtcRobotControllerActivity.hueSeekBar != null && FtcRobotControllerActivity.resultText != null) {
                int sliderValue = FtcRobotControllerActivity.hueSeekBar.getProgress();
                FtcRobotControllerActivity.resultText.setText(String.valueOf(sliderValue));
            }
        }catch (Throwable e) {
            Log.d("VISION ERROR", e.toString());
        }

        return processor.yellowCubeFilter(image);
    }
}
