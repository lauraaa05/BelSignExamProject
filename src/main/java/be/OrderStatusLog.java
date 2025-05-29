package be;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderStatusLog {

    private String orderCode;
    private int statusId;
    private int userRoleId;
    private LocalDateTime dateChanged;

    public OrderStatusLog(String orderCode, int statusId, int userRoleId, LocalDateTime dateChanged) {
        this.orderCode = orderCode;
        this.statusId = statusId;
        this.userRoleId = userRoleId;
        this.dateChanged = dateChanged;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }
}