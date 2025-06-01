package be;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

    class QRCodeInfoTest {

        private QRCodeInfo qrCodeInfo;
        private byte[] image;
        private String qrString;
        private int userId;

        @BeforeEach
        void setUp() {
            image = new byte[]{10, 20, 30};
            qrString = "QR-ABC-123";
            userId = 42;
            qrCodeInfo = new QRCodeInfo(image, qrString, userId);
        }

        @Test
        void testConstructorAndGetters() {
            assertTrue(Arrays.equals(image, qrCodeInfo.getImage()));
            assertEquals(qrString, qrCodeInfo.getQrCodeString());
            assertEquals(userId, qrCodeInfo.getUserId());
        }

        @Test
        void testSetters() {
            byte[] newImage = new byte[]{5, 6, 7};
            String newQrString = "NEW-QR-456";
            int newUserId = 99;

            qrCodeInfo.setImage(newImage);
            qrCodeInfo.setQrCodeString(newQrString);
            qrCodeInfo.setUserId(newUserId);

            assertTrue(Arrays.equals(newImage, qrCodeInfo.getImage()));
            assertEquals(newQrString, qrCodeInfo.getQrCodeString());
            assertEquals(newUserId, qrCodeInfo.getUserId());
        }
    }