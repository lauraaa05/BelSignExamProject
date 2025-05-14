package be;

import java.time.LocalDateTime;

public class Report {

    private int userId;
    private String comment;
    private String orderNumber;
    private LocalDateTime date;
    private String orderCode;

    public Report(int userId, String comment, String orderNumber, LocalDateTime date, String orderCode) {
        this.userId = userId;
        this.comment = comment;
        this.orderNumber = orderNumber;
        this.date = date;
        this.orderCode = orderCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
