package be;

import java.time.LocalDateTime;

    public class Picture {

        private int imageId;
        private byte[] image;
        private LocalDateTime timestamp;
        private String fileName;
        private int orderId;

        public Picture(int imageId, byte[] image, LocalDateTime timestamp, String fileName, int orderId) {
            this.imageId = imageId;
            this.image = image;
            this.timestamp = timestamp;
            this.fileName = fileName;
            this.orderId = orderId;
        }

        public int getImageId() {
            return imageId;
        }

        public byte[] getImage() {
            return image;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getFileName() {
            return fileName;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
    }