/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.factories;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ucf.assignments.model.Item;

import java.text.NumberFormat;
import java.util.Locale;

public class DialogFactories {

    public static Dialog<Boolean> getDuplicateSerialDialog(Item oldItem, Item newItem, Stage owner) {
        Dialog<Boolean> duplicateSerialDialog = new Dialog<>();

        ButtonType overwrite = new ButtonType("Overwrite");

        duplicateSerialDialog.getDialogPane().getButtonTypes().setAll(overwrite, ButtonType.CANCEL);

        GridPane pane = new GridPane();
        Label alertMessage = new Label(
                "Another item is using that serial number. Would you like to replace it?"
        );
        Label oldItemData = new Label(
                "Old Item: " + oldItem.getName() + " Serial Number: " + oldItem.getSerialNumber() +
                        " Value: " + NumberFormat.getCurrencyInstance(Locale.US).format(oldItem.getValue())
        );
        Label newItemData = new Label(
                "New Item: " + newItem.getName() + " Serial Number: " + newItem.getSerialNumber() +
                        " Value: " + NumberFormat.getCurrencyInstance(Locale.US).format(newItem.getValue())
        );

        pane.addRow(0, alertMessage);
        pane.addRow(1, oldItemData);
        pane.addRow(2, newItemData);

        duplicateSerialDialog.setResultConverter(param -> param == overwrite);
        duplicateSerialDialog.setTitle("Inventory Manager");
        duplicateSerialDialog.setGraphic(pane);
        duplicateSerialDialog.initOwner(owner);
        return duplicateSerialDialog;
    }

    public static Dialog<Boolean> getNotSavedWarning(Stage owner) {
        Dialog<Boolean> notSavedWarning = new Dialog<>();

        notSavedWarning.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane pane = new GridPane();
        Label notSavedMessage = new Label(
                "You have unsaved changes. Would you like to continue?"
        );

        pane.addRow(0, notSavedMessage);

        notSavedWarning.setResultConverter(param -> param == ButtonType.OK);
        notSavedWarning.setTitle("Inventory Manager");
        notSavedWarning.setGraphic(pane);
        notSavedWarning.initOwner(owner);
        return notSavedWarning;
    }
}
