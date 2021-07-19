package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// TODO copyright comments
// TODO switch TextFields to LimitedTextFields when done with SceneBuilder
// TODO Clean up classes to follow SRP (time permitting)
// TODO callback to get stage
public class InventoryManager extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResourceAsStream("/fxml/InventoryManager.fxml"));

        Scene scene = new Scene(loader.getRoot());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
