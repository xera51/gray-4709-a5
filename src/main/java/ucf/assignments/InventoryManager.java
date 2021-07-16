package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ucf.assignments.model.Item;

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
