package ftc.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

public class ObjectDetectionResult {

    /**
     * Holds the result of an object detection
     * @param image The image that the object was detected in as Mat format
     * @param objectPositionPixels A Point object that holds the X and Y coordinates of the detected object on the screen
     */
    private Mat image = new Mat();
    private Point objectPosition = new Point(0, 0);
    private Size objectSize = new Size(0, 0);

    public ObjectDetectionResult(Mat image, Point objectPositionPixels, Size objectSize) {
        this.image = image;
        this.objectPosition = objectPositionPixels;
        this.objectSize = objectSize;
    }

    /**
     * Constructs a result with just an image, there is no cube detected in this image
     * @param image The image in Mat format
     */
    public ObjectDetectionResult(Mat image) {
        this.image = image;
        this.objectPosition = new Point(-1, -1);
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
     * @return A Point object holding the X and Y coordinates of detected object if no object is found the coordinates will be (-1, -1)
     */
    public Point getObjectPosition() {
        return objectPosition;
    }

    public Size getObjectSize() {
        return objectSize;
    }
}
