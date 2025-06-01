package be;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    private Report report;
    private int userId;
    private String comment;
    private String orderNumber;
    private LocalDateTime date;
    private String orderCode;

    @BeforeEach
    void setUp() {
        userId = 1;
        comment = "Missing parts";
        orderNumber = "ORD-001";
        date = LocalDateTime.now();
        orderCode = "OC-456";

        report = new Report(userId, comment, orderNumber, date, orderCode);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(userId, report.getUserId());
        assertEquals(comment, report.getComment());
        assertEquals(orderNumber, report.getOrderNumber());
        assertEquals(date, report.getDate());
        assertEquals(orderCode, report.getOrderCode());
    }

    @Test
    void testSetters() {
        int newUserId = 2;
        String newComment = "Wrong color";
        String newOrderNumber = "ORD-002";
        LocalDateTime newDate = LocalDateTime.of(2023, 1, 1, 12, 0);
        String newOrderCode = "OC-789";

        report.setUserId(newUserId);
        report.setComment(newComment);
        report.setOrderNumber(newOrderNumber);
        report.setDate(newDate);
        report.setOrderCode(newOrderCode);

        assertEquals(newUserId, report.getUserId());
        assertEquals(newComment, report.getComment());
        assertEquals(newOrderNumber, report.getOrderNumber());
        assertEquals(newDate, report.getDate());
        assertEquals(newOrderCode, report.getOrderCode());
    }
}