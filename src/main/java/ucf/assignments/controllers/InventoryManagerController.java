package ucf.assignments.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import ucf.assignments.controls.LimitedTextField;
import ucf.assignments.factories.ValidatingTableCell;
import ucf.assignments.model.InventoryFileDAO;
import ucf.assignments.model.InventoryManagerModel;
import ucf.assignments.model.Item;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

// TODO warn unsaved when X or exit menu item clicked
public class InventoryManagerController {

    InventoryManagerModel model = new InventoryManagerModel();
    FileChooser fileChooser = new FileChooser();

    // TODO update on edit
    boolean saved = true;

    @FXML
    private TableView<Item> itemTable;

    @FXML
    private TableColumn<Item, String> serialNumberColumn;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, BigDecimal> valueColumn;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField serialNumberFilter;

    // TODO disable when fields empty
    @FXML
    private Button addButton;

    @FXML
    private TextField serialNumberField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField valueField;

    @FXML
    private Button removeButton;


    public void initialize() {
        // TODO consider blending extension filters into one
        // FileChooser set-up
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"), new FileChooser.ExtensionFilter("HTML", "*.html"), new FileChooser.ExtensionFilter("TSV", "*.txt"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/inventories/"));

        // TODO add placeholder for tableview
        // Table set-up
        serialNumberColumn.setText("Serial Number");
        nameColumn.setText("Name");
        valueColumn.setText("Value");

        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(param -> param.getValue().valueProperty());


        serialNumberColumn.setCellFactory(param -> new ValidatingTableCell<>(
                (Callback<Void, LimitedTextField>) empty -> new LimitedTextField(10),
                sn -> sn.matches("[A-Za-z0-9]{10}") && !model.containsSerialNumber(sn),
                new DefaultStringConverter(),
                "Must be 10 characters and not a duplicate"
        ));
        nameColumn.setCellFactory(param -> new ValidatingTableCell<>(
                (Callback<Void, LimitedTextField>) empty -> new LimitedTextField(256),
                name -> name.length() >= 2,
                new DefaultStringConverter(),
                "Must be between 2 and 256 characters"
        ));
        valueColumn.setCellFactory(param -> new ValidatingTableCell<>(
                null,
                Objects::nonNull,
                new StringConverter<>() {
                    @Override
                    public String toString(BigDecimal object) {
                        return NumberFormat.getCurrencyInstance(Locale.US).format(object);
                    }

                    @Override
                    public BigDecimal fromString(String string) {
                        try {
                            return BigDecimal.valueOf(Double.parseDouble(string.replace("$", "")));
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                },
                "Must be a valid currency value"
        ));

        serialNumberColumn.setOnEditCommit(event -> model.edit(event.getRowValue(), new Item(event.getRowValue().getName(), event.getNewValue(), event.getRowValue().getValue())));

        itemTable.setItems(model.getList());
        model.getList().comparatorProperty().bind(itemTable.comparatorProperty());

        // Remove Button set-up
        removeButton.disableProperty().bind(itemTable.getSelectionModel().selectedIndexProperty().lessThan(0));
    }

    @FXML
    void printMap(ActionEvent event) {
        System.out.println(model.getMap());
        System.out.println(model.getList());
        event.consume();
    }

    @FXML
    void addItem(ActionEvent event) {
        if (nameField.getText().length() < 2) {
            nameField.selectAll();
            nameField.requestFocus();
        } else if (!serialNumberField.getText().matches("[A-Za-z0-9]{10}")) {
            serialNumberField.selectAll();
            serialNumberField.requestFocus();
        } else {
            try {
                BigDecimal value = BigDecimal.valueOf(Double.parseDouble(valueField.getText()));
                if(model.add(new Item(nameField.getText(), serialNumberField.getText(), value))) {
                    saved = false;
                } else {
                    serialNumberField.selectAll();
                    serialNumberField.requestFocus();
                }
            } catch (NumberFormatException e) {
                valueField.selectAll();
                valueField.requestFocus();
            }
        }
    }

    @FXML
    void removeItem(ActionEvent event) {
        model.remove(itemTable.getSelectionModel().getSelectedItem());
        saved = false;
        event.consume();
    }

    @FXML
    void updateFilter(KeyEvent event) {
        model.setFilter(nameFilter.getText(), serialNumberFilter.getText());
        event.consume();
    }

    @FXML
    void newInv(ActionEvent event) {
        if(!saved) {
            //confirm not saved
        }
        model.close();
        event.consume();
    }

    @FXML
    void openInv(ActionEvent event) {
        if(!saved) {
           // confirm not saved
        }
        File file = fileChooser.showOpenDialog(itemTable.getScene().getWindow());
        if (file != null) {
            Path path = file.toPath();
            model.setDao(new InventoryFileDAO(path));
            model.load();
        }
        event.consume();
    }

    @FXML
    void saveInv(ActionEvent event) {
        if(model.getDao() == null) {
            saveAsInv(event);
        } else {
            model.save();
            saved = true;
        }
        event.consume();
    }

    @FXML
    void saveAsInv(ActionEvent event) {
        File file = fileChooser.showSaveDialog(itemTable.getScene().getWindow());
        if(file != null) {
            Path path = file.toPath();
            model.setDao(new InventoryFileDAO(path));
            model.save();
            saved = true;
        }
        event.consume();
    }


    @FXML
    void deleteInv(ActionEvent event) {
        if(model.getDao() != null) {
            model.delete();
        }
        event.consume();
    }
}
