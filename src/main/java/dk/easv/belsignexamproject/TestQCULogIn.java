package dk.easv.belsignexamproject;

import dal.OrderDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TestQCULogIn extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OperatorLogInApp.class.getResource("/view/QCULogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Operator Log In");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    private void loadOrdersToApprove() {
        System.out.println("[LOAD] Loading orders to approve from DB");

        OrderDAO orderDAO;
        List<String> formattedOrders = OrderDAO.getFormattedOrderNumbers();
        ObservableList<String> orders = FXCollections.observableArrayList(formattedOrders);

        ListView<String> toApproveListView = null;
        toApproveListView.setItems(orders);
        toApproveListView.setFixedCellSize(48); // Opcional para estética
    }

}