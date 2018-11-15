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
        try {

            Mat hsv = new Mat();
            Mat mask = new Mat();

            Mat grayImage = new Mat();
            Mat blurredImage = new Mat();

            List<MatOfPoint> contours = new ArrayList<>();
            Mat archy = new Mat();

            Mat outImage = new Mat();

            Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2Lab);//Convert the image to the HSV color space

            double L = Math.floor(FtcRobotControllerActivity.seekBar1.getProgress()*2.55);
            double A = Math.floor(FtcRobotControllerActivity.seekBar2.getProgress()*2.55);
            double B = Math.floor(FtcRobotControllerActivity.seekBar3.getProgress()*2.55);

            Scalar maxRange = new Scalar(L, A, B);
            Scalar lowestRange = new Scalar(30, 30, 30);

            Core.inRange(hsv, lowestRange, maxRange, mask);

            Core.bitwise_and(image, image, filteredImage, mask);

            Imgproc.cvtColor(filteredImage, grayImage, Imgproc.COLOR_BGR2GRAY);

            Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);

            Imgproc.Canny(blurredImage, cannyImage, 50, 100);


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
                double epsilon = 0.05*Imgproc.arcLength(new MatOfPoint2f(currentContour.toArray()), true);
                MatOfPoint2f approx = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(currentContour.toArray()), approx, epsilon, true);


                int numVerts = approx.toArray().length;
                //FtcRobotControllerActivity.resultText.setText(String.valueOf(numVerts));
                //Make sure its in a basic square/cube shape
                if (numVerts >= 4 && area >= 20) {
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

            Rect boundingBox = Imgproc.boundingRect(boxPoints.get(largestIndex));
            Imgproc.rectangle(image, new Point(boundingBox.x, boundingBox.y), new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height), new Scalar(0, 255, 0));
            //Imgproc.drawContours(image, contours, largestIndex, new Scalar(0, 255, 0), 3);
            //Rect boundingBox = Imgproc.boundingRect((MatOfPoint)contour);
            //Imgproc.rectangle(image, new Point(boundingBox.x, boundingBox.y), new Point(boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height), new Scalar(0, 255, 0));

        }catch (Throwable e) {
                Log.d("OpenCv Code Error", e.toString());
        }
        return image;
    }
}
