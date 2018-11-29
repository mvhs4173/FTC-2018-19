package ftc.vision;

import android.util.Log;
import android.view.SurfaceView;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import static ftc.vision.FrameGrabber.DetectionMode.NONE;
import static ftc.vision.FrameGrabber.ScreenOrientation.LANDSCAPE;
import static ftc.vision.FrameGrabber.ScreenOrientation.PORTRAIT;
import static org.opencv.core.CvType.CV_32FC3;

/**
 * Created by vandejd1 on 8/30/16.
 * FTC Team EV 7393
 */
public class FrameGrabber implements CameraBridgeViewBase.CvCameraViewListener {

    ElementRecognizer processor;
    ObjectDetectionResult currentResult;

    public enum DetectionMode {
        GOLD_CUBE,
        NONE;
    }

    public enum ScreenOrientation {
        PORTRAIT,
        LANDSCAPE;
    }

    private DetectionMode detectionMode = NONE;
    private ScreenOrientation screenOrientation = LANDSCAPE;
    private Point screenDimensions;
    private int absFrameWidth;//Absolute frame width
    private int absFrameHeight;//Absolute frame height

    public double lastWidth = 0.0;
    public double lastHeight = 0.0;

    public FrameGrabber(CameraBridgeViewBase cameraBridgeViewBase, int frameWidth, int frameHeight) {

        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setMinimumWidth(frameWidth);
        cameraBridgeViewBase.setMinimumHeight(frameHeight);
        cameraBridgeViewBase.setMaxFrameSize(frameWidth, frameHeight);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        this.absFrameHeight = frameHeight;
        this.absFrameWidth = frameWidth;
        screenDimensions = new Point(frameWidth, frameHeight);

        processor = new ElementRecognizer();
    }

    public Point getScreenDimensions() {
        return screenDimensions;
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

        //Determine what the camera should be recognizing
        switch (detectionMode) {
            case GOLD_CUBE:
                currentResult = processor.yellowCubeFilter(image, screenOrientation, screenDimensions);
                image = currentResult.getImage();
                break;
            case NONE:
                //Just give the image, no cube coordinates are provided
                currentResult = new ObjectDetectionResult(image);
                break;
        }

        //Swap coordinates based on screen Orientation
        if (screenOrientation == PORTRAIT) {
            Point position = currentResult.getObjectPosition();

            //Swap coordinates
            Point swappedPosition = new Point(position.y, position.x);

            if (swappedPosition.x > -1) {
                swappedPosition.x = screenDimensions.x - swappedPosition.x;//Switched the order so that as the object moves to the right, the x coordinate increase
            }

            Mat rImage = currentResult.getImage();

            currentResult = new ObjectDetectionResult(rImage, swappedPosition, currentResult.getObjectSize());
        }



        try {
            if (FtcRobotControllerActivity.seekBar1 != null && FtcRobotControllerActivity.resultText != null) {
                double L = Math.floor(FtcRobotControllerActivity.seekBar1.getProgress()*2.55);
                double A = Math.floor(FtcRobotControllerActivity.seekBar2.getProgress()*2.55);
                double B = Math.floor(FtcRobotControllerActivity.seekBar3.getProgress()*2.55);
                //FtcRobotControllerActivity.resultText.setText(String.valueOf(L) + ", " + String.valueOf(A) + ", " + String.valueOf(B));


                lastWidth = currentResult.getObjectSize().width;
                lastHeight = currentResult.getObjectSize().height;

                FtcRobotControllerActivity.resultText.setText(String.valueOf(lastWidth) + ", " + String.valueOf(lastHeight));
            }
        }catch (Throwable e) {
            Log.d("VISION ERROR", e.toString());
        }





        return image;
    }

    /**
     * Get the result from the last frame operation
     * @return An ObjectDetectionResult
     */
    public ObjectDetectionResult getLastProcessingResult() {
        return this.currentResult;
    }

    /**
     * Sets what the camera will be trying to recognize
     * @param mode An enumeration indicating what object the camera should be trying to recognize
     */
    public void setDetectionMode(DetectionMode mode) {
        this.detectionMode = mode;
    }

    /**
     * Sets the screen orientation which will ensure the X and Y coordinates are correct
     * @param orientation The orientation, either portrait or landscape
     */
    public void setScreenOrientation(ScreenOrientation orientation) {
        this.screenOrientation = orientation;

        //Swap coordinates accordingly
        if (this.screenOrientation == PORTRAIT) {
            screenDimensions = new Point(absFrameHeight, absFrameWidth);
        }else {
            screenDimensions = new Point(absFrameWidth, absFrameHeight);
        }
    }

    /**
     * Gets the currently set screen orientation
     * @return ScreenOrientation
     */
    public ScreenOrientation getScreenOrientation() {
        return screenOrientation;
    }
}
