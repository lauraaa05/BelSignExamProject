package gui.controllers;

import bll.OrderManager;
import dk.easv.belsignexamproject.OperatorLogInApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
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
    private ListView<String> orderListView;

    @FXML
    private Label currentFolderLabel;

    private final OrderManager orderManager = new OrderManager();

    private final List<String> folderDates = new ArrayList<>();

    private final SceneNavigator sceneNavigator = new SceneNavigator();

    private final List<VBox> allFolderNodes = new ArrayList<>();


    @FXML
    public void initialize() {
        highlightActiveButton(folderButton);

        for (int year = 2025; year <= 2026; year++) {
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


    private List<String> performSearch(String searchText) {
        // Case-insensitive partial match search on folderDates
        return folderDates.stream()
                .filter(date -> date.toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void displayResults(List<String> results) {
        resultsListView.getItems().clear();
        resultsListView.getItems().addAll(results);
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
}
