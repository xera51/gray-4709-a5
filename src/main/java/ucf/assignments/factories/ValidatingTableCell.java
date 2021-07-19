package ucf.assignments.factories;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;

// TODO allow to deselect cell when another row (and potentially Node) is selected
public class ValidatingTableCell<S, T> extends TableCell<S, T> {

    private final Callback<Void, ? extends TextField> textFieldFactory;
    private final Callback<T, Boolean> validator;
    private final String errorMessage;
    private final StringConverter<T> converter;
    private TextField textField;

    public ValidatingTableCell(Callback<Void, ? extends TextField> textFieldFactory,
                               Callback<T, Boolean> validator,
                               StringConverter<T> converter,
                               String errorMessage) {
        if (textFieldFactory == null) {
            textFieldFactory = param -> new TextField();
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
                createTextInputControl();
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

    private void createTextInputControl() {
        textField = textFieldFactory.call(null);
        textField.setText(converter.toString(this.getItem()));

        textField.setOnAction(event -> {
            if (validator != null){
                if (validator.call(converter.fromString(textField.getText()))) {
                    this.commitEdit(converter.fromString(textField.getText()));
                } else {
                    // TODO alert user when mistake
                    startEdit();
                }
            } else {
                this.commitEdit(converter.fromString(textField.getText()));
            }

            event.consume();
        });
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                this.cancelEdit();
                t.consume();
            }
        });
    }
}
