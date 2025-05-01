package gui.controllers;

import bll.OrderManager;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import dal.OrderDAO;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OperatorMainController implements Initializable {

    @FXML
    private ListView toDoListView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrdersIntoToDoList();
    }

    private void loadOrdersIntoToDoList() {
        OrderManager om = new OrderManager();
        List<String> orders = om.getOrderNumbersAsList();
        toDoListView.getItems().addAll(orders);
        toDoListView.setFixedCellSize(48);
    }

}
