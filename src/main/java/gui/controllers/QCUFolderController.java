package gui.controllers;

import bll.OrderManager;
import dal.OrderDAO;
import dk.easv.belsignexamproject.OperatorLogInApp;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utilities.SceneNavigator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QCUFolderController {

    @FXML
    private FlowPane folderFlowPane;

    @FXML
    private Button signOutButton, homeButton;

    @FXML
    private Button folderButton;

    @FXML
    private VBox folderViewPane;

    @FXML
    private VBox orderListPane;

    @FXML
    private ListView<String> orderListView;

    @FXML
    private Label currentFolderLabel;

    private OrderManager orderManager =  new OrderManager();

    private final List<String> folderDates = new ArrayList<>();

    private final SceneNavigator sceneNavigator = new SceneNavigator();
    @FXML
    public void initialize() {

        highlightActiveButton(folderButton);
        for(int year = 2025; year <= 2026; year++) {
            for(int month = 1; month <= 12; month++) {
                folderDates.add(String.format("%d-%02d", year, month));
            }
        }

        for (String date : folderDates) {
            VBox folderBox = new VBox(5);
            folderBox.setAlignment(Pos.CENTER);
            folderBox.setPrefWidth(100);

            Button folderButton = new Button();
            ImageView icon = new ImageView(new Image(getClass().getResource("/images/folder.png").toExternalForm()));
            icon.setFitWidth(60);
            icon.setFitHeight(60);
            folderButton.setGraphic(icon);
            folderButton.getStyleClass().add("folder-button");

            folderButton.setOnAction(e -> openFolderAndShowOrders(date));

            Label label = new Label(date);
            label.setStyle("-fx-font-size: 13px;");

            folderBox.getChildren().addAll(folderButton, label);
            folderFlowPane.getChildren().add(folderBox);
        }

        orderListView.setFixedCellSize(40);
    }

    @FXML
    private void switchToMainScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Folder Screen");
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    private void handleMainButtonClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage) homeButton.getScene().getWindow();
        try {
            switchToMainScene(currentStage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSignOutButtonClick(ActionEvent event) {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        try {
            switchToLoginScene(currentStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToLoginScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCULogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Login");
        currentStage.setScene(scene);
        currentStage.show();
    }

    private void highlightActiveButton(Button activeButton) {
        folderButton.getStyleClass().remove("active");

        activeButton.getStyleClass().add("active");
    }

    @FXML
    private void btnOpenReportAction(ActionEvent actionEvent) {
        sceneNavigator.switchTo(actionEvent, "QCUReport.fxml");
    }

    private void openFolderAndShowOrders(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        List<String> orders = orderManager.getOrdersForDate(year, month);

        currentFolderLabel.setText("📁 " + date);

        folderViewPane.setVisible(false);
        folderViewPane.setManaged(false);

        orderListPane.setVisible(true);
        orderListPane.setManaged(true);

        List<String> formattedOrders = orders.stream()
                .map(order -> "\uD83D\uDCC4 " + order)
                .toList();

        orderListView.getItems().setAll(formattedOrders);
    }

    @FXML
    private void handleBackToFolders() {
        orderListPane.setVisible(false);
        orderListPane.setManaged(false);

        folderViewPane.setVisible(true);
        folderViewPane.setManaged(true);
    }

    @FXML
    private void handleOrderClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            String selectedOrderNumber = orderListView.getSelectionModel().getSelectedItem();
            if (selectedOrderNumber != null) {
                String clearOrderNumber = selectedOrderNumber.replace("\uD83D\uDCC4 ", "");
                openOrderReportScene(selectedOrderNumber);
            }
        }
    }

    private void openOrderReportScene(String orderNumber) {
        Stage stage = (Stage) orderListView.getScene().getWindow();

        SceneNavigator sceneNavigator = new SceneNavigator();
        sceneNavigator.<QCUReportController>switchToWithData(stage, "QCUReport.fxml", qcuReportController -> {
            qcuReportController.setOrderNumber(orderNumber);
        });
    }
}