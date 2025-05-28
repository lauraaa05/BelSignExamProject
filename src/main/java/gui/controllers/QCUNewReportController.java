package gui.controllers;

import be.Order;
import be.Picture;
import be.Report;
import dal.OrderStatusDAO;
import dal.PictureDAO;
import gui.model.ReportModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QCUNewReportController {

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
    private Label orderNumberLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final PictureDAO pictureDAO = new PictureDAO();

    private final ReportModel reportModel = new ReportModel();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    private Order currentOrder;

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> submitComment());

        rejectButton.setOnAction(event -> handleReject());
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        orderNumberLabel.setText("ORDER NUMBER: " + order);

        loadPictures(order.toString());
        loadLatestComment(order.getOrderCode());

        String status = new OrderStatusDAO().getStatusForOrder(order.getOrderCode());
        if ("done".equalsIgnoreCase(status)) {
            submitButton.setVisible(false);
            commentsTextArea.setEditable(false);
        }
    }

    private VBox createImageCard(Picture picture) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #fff; -fx-padding: 10px; -fx-border-color: #d1d5db; -fx-border-radius: 5px; -fx-background-radius: 8px;");
        vBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(130);
        imageView.setFitWidth(180);
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
        } catch (SQLException e) {
            System.err.println("Database error while loading pictures: " + e.getMessage());
        }
    }

    private void submitComment() {
        String commentText = commentsTextArea.getText();
        if (commentText == null || commentText.isEmpty()) {
            System.out.println("Comment is empty");
            return;
        }

        try {
            String fullOrderNumber = extractOrderNumber();
            String orderCode = fullOrderNumber.substring(fullOrderNumber.lastIndexOf("-") + 1);

            Report report = new Report(4, commentText, fullOrderNumber, LocalDateTime.now(), orderCode);
            reportModel.insertReport(report);

            boolean updated = new OrderStatusDAO().updateOrderStatus(orderCode, "qcu", "done");

            if (updated) {
                System.out.println("Order marked as done.");
            }

            commentsTextArea.clear();
            loadLatestComment(fullOrderNumber);

            submitButton.setVisible(false);
            commentsTextArea.setEditable(false);

            hideSubmitButton(orderCode);

            Stage currentStage = (Stage) submitButton.getScene().getWindow();
            currentStage.close();
            sceneNavigator.switchTo("/view/QCUMain.fxml");

        } catch (Exception e) {
            e.printStackTrace();
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

    private String extractOrderNumber() {
        return orderNumberLabel.getText().replace("ORDER NUMBER: ", "").trim();
    }

    private void refreshLatestComment() {
        try {
            String latestComment = reportModel.getLatestCommentByOrderNumber(extractOrderNumber());
            generalCommentsLabel.setText(latestComment);
        } catch (SQLException e) {
            generalCommentsLabel.setText("Failed to fetch comment: " + e.getMessage());
        }
    }

    @FXML
    private void goBackBtnAction(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "QCUMain.fxml");
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
    private void handleReject() {
        try {
            String commentText = commentsTextArea.getText();
            String fullOrderNumber = extractOrderNumber();
            String orderCode = fullOrderNumber.substring(fullOrderNumber.lastIndexOf("-") + 1);

            // Save rejected comment
            if (commentText != null && !commentText.isEmpty()) {
                Report report = new Report(4, "[REJECTED] " + commentText, fullOrderNumber, LocalDateTime.now(), orderCode);
                reportModel.insertReport(report);
            }

            // Update order status to 'rejected'
            boolean updated = new OrderStatusDAO().updateOrderStatus(orderCode, "operator", "rejected");

            if (updated) {
                System.out.println("Order marked as rejected and returned to operator.");
            } else {
                System.out.println("Failed to update order status to rejected.");
            }

            // UI updates
            rejectButton.setDisable(true);
            rejectButton.setVisible(false);
            commentsTextArea.setEditable(false);

            // Navigate away
            sceneNavigator.switchTo("/view/QCUMain.fxml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
