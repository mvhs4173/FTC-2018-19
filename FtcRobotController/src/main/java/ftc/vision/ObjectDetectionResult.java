package ftc.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class ObjectDetectionResult {

    /**
     * Holds the result of an object detection
     * @param image The image that the object was detected in as Mat format
     * @param objectPositionPixels A Point object that holds the X and Y coordinates of the detected object on the screen
     */
    private Mat image = new Mat();
    private Point objectPosition = new Point(0, 0);

    public ObjectDetectionResult(Mat image, Point objectPositionPixels) {
        this.image = image;
        this.objectPosition = objectPositionPixels;
    }

    /**
     * Gets the image used in processing
     * @return The image in Mat format
     */
    public Mat getImage() {
        return image;
    }

    /**
     * Gets the position of the object, if detected
     * @return A Point object holding the X and Y coordinates of detected object
     */
    public Point getObjectPosition() {
        return objectPosition;
    }
}
