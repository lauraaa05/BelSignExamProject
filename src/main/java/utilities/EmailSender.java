package utilities;

public class EmailSender {
    public static boolean sendWithAttachment(String recipient, String subject, String body, String filePath) {
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
