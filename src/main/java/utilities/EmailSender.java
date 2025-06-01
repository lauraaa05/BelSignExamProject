package utilities;

public class EmailSender {

    public boolean sendEmail(String email) {

        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

        if (!email.matches(emailRegex)) {
            return false; // Invalid email
        }
        // Simulate email delay
        try {
            Thread.sleep(1500); // 1.5 second delay to simulate sending
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate success
        System.out.println("Email sent successfully");
        return true;
    }
}

