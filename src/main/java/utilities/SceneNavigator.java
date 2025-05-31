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

    /**
     * Switches to a new scene based on an ActionEvent (e.g., button click).
     * Loads the FXML file from the /view/ directory.
     *
     * @param event     The ActionEvent from the UI component.
     * @param fxmlFile  The name of the FXML file (e.g., "OperatorMain.fxml").
     */
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

    /**
     * Switches to a new scene and initializes its controller with data.
     * Useful when passing values to the controller before displaying the scene.
     *
     * @param stage                The target stage to set the new scene on.
     * @param fxmlFile             The name of the FXML file (e.g., "OperatorMain.fxml").
     * @param controllerInitializer A lambda function to initialize the controller.
     * @param <T>                  The type of the controller.
     */
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

    /**
     * Opens a new scene in a new window, based only on FXML path.
     * Example: switchTo("/view/OperatorMain.fxml");
     *
     * @param fxmlPath The path to the FXML file (including folder).
     */
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
