package gui.controllers;

import be.Order;
import be.Picture;
import be.QualityControl;
import be.Report;
import dal.OrderStatusDAO;
import dal.PictureDAO;
import exceptions.DALException;
import gui.model.ReportModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

public class QCUReportPDFController {

    @FXML
    private VBox ignoreThisInPDF;

    @FXML
    private Label signatureLabel;

    @FXML
    private AnchorPane photoSectionPane;

    @FXML
    private TextArea commentsTextArea;

    @FXML
    private Button submitButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Label generalCommentsLabel;

    @FXML
    private TilePane photoTile;

    @FXML
    private ScrollPane scrollPane;


    @FXML
    private Label orderNumberLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final PictureDAO pictureDAO = new PictureDAO();

    private final ReportModel reportModel = new ReportModel();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    private Order currentOrder;

    private QualityControl currentUser;

    @FXML
    public void initialize() {

    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        orderNumberLabel.setText("ORDER NUMBER: " + order);

        loadPictures(order.toString());
        loadLatestComment(order.getOrderCode());

        String status = new OrderStatusDAO().getStatusForOrder(order.getOrderCode());

        loadSignatureName(order.getOrderCode());
    }

    private VBox createImageCard(Picture picture) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #fff; -fx-padding: 10px; -fx-border-color: #d1d5db; -fx-border-radius: 5px; -fx-background-radius: 8px;");
        vBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(520);
        imageView.setFitWidth(420);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(new ByteArrayInputStream(picture.getImage())));

        String sideText = (picture.getSide() == null || picture.getSide().isEmpty()) ? "Unknown" : picture.getSide();
        Label sideLabel = new Label("Side: " + sideText);
        Label dateLabel = new Label("Date: " + picture.getTimestamp().format(formatter));

        vBox.getChildren().addAll(imageView, sideLabel, dateLabel);
        return vBox;
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
        } catch (DALException e) {
            System.err.println("Database error while loading pictures: " + e.getMessage());
        }
    }

    private void loadLatestComment(String orderCode) {
        try {
            String latestComment = reportModel.getLatestCommentByOrderNumber(orderCode);
            generalCommentsLabel.setText(latestComment != null ? latestComment : "No comments yet.");
        } catch (Exception e) {
            generalCommentsLabel.setText("Failed to fetch comment: " + e.getMessage());
        }
    }

    private String extractOrderNumber() {
        return orderNumberLabel.getText().replace("ORDER NUMBER: ", "").trim();
    }

    public void setCurrentUser(QualityControl user) {
        this.currentUser = user;
        signatureLabel.setText(user.getFirstName() + " " + user.getLastName());
    }

    private void hideSubmitButton(String orderCode) {
        String status = new OrderStatusDAO().getStatusForOrder(orderCode);
        if ("done".equalsIgnoreCase(status)) {
            submitButton.setVisible(false);
            submitButton.setManaged(false);
            commentsTextArea.setEditable(false);
        }
    }

    @FXML
    private void handleGoBack(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "QCUFolderScreen.fxml");
    }

    @FXML
    private void downloadPdfAct(ActionEvent actionEvent) {
        handleDownloadPDF();
    }

    private void loadSignatureName(String orderCode) {
        try {
            String signatureName = reportModel.getSignatureNameByOrderCode(orderCode);
            signatureLabel.setText(signatureName);
        } catch (SQLException e) {
            signatureLabel.setText("Failed to fetch signature name: " + e.getMessage());
            System.err.println("Error loading signature name: " + e.getMessage());
        }
    }


    private void handleDownloadPDF() {
        try {
            VBox content = (VBox) scrollPane.getContent();

            // Hide content you don’t want in the PDF
            boolean wasVisible = ignoreThisInPDF.isVisible();
            ignoreThisInPDF.setVisible(false);
            ignoreThisInPDF.setManaged(false);

            content.applyCss();
            content.layout();

            // Snapshot the VBox
            int width = (int) content.getBoundsInParent().getWidth();
            int height = (int) content.getBoundsInParent().getHeight();
            WritableImage fxImage = new WritableImage(width, height);
            content.snapshot(new SnapshotParameters(), fxImage);

            // Restore visibility
            ignoreThisInPDF.setVisible(wasVisible);
            ignoreThisInPDF.setManaged(wasVisible);

            // Convert WritableImage to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

            // Use FileChooser to let user select the save location
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("QCU_Report.pdf");

            // Get the current window/stage
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                System.out.println("PDF save cancelled by user.");
                return;
            }

            try (PDDocument document = new PDDocument()) {
                // Scale to fit A4
                float pageWidth = PDRectangle.A4.getWidth();
                float pageHeight = PDRectangle.A4.getHeight();
                float scale = Math.min(pageWidth / bufferedImage.getWidth(), pageHeight / bufferedImage.getHeight());

                int scaledWidth = (int) (bufferedImage.getWidth() * scale);
                int scaledHeight = (int) (bufferedImage.getHeight() * scale);

                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                float x = (pageWidth - scaledWidth) / 2;
                float y = (pageHeight - scaledHeight) / 2;
                contentStream.drawImage(pdImage, x, y, scaledWidth, scaledHeight);

                contentStream.close();

                document.save(selectedFile);
                System.out.println("PDF saved at: " + selectedFile.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
