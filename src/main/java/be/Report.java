package be;

import java.time.LocalDateTime;

public class Report {
    private int userId;
    private String comment;
    private String orderNumber;
    private LocalDateTime date;
    private String orderCode;

    // ✅ Private constructor to force use of Builder
    private Report(Builder builder) {
        this.userId = builder.userId;
        this.comment = builder.comment;
        this.orderNumber = builder.orderNumber;
        this.date = builder.date;
        this.orderCode = builder.orderCode;
    }

    // ✅ Getters
    public int getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getOrderCode() {
        return orderCode;
    }

    // ✅ Static nested Builder class
    public static class Builder {
        private int userId;
        private String comment;
        private String orderNumber;
        private LocalDateTime date;
        private String orderCode;

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder setOrderCode(String orderCode) {
            this.orderCode = orderCode;
            return this;
        }

        public Report build() {
            return new Report(this);
        }
    }
}
