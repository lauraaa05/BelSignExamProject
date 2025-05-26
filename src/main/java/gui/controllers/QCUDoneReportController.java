package gui.controllers;

import be.Order;
import be.Picture;
import be.User;
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
import utilities.LoggedInUser;
import utilities.SceneNavigator;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QCUDoneReportController {

    @FXML
    private AnchorPane photoSectionPane;

    @FXML
    private TextArea commentsTextArea;

    @FXML
    private Button sendEmailButton;

    @FXML
    private Button savePdfButton;

    @FXML
    private Label generalCommentsLabel;

    @FXML
    private TilePane photoTile;

    @FXML
    private Label orderNumberLabel;

    private final PictureDAO pictureDAO = new PictureDAO();

    private final ReportModel reportModel =  new ReportModel();

    private Order currentOrder;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        User loggedIn = LoggedInUser.getUser();

        if (loggedIn != null && "admin".equalsIgnoreCase(loggedIn.getRole())) {
            sendEmailButton.setVisible(false);
            sendEmailButton.setManaged(false);
            savePdfButton.setVisible(false);
            savePdfButton.setManaged(false);
        }
    }

    @FXML
    private void goBackBtnAction(ActionEvent event) {
        SceneNavigator sceneNavigator = new SceneNavigator();

        User loggedInUser = LoggedInUser.getUser();

        if (loggedInUser.getRole().equalsIgnoreCase("Admin")) {
            sceneNavigator.switchTo(event, "AdminReport.fxml");
        } else if (loggedInUser.getRole().equalsIgnoreCase("Quality Control")) {
            sceneNavigator.switchTo(event, "QCUFolderScreen.fxml");
        } else {
            System.out.println("Unknown role: " + loggedInUser.getRole());
        }
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
}