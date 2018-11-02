package ftc.vision;

import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32FC3;

/**
 * Created by vandejd1 on 8/30/16.
 * FTC Team EV 7393
 */
public class FrameGrabber implements CameraBridgeViewBase.CvCameraViewListener2 {

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
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat image = inputFrame.rgba();
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2BGR);

        return processor.yellowCubeFilter(image);
    }
}
