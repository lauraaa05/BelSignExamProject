package bll;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import java.awt.image.BufferedImage;

public class CameraManager {

    private Webcam webcam;

    public void initializeCamera() {
        webcam = Webcam.getDefault();

        if (webcam.isOpen()) {
            webcam.close();
        }
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