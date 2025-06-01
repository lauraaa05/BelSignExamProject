package be;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

    class PictureTest {

        private Picture picture;
        private byte[] sampleImage;
        private LocalDateTime timestamp;

        @BeforeEach
        void setUp() {
            sampleImage = new byte[]{1, 2, 3, 4};
            timestamp = LocalDateTime.now();
            picture = new Picture(1, sampleImage, timestamp, "Left", "ORD001");
        }

        @Test
        void testConstructorAndGetters() {
            assertEquals(1, picture.getImageId());
            assertTrue(Arrays.equals(sampleImage, picture.getImage()));
            assertEquals(timestamp, picture.getTimestamp());
            assertEquals("Left", picture.getSide());
            assertEquals("ORD001", picture.getOrderNumber());
            assertNull(picture.getFileName()); // not set in this constructor
        }

        @Test
        void testConstructorWithFilename() {
            Picture pic = new Picture(sampleImage, "photo.jpg", timestamp, "ORD002", "Right");
            assertTrue(Arrays.equals(sampleImage, pic.getImage()));
            assertEquals("photo.jpg", pic.getFileName());
            assertEquals("ORD002", pic.getOrderNumber());
            assertEquals("Right", pic.getSide());
            assertEquals(timestamp, pic.getTimestamp());
        }

        @Test
        void testConstructorWithoutId() {
            Picture pic = new Picture(sampleImage, timestamp, "Back");
            assertTrue(Arrays.equals(sampleImage, pic.getImage()));
            assertEquals("", pic.getFileName());
            assertEquals("", pic.getOrderNumber());
            assertEquals("Back", pic.getSide());
            assertEquals(timestamp, pic.getTimestamp());
        }

        @Test
        void testSetters() {
            byte[] newImage = new byte[]{9, 8, 7};
            LocalDateTime newTime = timestamp.plusHours(1);

            picture.setImageId(99);
            picture.setImage(newImage);
            picture.setTimestamp(newTime);
            picture.setSide("Top");
            picture.setFileName("new.png");
            picture.setOrderNumber("ORD999");

            assertEquals(99, picture.getImageId());
            assertTrue(Arrays.equals(newImage, picture.getImage()));
            assertEquals(newTime, picture.getTimestamp());
            assertEquals("Top", picture.getSide());
            assertEquals("new.png", picture.getFileName());
            assertEquals("ORD999", picture.getOrderNumber());
        }
    }