package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utilities.EmailSender;
import utilities.PDFReportGenerator;

import java.io.File;

public class QCUDoneReportController {

    @FXML
    private TextField emailField;

    @FXML
    private Button sendEmailButton;

    @FXML
    private Button savePdfButton;

    @FXML
    private void handleSendEmail() {
        String email = emailField.getText();

        if (email == null || email.isBlank()) {
            showAlert("Missing Email", "Please enter an email address.");
            return;
        }

        try {
            String content = "Quality Control Report Completed.\nAll items approved.";
            String fileName = "QCU_Report";

            // Correct PDF file name
            File pdf = PDFReportGenerator.generateReport(content, fileName);

            boolean success = EmailSender.sendWithAttachment(
                    email,
                    "Report Complete",
                    "Attached is the Belman Quality Control Report.",
                    pdf
            );

            if (success) {
                showAlert("Success", "Email sent successfully to: " + email);
            } else {
                showAlert("Failure", "Could not send email to: " + email);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Exception occurred while sending email: " + e.getMessage());
        }
    }

    public void handleGoBack(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }

    // Save PDF to file
    public void handleSavePdf(ActionEvent actionEvent) {
        String content = "Quality Control Report Completed.\nAll items approved.";
        String fileName = "QCU_Report";

        try {
            File file = PDFReportGenerator.generateReport(content, fileName);
            showAlert("Success", "PDF saved at: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not save PDF: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
