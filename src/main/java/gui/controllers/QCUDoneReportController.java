package gui.controllers;

import be.Order;
import be.Picture;
import be.User;
import dal.OrderStatusDAO;
import dal.PictureDAO;
import exceptions.DALException;
import gui.model.ReportModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilities.EmailSender;
import utilities.LoggedInUser;
import utilities.PDFReportGenerator;
import utilities.SceneNavigator;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.awt.Desktop;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.imageio.ImageIO;

public class QCUDoneReportController {

    @FXML
    private Label signatureLabel;

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

    private final SceneNavigator sceneNavigator = new SceneNavigator();


    @FXML
    public void initialize() {
        User loggedIn = LoggedInUser.getUser();

    }


    public void handleGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            sceneNavigator.openNewScene(actionEvent, stage, "AdminReport.fxml", "Admin Report Menu");
        } catch (IOException e) {
            e.printStackTrace();
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

        String status = new OrderStatusDAO().getStatusForOrder(order.getOrderCode());

        loadSignatureName(order.getOrderCode());
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

}
