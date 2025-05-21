package be;

public class Order {
    private int countryNumber;
    private int year;
    private String month;
    private String orderCode;
    private int orderGroupId;

    public Order(int countryNumber, int year, String month, String orderCode, int orderGroupId) {
        this.countryNumber = countryNumber;
        this.year = year;
        this.month = month;
        this.orderCode = orderCode;
        this.orderGroupId = orderGroupId;
    }

    public String getFormattedOrderText() {
        return countryNumber + "-" +  year + "-" + month + "-" + orderCode;
    }

    @Override
    public String toString() {
        return getFormattedOrderText();
    }

    public int getCountryNumber() {
        return countryNumber;
    }

    public int getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public int getOrderGroupId() {
        return orderGroupId;
    }

    public void setCountryNumber(int countryNumber) {
        this.countryNumber = countryNumber;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderGroupId(int orderGroupId) {
        this.orderGroupId = orderGroupId;
    }
}