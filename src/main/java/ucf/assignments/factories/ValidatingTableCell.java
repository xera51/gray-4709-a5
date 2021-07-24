/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Code derived from javafx.scene.control.cell.TextFieldTableCell.java from the OpenJFX project
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.factories;

import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ValidatingTableCell<S, T> extends TableCell<S, T> {

    private final Supplier<? extends TextField> textFieldFactory;
    private final BiPredicate<T, Cell<T>> validator;
    private final String errorMessage;
    private final StringConverter<T> converter;
    private TextField textField;

    //TODO validator cant be null
    public ValidatingTableCell(Supplier<? extends TextField> textFieldFactory,
                               BiPredicate<T, Cell<T>> validator,
                               StringConverter<T> converter,
                               String errorMessage) {
        if (textFieldFactory == null) {
            textFieldFactory = TextField::new;
        }
        if (validator == null) {
            throw new NullPointerException("Validator can't be null");
        }
        if (converter == null) {
            throw new NullPointerException("StringConverter can't be null");
        }
        this.textFieldFactory = textFieldFactory;
        this.validator = validator;
        this.converter = converter;
        this.errorMessage = errorMessage;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (this.isEditing()) {
                textField.setText(converter.toString(this.getItem()));
                setText(null);
                setGraphic(textField);
            } else {
                setText(converter.toString(this.getItem()));
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        if (!isEditable()
                || !getTableView().isEditable()
                || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            if (textField == null) {
                textField = EditableCellUtils.createValidatedTextField(
                        this, converter, textFieldFactory, validator, errorMessage);
            }

            textField.setText(converter.toString(this.getItem()));
            this.setText(null);
            this.setGraphic(textField);
            textField.selectAll();
            textField.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        this.setText(converter.toString(this.getItem()));
        this.setGraphic(null);
    }
}
