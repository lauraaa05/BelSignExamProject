package gui.controllers;

import be.Order;
import be.User;
import bll.OrderManager;
import dk.easv.belsignexamproject.MainLogin;
import exceptions.BLLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import utilities.LoggedInUser;
import utilities.SceneNavigator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QCUFolderController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> resultsListView;

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
    private ListView<Order> orderListView;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label currentFolderLabel;

    private final OrderManager orderManager = new OrderManager();

    private final List<String> folderDates = new ArrayList<>();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    @FXML
    public void initialize() {
        highlightActiveButton(folderButton);

        for (int year = 2024; year <= 2028; year++) {
            for (int month = 1; month <= 12; month++) {
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

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterFolders(newVal);
        });

        orderListView.setFixedCellSize(40);

        orderListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                } else {
                    setText("📄 " + order.toString());
                }
            }
        });

        User user = LoggedInUser.getUser();
        if (user != null) {
            welcomeLabel.setText("Welcome " + user.getFirstName());
        }
    }

    @FXML
    private void searchBtnAction(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();

        for (javafx.scene.Node node : folderFlowPane.getChildren()) {
            if (node instanceof VBox folderBox && !folderBox.getChildren().isEmpty()) {
                Label label = (Label) folderBox.getChildren().get(1); // Assuming second child is always the label
                String folderDate = label.getText().toLowerCase();

                boolean matches = searchText.isEmpty() || folderDate.contains(searchText);
                folderBox.setVisible(matches);
                folderBox.setManaged(matches);
            }
        }
    }


    private void filterFolders(String searchText) {
        String lowerSearch = searchText.trim().toLowerCase();

        for (Node node : folderFlowPane.getChildren()) {
            if (node instanceof VBox folderBox && !folderBox.getChildren().isEmpty()) {
                Label label = (Label) folderBox.getChildren().get(1);
                String folderDate = label.getText().toLowerCase();

                boolean matches = lowerSearch.isEmpty() || folderDate.contains(lowerSearch);
                folderBox.setVisible(matches);
                folderBox.setManaged(matches);
            }
        }
    }

    @FXML
    private void switchToMainScene(Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/QCUMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Main Screen");
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
        FXMLLoader fxmlLoader = new FXMLLoader(MainLogin.class.getResource("/view/MainLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setTitle("QCU Login");
        currentStage.setScene(scene);
        currentStage.show();
    }

    private void highlightActiveButton(Button activeButton) {
        folderButton.getStyleClass().remove("active");
        activeButton.getStyleClass().add("active");
    }


    private void openFolderAndShowOrders(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        currentFolderLabel.setText("📁 " + date);

        folderViewPane.setVisible(false);
        folderViewPane.setManaged(false);

        orderListPane.setVisible(true);
        orderListPane.setManaged(true);

        try {
            List<Order> orders = orderManager.getOrdersForDate(year, month);
            orderListView.getItems().setAll(orders);
        } catch (BLLException e) {
            e.printStackTrace();
            showError("Failed to load orders for the folder: " + e.getMessage());
        }
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
            Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                Stage stage = (Stage) orderListView.getScene().getWindow();
                sceneNavigator.<QCUReportPDFController>switchToWithData(stage, "QCUReportPDF.fxml", "QCU Report PDF", controller -> {
                    controller.setOrder(selectedOrder);
                });
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}