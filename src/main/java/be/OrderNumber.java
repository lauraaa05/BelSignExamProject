package be;

public class OrderNumber {
    private int countryNumber;
    private int year;
    private String month;
    private String orderCode;

    public OrderNumber(int countryNumber, int year, String month, String orderCode) {
        this.countryNumber = countryNumber;
        this.year = year;
        this.month = month;
        this.orderCode = orderCode;
    }

    public OrderNumber(int orderNumber, String status, String date) {
    }

    // Getters
    public int getCountryNumber() { return countryNumber; }
    public int getYear() { return year; }
    public String getMonth() { return month; }
    public String getOrderCode() { return orderCode; }
}


