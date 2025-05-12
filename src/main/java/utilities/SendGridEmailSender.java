package utilities;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

    public class SendGridEmailSender {

        private static final String API_KEY = "YOUR_SENDGRID_API_KEY";
        private static final SendGrid sendGrid = new SendGrid(API_KEY);

        public static void sendEmail(String toEmail, String subject, String content) throws IOException {
            Email from = new Email("your-email@example.com");  // Your SendGrid email
            Email to = new Email(toEmail);
            Content emailContent = new Content("text/plain", content);
            Mail mail = new Mail(from, subject, to, emailContent);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sendGrid.api(request);
        }
    }
