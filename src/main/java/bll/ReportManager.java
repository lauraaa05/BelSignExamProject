package bll;

import java.io.IOException;
import utilities.SendGridEmailSender;

public class ReportManager {

    private utilities.SendGridEmailSender emailSender;

    // Constructor to initialize the SendGridEmailSender
    public ReportManager() {
        this.emailSender = new utilities.SendGridEmailSender();
    }

    public void approveReport(String customerEmail) {
        // Business logic for approving the report (e.g., update status in DB)

        try {
            // Use the instance variable emailSender to call sendEmail
            emailSender.sendEmail(
                    customerEmail,
                    "Your Report is Approved",
                    "Dear Customer,\n\nYour report has been approved and is ready for review."
            );
        } catch (IOException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
