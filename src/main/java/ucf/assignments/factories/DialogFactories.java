/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.factories;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import ucf.assignments.model.Item;

import java.text.NumberFormat;
import java.util.Locale;

public class DialogFactories {

    private static Dialog<Boolean> duplicateSerialDialog;
    private static Dialog<ButtonType> notSavedWarning;

    public static Dialog<Boolean> getDuplicateSerialDialog(Item oldItem, Item newItem, Stage owner) {
        if (duplicateSerialDialog == null) {
            Dialog<Boolean> duplicateSerialDialog = new Dialog<>();
            DialogPane dialogPane = duplicateSerialDialog.getDialogPane();

            ButtonType overwrite = new ButtonType("Overwrite", ButtonBar.ButtonData.OK_DONE);
            dialogPane.getButtonTypes().setAll(overwrite, ButtonType.CANCEL);

            GridPane pane = new GridPane();
            Label alertMessage = new Label(
                    "Another item is using that serial number. Would you like to replace it?"
            );
            Label oldItemName = new Label("Old Item: " + oldItem.getName());
            Label oldItemSerialNumber = new Label("Serial Number: " + oldItem.getSerialNumber());
            Label oldItemValue = new Label(
                            " Value: " + NumberFormat.getCurrencyInstance(Locale.US).format(oldItem.getValue())
            );
            Label newItemName = new Label("New Item: " + newItem.getName());
            Label newItemSerialNumber = new Label("Serial Number: " + newItem.getSerialNumber());
            Label newItemValue = new Label(
                    " Value: " + NumberFormat.getCurrencyInstance(Locale.US).format(newItem.getValue())
            );

            alertMessage.setStyle("-fx-font-weight: bold");

            pane.addRow(0, alertMessage);
            GridPane.setColumnSpan(alertMessage, GridPane.REMAINING);
            pane.addRow(1, oldItemName, oldItemSerialNumber, oldItemValue);
            pane.addRow(2, newItemName, newItemSerialNumber, newItemValue);
            pane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            pane.setVgap(6);
            pane.setHgap(6);

            duplicateSerialDialog.setResultConverter(param -> param == overwrite);
            duplicateSerialDialog.setTitle("Inventory Manager");
            dialogPane.setContent(pane);
            duplicateSerialDialog.initOwner(owner);
            addCssFile(duplicateSerialDialog);
            dialogPane.getStyleClass().add("duplicateSerialDialog");
            DialogFactories.duplicateSerialDialog = duplicateSerialDialog;
        }
        return duplicateSerialDialog;
    }

    public static Dialog<ButtonType> getNotSavedWarning(Stage owner) {
        if (notSavedWarning == null) {
            Dialog<ButtonType> notSavedWarning = new Dialog<>();

            ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.YES);
            ButtonType dontSave = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);

            notSavedWarning.getDialogPane().getButtonTypes().setAll(save, dontSave, ButtonType.CANCEL);

            GridPane pane = new GridPane();
            Label notSavedMessage = new Label(
                    "You have unsaved changes. Would you like to save?"
            );

            notSavedMessage.setPrefWidth(Region.USE_COMPUTED_SIZE);
            notSavedMessage.setPrefHeight(Region.USE_COMPUTED_SIZE);

            notSavedWarning.setTitle("Inventory Manager");
            notSavedWarning.getDialogPane().setContent(notSavedMessage);
            notSavedWarning.initOwner(owner);
            addCssFile(notSavedWarning);
            notSavedWarning.getDialogPane().getStyleClass().add("notSavedWarning");
            DialogFactories.notSavedWarning =  notSavedWarning;
        }
        return notSavedWarning;
    }

    @SuppressWarnings("ConstantConditions")
    private static void addCssFile(Dialog<?> dialog) {
        dialog.getDialogPane()
              .getStylesheets()
              .add(DialogFactories.class
                      .getResource("/ucf/assignments/css/InventoryManager.css")
                      .toExternalForm());
    }
}
