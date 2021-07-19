package ucf.assignments.factories;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import ucf.assignments.controls.LimitedTextField;

// TODO Make a TextFieldCell that has limited length or use an eventhandler
public class LimitedTextFieldTableCell<S,T> extends TableCell<S,T> {

    private LimitedTextField textField;

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
        } else {
            if(isEditing()) {
                setText(null);
                setGraphic(textField);
            } else {
//                setText();
            }
        }
    }

    public void setConverter(StringConverter<T> converter) {
//        converter
    }

    @Override
    public void startEdit() {
        if (! isEditable()
                || ! getTableView().isEditable()
                || ! getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            if (textField == null) {
//                textField = CellUtils.createTextField(this, getConverter());
            }

//            CellUtils.startEdit(this, getConverter(), null, null, textField);
        }
    }

    @Override
    public void commitEdit(T newValue) {
        super.commitEdit(newValue);
    }
}
