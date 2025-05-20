package be;

import java.time.LocalDateTime;

public class OrderGroup {
    private int id;
    private LocalDateTime createdAt;
    private String customerName;

    public OrderGroup(int orderNumber, LocalDateTime createdAt, String customerName) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerName = customerName;
    }

    public OrderGroup(String customerName) {
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}