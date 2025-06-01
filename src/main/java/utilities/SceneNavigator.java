package utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneNavigator {

    public void switchTo(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(SceneNavigator.class.getResource("/view/" + fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // You can replace with an alert for production
        }
    }

    public <T> void switchToWithData(Stage stage, String fxmlFile, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/view/" + fxmlFile));
            Parent root = loader.load();

            T controller = loader.getController();
            controllerInitializer.accept(controller);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNewScene(ActionEvent actionEvent, Stage stage, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("QCU Folder Screen");
        stage.setScene(scene);
        stage.show();
    }

    public void openNewScene(ActionEvent actionEvent, Stage stage, String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(title); // set custom title
        stage.setScene(scene);
        stage.show();
    }

    public <T> void switchToWithData(Stage stage, String fxmlFile, String title, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/view/" + fxmlFile));
            Parent root = loader.load();

            T controller = loader.getController();
            controllerInitializer.accept(controller);

            stage.setScene(new Scene(root));
            stage.setTitle(title); // ✅ Set new window title here
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
