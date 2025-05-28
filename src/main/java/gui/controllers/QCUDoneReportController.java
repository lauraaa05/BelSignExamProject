package gui.controllers;

import be.Order;
import be.Picture;
import be.User;
import dal.PictureDAO;
import gui.model.ReportModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import utilities.EmailSender;
import utilities.LoggedInUser;
import utilities.PDFReportGenerator;
import utilities.SceneNavigator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.awt.Desktop;
import java.io.FileOutputStream;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.imageio.ImageIO;

public class QCUDoneReportController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField emailField;

    @FXML
    private AnchorPane photoSectionPane;

    @FXML
    private TextArea commentsTextArea;

    @FXML
    private Label emailText;

    @FXML
    private Button sendEmailButton;

    @FXML
    private Button viewPdfButton;

    @FXML
    private Label generalCommentsLabel;

    @FXML
    private TilePane photoTile;

    @FXML
    private Label orderNumberLabel;

    private final PictureDAO pictureDAO = new PictureDAO();

    private final ReportModel reportModel = new ReportModel();

    private Order currentOrder;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @FXML
    public void initialize() {
        User loggedIn = LoggedInUser.getUser();

        if (loggedIn != null && "admin".equalsIgnoreCase(loggedIn.getRole())) {
            sendEmailButton.setVisible(false);
            sendEmailButton.setManaged(false);
            viewPdfButton.setVisible(false);
            viewPdfButton.setManaged(false);
            emailField.setVisible(false);
            emailField.setManaged(false);
            emailText.setVisible(false);
            emailText.setManaged(false);
        }
    }

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
        SceneNavigator sceneNavigator = new SceneNavigator();

        User loggedInUser = LoggedInUser.getUser();

        if (loggedInUser.getRole().equalsIgnoreCase("Admin")) {
            sceneNavigator.switchTo(actionEvent, "AdminReport.fxml");
        } else if (loggedInUser.getRole().equalsIgnoreCase("Quality Control")) {
            sceneNavigator.switchTo(actionEvent, "QCUFolderScreen.fxml");
        } else {
            System.out.println("Unknown role: " + loggedInUser.getRole());
        }
    }

    // Save PDF to file
    public void handleSavePdf(ActionEvent actionEvent) {
        String content = "Quality Control Report Completed.\nAll items approved.";
        String fileName = "QCU_Report";

        try {
            // Generate PDF as bytes
            byte[] pdfBytes = PDFReportGenerator.generateReportAsBytes(content, fileName);

            // Save to DB
            reportModel.savePdfToDatabase(currentOrder.getOrderCode(), pdfBytes);

            showAlert("Success", "PDF saved to database for order: " + currentOrder.getOrderCode());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not save PDF to database: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        orderNumberLabel.setText("ORDER NUMBER: " + order);

        loadPictures(order.toString());
        loadLatestComment(order.getOrderCode());
    }

    private void loadPictures(String orderNumber) {
        try {
            List<Picture> pictures = pictureDAO.getPicturesByOrderNumberRaw(orderNumber);
            photoTile.getChildren().clear();

            for (Picture picture : pictures) {
                VBox imageCard = createImageCard(picture);
                photoTile.getChildren().add(imageCard);
            }

            if (pictures.isEmpty()) {
                System.out.println("No pictures found for order number: " + orderNumber);
            }
        } catch (SQLException e) {
            System.err.println("Database error while loading pictures: " + e.getMessage());
        }
    }

    private void loadLatestComment(String orderNumber) {
        try {
            String latestComment = reportModel.getLatestCommentByOrderNumber(orderNumber);
            generalCommentsLabel.setText(latestComment != null ? latestComment : "No comments yet.");
        } catch (Exception e) {
            generalCommentsLabel.setText("Failed to fetch comment: " + e.getMessage());
        }
    }

    private VBox createImageCard(Picture picture) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #fff; -fx-padding: 10px; -fx-border-color: #d1d5db; -fx-border-radius: 5px; -fx-background-radius: 8px;");
        vBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(520);
        imageView.setFitWidth(420);
        imageView.setPreserveRatio(false);
        imageView.setImage(new Image(new ByteArrayInputStream(picture.getImage())));

        String sideText = (picture.getSide() == null || picture.getSide().isEmpty()) ? "Unknown" : picture.getSide();
        Label sideLabel = new Label("Side: " + sideText);
        Label dateLabel = new Label("Date: " + picture.getTimestamp().format(formatter));

        vBox.getChildren().addAll(imageView, sideLabel, dateLabel);
        return vBox;
    }

    @FXML
    private void handleViewPdf(ActionEvent actionEvent) {
        try {
            // Capture the node you want as image (e.g., ScrollPane)
            WritableImage snapshot = scrollPane.snapshot(new SnapshotParameters(), null);

            // Convert WritableImage to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            // Save the image to a temporary file (optional)
            File imageFile = File.createTempFile("report_image_", ".png");
            ImageIO.write(bufferedImage, "png", imageFile);

            // Create a PDF file
            File pdfFile = File.createTempFile("report_", ".pdf");
            pdfFile.deleteOnExit();

            // Use iText to write image to PDF
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                PdfWriter writer = new PdfWriter(fos);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                // Load image
                com.itextpdf.layout.element.Image pdfImage =
                        new com.itextpdf.layout.element.Image(com.itextpdf.io.image.ImageDataFactory.create(imageFile.getAbsolutePath()));

                pdfImage.scaleToFit(500, 700);  // Resize as needed
                document.add(pdfImage);
                document.close();
            }

            // Open the PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                showAlert("Unsupported", "Desktop API is not supported on this system.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF: " + e.getMessage());
        }
    }
}
