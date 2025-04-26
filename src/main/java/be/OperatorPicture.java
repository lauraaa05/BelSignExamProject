package be;

import java.time.LocalDateTime;

    public class OperatorPicture {
        private int operatorId;
        private String filePath;   //TO DO <- store the location of the picture
        private LocalDateTime timestamp;

        public OperatorPicture(int operatorId, String filePath, LocalDateTime timestamp) {
            this.operatorId = operatorId;
            this.filePath = filePath;
            this.timestamp = timestamp;
        }

        public int getOperatorId() {
            return operatorId;
        }

        public String getFilePath() {
            return filePath;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

