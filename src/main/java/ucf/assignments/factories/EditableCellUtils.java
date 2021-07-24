/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Code derived from javafx.scene.control.cell.CellUtils.java from the OpenJFX project
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.factories;

import javafx.geometry.Side;
import javafx.scene.control.Cell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import ucf.assignments.controls.Notification;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class EditableCellUtils {

    static <T> TextField createValidatedTextField(final Cell<T> cell,
                                                  final StringConverter<T> converter,
                                                  final Supplier<? extends TextField> textFieldFactory,
                                                  final BiPredicate<T, Cell<T>> validator,
                                                  final String errorMessage) {

        final TextField textField = textFieldFactory.get();
        textField.setText(converter.toString(cell.getItem()));

        textField.setOnAction(event -> {
            if (textField.getText().equals(converter.toString(cell.getItem()))) {
                cell.cancelEdit();
            } else if (validator.test(converter.fromString(textField.getText()), cell)) {
                cell.commitEdit(converter.fromString(textField.getText()));
            } else {
                textField.selectAll();
                textField.requestFocus();
                new Notification(errorMessage).show(textField, Side.BOTTOM, 5, 0);
            }
            event.consume();
        });
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                cell.cancelEdit();
                t.consume();
            }
        });

        return textField;
    }
}
