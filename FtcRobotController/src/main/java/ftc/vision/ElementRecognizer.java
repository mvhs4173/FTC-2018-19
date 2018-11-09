package ftc.vision;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
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
        try {

            Mat hsv = new Mat();
            Mat mask = new Mat();
            Mat filteredImage = new Mat();
            Mat grayImage = new Mat();
            Mat blurredImage = new Mat();

            List<MatOfPoint> contours = new ArrayList<>();
            Mat archy = new Mat();

            Mat outImage = new Mat();

            Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);//Convert the image to the HSV color space

            Scalar maxRange = new Scalar(100, 255, 255);
            Scalar lowestRange = new Scalar(80, 100, 100);

            Core.inRange(hsv, lowestRange, maxRange, mask);

            Core.bitwise_and(image, image, filteredImage, mask);

            Imgproc.cvtColor(filteredImage, grayImage, Imgproc.COLOR_BGR2GRAY);

            Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);

            Imgproc.Canny(blurredImage, cannyImage, 10, 100);


            Imgproc.findContours(cannyImage, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            //Check all the contours and get the contour with largest area
            Iterator<MatOfPoint> contourIterator = contours.iterator();
            double largestArea = 0.0;
            Mat contour = null;
            int largestIndex = 0;
            int contourIndex = 0;

            while (contourIterator.hasNext()) {
                Mat currentContour = contourIterator.next();

                double area = Imgproc.contourArea(currentContour);

                if (area > largestArea) {
                    largestArea = area;
                    contour = currentContour;
                    largestIndex = contourIndex;
                }

                contourIndex++;
            }
            contourIndex--;

            Imgproc.drawContours(image, contours, largestIndex, new Scalar(0, 255, 0), 5);

            if (contour != null) {
                Moments moments = Imgproc.moments(contour, false);

                Scalar circleColor = new Scalar(255, 0, 0);

                Point center = new Point((int) (moments.m10 / moments.m00), (int) (moments.m01 / moments.m00));
            }
        }catch (Throwable e) {
                Log.d("OpenCv Code Error", e.toString());
        }
        return image;
    }
}
