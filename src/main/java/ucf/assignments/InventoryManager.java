/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ucf.assignments.controllers.InventoryManagerController;

import java.io.InputStream;

// NOTE: Undefined behaviour when input files are not formatted correctly
public class InventoryManager extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(param -> new InventoryManagerController(primaryStage));
        loader.load(getClass().getResourceAsStream("/ucf/assignments/fxml/InventoryManager.fxml"));

        Scene scene = new Scene(loader.getRoot());
        primaryStage.setTitle("Inventory Manager");
        primaryStage.setScene(scene);

        InputStream icon = this.getClass().getResourceAsStream("/ucf/assignments/images/AppIcon.png");
        if (icon != null) {
            primaryStage.getIcons().add(new Image(icon));
        }

        primaryStage.show();
    }
}
