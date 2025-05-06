package utilities;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

    public class SceneNavigator {
        public static void switchTo(ActionEvent event, String fxmlFile) {
            try {
                Parent root = FXMLLoader.load(SceneNavigator.class.getResource("/view/" + fxmlFile));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Or we can show an alert dialog
            }
        }
    }

