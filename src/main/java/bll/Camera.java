package bll;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import java.awt.image.BufferedImage;

public class Camera {

    private Webcam webcam;

    public void initializeCamera() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();
    }

    public BufferedImage takePicture() {
        if (webcam != null && webcam.isOpen()) {
            return webcam.getImage();
        }
        return null;
    }

    public void closeCamera() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }
}