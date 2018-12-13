package ftc.vision;

import android.util.Log;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ftc.vision.FrameGrabber.ScreenOrientation;

import static ftc.vision.FrameGrabber.ScreenOrientation.LANDSCAPE;
import static ftc.vision.FrameGrabber.ScreenOrientation.PORTRAIT;


public class ElementRecognizer {

    public ElementRecognizer() {

    }

    Point lastCubePosition = new Point(-1, -1);
    double lastCubeArea = 0.0;
    MatOfPoint lastContour = null;
    double lastCubeWidth = 0.0;
    double lastCubeHeight = 0.0;
    public Point currentCubeVelocity = new Point(0.0, 0.0);//How fast the cube is moving through the screen
    Point lastFrameCubeVelocity = new Point(0.0, 0.0);//The calculated velocity from the last 2 frames

    public ObjectDetectionResult yellowCubeFilter(Mat image, ScreenOrientation orientation, Point screenDimensions) {
        Mat cannyImage = new Mat();
        Mat filteredImage = new Mat();
        Mat hsv = new Mat();
        Mat mask = new Mat();

        Mat grayImage = new Mat();
        Mat blurredImage = new Mat();
        Mat outImage = new Mat();

        double XcushionPixels = -2.0;//An allowance of how many pixels over it can be
        double YcushionPixels = 2.0;

        Point cubePosition = new Point(-1, -1);


        Size cubeSize = new Size(0, 0);

        try {

            List<MatOfPoint> contours = new ArrayList<>();
            Mat archy = new Mat();

            Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);//Convert the image to the HSV color space

            double H = Math.floor(FtcRobotControllerActivity.seekBar1.getProgress()*2.55);
            double S = Math.floor(FtcRobotControllerActivity.seekBar2.getProgress()*2.55);
            double V = Math.floor(FtcRobotControllerActivity.seekBar3.getProgress()*2.55);

            //Color values to filter for in HSV format
            Scalar maxRange = new Scalar(109, 255, 255);
            Scalar lowestRange = new Scalar(72, 160, 145);

            Core.inRange(hsv, lowestRange, maxRange, mask);//Get only the pixels in the correct color range

            Core.bitwise_and(image, image, filteredImage, mask);//Mask the image so opencv only sees the parts of the image in the right color range

            Imgproc.cvtColor(filteredImage, grayImage, Imgproc.COLOR_BGR2GRAY);//Convert the image to grayscale

            Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 2);//Blur the image

            Imgproc.Canny(blurredImage, cannyImage, 239, 71);//Edge detection

            Imgproc.findContours(cannyImage, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            //Check all the contours and get the contour with largest area
            Iterator<MatOfPoint> contourIterator = contours.iterator();
            double largestArea = 0.0;
            List<MatOfPoint> targetContours = new ArrayList<>();
            int index = 0;
            int largestIndex = 0;

            while (contourIterator.hasNext()) {
                MatOfPoint currentContour = (MatOfPoint)contourIterator.next();

                double area = Imgproc.contourArea(currentContour);

                //Make sure its in a basic square/cube shape
                if (area >= 5) {
                    //Get a bounding box
                    Rect positionBox = Imgproc.boundingRect(currentContour);
                    Point boxPosition = new Point(positionBox.x, positionBox.y);

                    //Swap coordinates according to orientation
                    if (orientation == PORTRAIT) {
                        boxPosition = new Point(positionBox.y, positionBox.x);
                    }

                    //Only use object in the bottom half of the screen
                    if (area > largestArea && boxPosition.y >= screenDimensions.y/1.5) {

                        largestIndex = index;
                        largestArea = area;
                        targetContours.add(currentContour);
                        index++;
                    }
                }
            }

            //If there is anything detected
            if (targetContours.size() > 0) {
                Rect boundingBox = Imgproc.boundingRect(targetContours.get(largestIndex));

                cubeSize.height = boundingBox.height;
                cubeSize.width = boundingBox.width;

                cubePosition = new Point(boundingBox.x + (boundingBox.width/2), boundingBox.y + (boundingBox.height/2));

                //This chunk estimates the velocity that the cube is moving through the screen at
                if (lastCubePosition.x != -1) {
                    //The cube's velocity from the last frame to this one
                    Point immediateVelocity = new Point(cubePosition.x - lastCubePosition.x, cubePosition.y - lastCubePosition.y);

                    //Average the velocity of the cube based on the last two samples we took
                    double x = (immediateVelocity.x + lastFrameCubeVelocity.x)/2;
                    double y = (immediateVelocity.y + lastFrameCubeVelocity.y)/2;

                    //Find the bounds of the current box
                    Point topLeft = new Point(boundingBox.x, boundingBox.y);
                    Point bottomRight = new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height);

                    //Find the bounds of the last box
                    Point lastTopLeft = new Point(lastCubePosition.x - lastCubeWidth/2, lastCubePosition.y - lastCubeHeight/2);
                    Point lastBottomRight = new Point(lastCubePosition.x + lastCubeWidth/2, lastCubePosition.y + lastCubeHeight/2);

                    //If the current box does not break outside the previous box then there is no change in position
                    if (topLeft.x > lastTopLeft.x - XcushionPixels || bottomRight.x < lastBottomRight.x + XcushionPixels) {
                        x = 0.0;
                    }

                    if (topLeft.y > lastTopLeft.y - YcushionPixels || bottomRight.x < lastBottomRight.x + YcushionPixels) {
                        y = 0.0;
                    }

                    currentCubeVelocity = new Point(x, y);
                    lastFrameCubeVelocity = immediateVelocity;
                }

                //Check if the new cube position is within the position of the last one
                if (lastCubePosition.x != -1) {
                    //If the cube is within the area of the last cube on the X axis
                    if (Math.abs(cubePosition.x - lastCubePosition.x) < lastCubeWidth/2) {
                        //If the cube is within the area of the last cube on the Y axis
                        if (Math.abs(cubePosition.y - lastCubePosition.y) < lastCubeHeight/2) {
                            //Now if the area of the last cube is larger than the current one then use the last cube
                            if (lastCubeHeight >= cubeSize.height) {
                                //Use the last cube position and box
                                cubeSize.height = lastCubeHeight;
                                cubeSize.width = lastCubeWidth;
                                boundingBox = Imgproc.boundingRect(lastContour);
                                cubePosition = lastCubePosition;
                            }
                        }
                    }
                }

                Imgproc.rectangle(image, new Point(boundingBox.x, boundingBox.y), new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height), new Scalar(0, 255, 0));

                //The position of the cube in the image
                lastCubePosition = cubePosition;
                lastCubeArea = cubeSize.height * cubeSize.width;
                lastCubeWidth = cubeSize.width;
                lastCubeHeight = cubeSize.height;
                lastContour = targetContours.get(largestIndex);

            }else { //If we didnt detect any cube in this frame then estimate where the cube is
                if (currentCubeVelocity.x == 0.0 && currentCubeVelocity.y == 0.0) {
                    //Esimate the x and y coordinates
                    double x = lastCubePosition.x;
                    double y = lastCubePosition.y;

                    //If the cube goes above the point where we dont recognize cubes then just say we dont see it anymore
                    if (y < screenDimensions.y / 2) {
                        x = -1.0;
                        y = -1.0;
                    }

                    cubePosition = new Point(x, y);//Create the point

                    //Now use the last contour to create a bounding box for the estimated position

                    //Find the bounds of the box
                    Point topLeftCorner = new Point(cubePosition.x - lastCubeWidth / 2, cubePosition.y - lastCubeHeight / 2);
                    Point bottomRightCorner = new Point(cubePosition.x + lastCubeWidth / 2, cubePosition.y + lastCubeHeight / 2);
                    Imgproc.rectangle(image, topLeftCorner, bottomRightCorner, new Scalar(0, 255, 0));//Draw rectangle on screen

                    lastCubePosition = cubePosition;//Set the last cube position for the next loop

                    cubeSize.width = lastCubeWidth;
                    cubeSize.height = lastCubeHeight;
                }
            }
            //Imgproc.drawContours(image, contours, largestIndex, new Scalar(0, 255, 0), 3);
            //Rect boundingBox = Imgproc.boundingRect((MatOfPoint)contour);
            //Imgproc.rectangle(image, new Point(boundingBox.x, boundingBox.y), new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height), new Scalar(0, 255, 0));

        }catch (Throwable e) {
                Log.d("OpenCv Code Error", e.toString());
        }

        //Controls what filter to display on the screen
        if (FtcRobotControllerActivity.viewType == 1) {
            outImage = image;
        }else if (FtcRobotControllerActivity.viewType == 2) {
            outImage = filteredImage;
        }else if (FtcRobotControllerActivity.viewType == 3) {
            outImage = cannyImage;
        }

        //Set up the result
        ObjectDetectionResult result = new ObjectDetectionResult(outImage, cubePosition, cubeSize);
        return result;
    }
}
