package ftc.vision;

import android.util.Log;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ElementRecognizer {

    public ElementRecognizer() {

    }



    public Mat yellowCubeFilter(Mat image) {
        Mat cannyImage = new Mat();
        Mat filteredImage = new Mat();
        Mat hsv = new Mat();
        Mat mask = new Mat();

        Mat grayImage = new Mat();
        Mat blurredImage = new Mat();
        Mat threshedImage = new Mat();
        Mat outImage = new Mat();

        try {

            List<MatOfPoint> contours = new ArrayList<>();
            Mat archy = new Mat();

            Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);//Convert the image to the HSV color space

            double H = Math.floor(FtcRobotControllerActivity.seekBar1.getProgress()*2.55);
            double S = Math.floor(FtcRobotControllerActivity.seekBar2.getProgress()*2.55);
            double V = Math.floor(FtcRobotControllerActivity.seekBar3.getProgress()*2.55);

            //Color values to filter for in HSV format
            Scalar maxRange = new Scalar(109, 255, 255);
            Scalar lowestRange = new Scalar(73, 170, 158);

            Core.inRange(hsv, lowestRange, maxRange, mask);//Get only the pixels in the correct color range

            Core.bitwise_and(image, image, filteredImage, mask);//Mask the image so opencv only sees the parts of the image in the right color range

            Imgproc.cvtColor(filteredImage, grayImage, Imgproc.COLOR_BGR2GRAY);//Convert the image to grayscale

            Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 2);//Blur the image

            Imgproc.Canny(blurredImage, cannyImage, 239, 71);//Edge detection

            Imgproc.findContours(cannyImage, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            //Check all the contours and get the contour with largest area
            Iterator<MatOfPoint> contourIterator = contours.iterator();
            double largestArea = 0.0;
            List<MatOfPoint> boxPoints = new ArrayList<>();
            int index = 0;
            int largestIndex = 0;

            while (contourIterator.hasNext()) {
                MatOfPoint currentContour = (MatOfPoint)contourIterator.next();

                double area = Imgproc.contourArea(currentContour);

                //Aproximate the contours
                double epsilon = 0.03*Imgproc.arcLength(new MatOfPoint2f(currentContour.toArray()), true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(currentContour.toArray()), approx, epsilon, true);


                int numVerts = approx.toArray().length;
                //FtcRobotControllerActivity.resultText.setText(String.valueOf(numVerts));
                //Make sure its in a basic square/cube shape
                if (numVerts >= 4 && area >= 30) {
                    //Get a bounding box
                    RotatedRect boundingBox = Imgproc.minAreaRect(approx);

                    Point[] vertices = new Point[4];
                    boundingBox.points(vertices);//Get vertices
                    MatOfPoint verts = new MatOfPoint(vertices);
                    Rect bBox = Imgproc.boundingRect(verts);
                    int ratio = bBox.width/bBox.height;


                    if (area > largestArea) {
                        largestIndex = index;
                        largestArea = area;
                        boxPoints.add(new MatOfPoint(vertices));
                        index++;
                    }
                }
            }

            //If there is anything detected
            if (largestIndex > 0) {
                Rect boundingBox = Imgproc.boundingRect(boxPoints.get(largestIndex));
                Imgproc.rectangle(image, new Point(boundingBox.x, boundingBox.y), new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height), new Scalar(0, 255, 0));

                //The position of the cube in the image
                Point cubePosition = new Point(boundingBox.x + (boundingBox.width/2), boundingBox.y + (boundingBox.height/2));
            }
            //Imgproc.drawContours(image, contours, largestIndex, new Scalar(0, 255, 0), 3);
            //Rect boundingBox = Imgproc.boundingRect((MatOfPoint)contour);
            //Imgproc.rectangle(image, new Point(boundingBox.x, boundingBox.y), new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height), new Scalar(0, 255, 0));

        }catch (Throwable e) {
                Log.d("OpenCv Code Error", e.toString());
        }

        if (FtcRobotControllerActivity.viewType == 1) {
            outImage = image;
        }else if (FtcRobotControllerActivity.viewType == 2) {
            outImage = filteredImage;
        }else if (FtcRobotControllerActivity.viewType == 3) {
            outImage = cannyImage;
        }

        return outImage;
    }
}
