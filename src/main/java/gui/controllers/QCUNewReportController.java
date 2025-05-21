package gui.controllers;

import be.Order;
import be.Picture;
import be.Report;
import dal.OrderStatusDAO;
import dal.PictureDAO;
import gui.model.ReportModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class QCUNewReportController {

    @FXML
    private AnchorPane photoSectionPane;

    @FXML
    private TextArea commentsTextArea;

    @FXML
    private Button submitButton;

    @FXML
    private Label generalCommentsLabel;

    @FXML
    private TilePane photoTile;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private VBox productDetailsSection;
    @FXML
    private HBox productDetailsBox;
//    @FXML
//    private Label materialTypeLabel;
//    @FXML
//    private Label colorLabel;
//    @FXML
//    private Label weightLabel;
//    @FXML
//    private Label heightLabel;
//    @FXML
//    private Label lengthLabel;
//    @FXML
//    private Label widthLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private PictureDAO pictureDAO = new PictureDAO();

    private ReportModel reportModel = new ReportModel();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> submitComment());


    }

    private VBox createImageCard(Picture picture) {
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #fff; -fx-padding: 10px; -fx-border-color: #d1d5db; -fx-border-radius: 5px; -fx-background-radius: 8px;");
        vBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(130);
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);
        imageView.setImage((new Image(new ByteArrayInputStream(picture.getImage()))));

        String sideText = (picture.getSide() == null || picture.getSide().isEmpty()) ? "Unknown" : picture.getSide();
        Label sideLabel = new Label("Side: " + sideText); //File name just be back front up...
        Label dateLabel = new Label("Date: " + picture.getTimestamp().format(formatter));

        vBox.getChildren().addAll(imageView, sideLabel, dateLabel);

        return vBox;
    }

    public void setOrder(Order order) {
        orderNumberLabel.setText("ORDER NUMBER: " + order);

        loadPictures(order.getOrderCode());
        loadLatestComment(order.getOrderCode());

        String status = new OrderStatusDAO().getStatusForOrder(order.getOrderCode());
        if ("done".equalsIgnoreCase(status)) {
            submitButton.setVisible(false);
            commentsTextArea.setEditable(false);
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
        } catch (SQLException e) {
            System.err.println("Database error while loading pictures: " + e.getMessage());
            e.printStackTrace();
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

            OrderStatusDAO dao = new OrderStatusDAO();
            boolean updated = dao.updateOrderStatus(orderCode, "qcu", "done");

            if (updated) {
                System.out.println("Order marked as done.");
            } else {
                System.out.println("Order status update failed!");
            }

            commentsTextArea.clear();
            loadLatestComment(fullOrderNumber);

            submitButton.setVisible(false);
            commentsTextArea.setEditable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUMain.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) submitButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("QCU Main");
            currentStage.show();
        } catch (IOException e) {
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
        sceneNavigator.switchTo(actionEvent, "QCUFolderScreen.fxml");
    }

    public void onRejectAction(ActionEvent actionEvent) {
    }
}
