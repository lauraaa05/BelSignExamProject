package gui.controllers.models;

public class Folder {
        private final String OrderNumber;
        private final String month;
        private final String year;

        public Folder(String OrderNumber, String month, String year) {
            this.OrderNumber = OrderNumber;
            this.month = month;
            this.year = year;
        }

        public String getOrderNumber() {
            return OrderNumber;
        }

        public String getMonth() {
            return month;
        }

        public String getYear() {
            return year;
        }

    public String getOrderCode() {
        return "null";
    }
}

