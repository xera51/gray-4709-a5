package ucf.assignments.factories;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import net.xera51.javafx.control.Notification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
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
            if(textField.getText().equals(converter.toString(cell.getItem()))) {
                cell.cancelEdit();
            } else if (validator.test(converter.fromString(textField.getText()), cell)) {
                cell.commitEdit(converter.fromString(textField.getText()));
            } else {
                textField.selectAll();
                textField.requestFocus();
                Notification errorNotification = new Notification(errorMessage);
                errorNotification.show(textField, Side.BOTTOM, 5, 0);
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
