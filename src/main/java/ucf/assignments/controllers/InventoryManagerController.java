/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import ucf.assignments.controls.LimitedTextField;
import ucf.assignments.controls.Notification;
import ucf.assignments.factories.DialogFactories;
import ucf.assignments.factories.ValidatingTableCell;
import ucf.assignments.model.InventoryManagerModel;
import ucf.assignments.model.Item;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class InventoryManagerController {

    Stage stage;
    InventoryManagerModel model = new InventoryManagerModel();
    FileChooser fileChooser = new FileChooser();
    List<TextField> addFields = new ArrayList<>();
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

    @FXML
    private ImageView editImageView;


    public InventoryManagerController(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        // FileChooser set-up
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Inventory", "*.json", "*.html", "*.txt"));
        fileChooser.setInitialDirectory(getDefaultDirectory());

        // TODO add placeholder for tableview
        // Table Set-up
        itemTable.setItems(model.getSortedList());
        model.getSortedList().comparatorProperty().bind(itemTable.comparatorProperty());

        // Table Column Set-up
        serialNumberColumn.setText("Serial Number");
        nameColumn.setText("Name");
        valueColumn.setText("Value");

        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(param -> param.getValue().valueProperty());

        serialNumberColumn.setCellFactory(param -> new ValidatingTableCell<>(
                () -> new LimitedTextField(10),
                (sn, cell) -> {
                    if (sn.matches("[A-Za-z0-9]{10}")) {
                        if (model.containsSerialNumber(sn)) {
                            //noinspection rawtypes
                            return DialogFactories.getDuplicateSerialDialog(
                                    model.getItemBySerialNumber(sn),
                                    (Item) ((TableCell) cell).getTableRow().getItem(),
                                    stage).showAndWait().orElse(false);
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                },
                new DefaultStringConverter(),
                "Serial Numbers must be exactly 10 characters and contain only letters and numbers"
        ));

        nameColumn.setCellFactory(param -> new ValidatingTableCell<>(
                () -> new LimitedTextField(256),
                (name, cell) -> name.length() >= 2,
                new DefaultStringConverter(),
                "Items must be at least 2 characters"
        ));

        valueColumn.setCellFactory(param -> new ValidatingTableCell<>(
                null,
                (bigDecimal, bigDecimalCell) -> bigDecimal != null,
                new StringConverter<>() {
                    @Override
                    public String toString(BigDecimal object) {
                        return NumberFormat.getCurrencyInstance(Locale.US).format(object);
                    }

                    @Override
                    public BigDecimal fromString(String string) {
                        try {
                            return BigDecimal.valueOf(Double.parseDouble(string.replace("$", "").replace(",", "")));
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                },
                "Must be a valid currency value"
        ));

        nameColumn.addEventHandler(TableColumn.<Item, String>editCommitEvent(), event -> saved = false);
        serialNumberColumn.setOnEditCommit(event -> {
            model.updateSerialNumber(event.getOldValue(), event.getNewValue().toUpperCase());
            saved = false;
        });
        valueColumn.addEventHandler(TableColumn.<Item, String>editCommitEvent(), event -> saved = false);

        // Add Button set-up
        addButton.disableProperty().bind(
                nameField.textProperty().isEmpty().or(
                        serialNumberField.textProperty().isEmpty()).or(
                        valueField.textProperty().isEmpty())
        );

        // Remove Button set-up
        removeButton.disableProperty().bind(itemTable.getSelectionModel().selectedIndexProperty().lessThan(0));

        // TODO move to FXML when done with SceneBuilder
        // Search Icon setup
        InputStream searchIcon = this.getClass().getResourceAsStream("/ucf/assignments/images/SearchIcon.png");
        if (searchIcon != null) {
            editImageView.setImage(new Image(searchIcon));
        }

        // Add Fields setup
        Collections.addAll(addFields, nameField, serialNumberField, valueField);

        // Stage setup
        stage.setOnCloseRequest(event -> {
            onExitRequest();
            event.consume();
        });
    }

    @FXML
    void printMap(ActionEvent event) {
        // TODO help
        event.consume();
    }

    /***************************************************************************
     *                                                                         *
     * Event Handlers                                                          *
     *                                                                         *
     **************************************************************************/

    @FXML
    void addItem(ActionEvent event) {
        if (nameField.getText().length() < 2) {
            selectAndFocus(nameField);
            showNotification(nameField, "Items must be at least 2 characters");
        } else if (!serialNumberField.getText().matches("[A-Za-z0-9]{10}")) {
            selectAndFocus(serialNumberField);
            showNotification(serialNumberField,
                    "Serial Numbers must be exactly 10 characters and only contain letters and numbers");
        } else if (!isDoubleValue(valueField.getText())) {
            selectAndFocus(valueField);
            showNotification(valueField, "Must be a valid currency value");
        } else {
            double value = Double.parseDouble(valueField.getText());
            Item newItem = new Item(nameField.getText(), serialNumberField.getText().toUpperCase(), value);
            if (!model.containsSerialNumber(serialNumberField.getText().toUpperCase()) ||
                    DialogFactories.getDuplicateSerialDialog(
                            model.getItemBySerialNumber(serialNumberField.getText().toUpperCase()),
                            newItem,
                            stage).showAndWait().orElse(false)) {
                model.addItem(newItem);
                clearAddFields();
                saved = false;
            } else {
                selectAndFocus(serialNumberField);
            }
        }
        event.consume();
    }

    @FXML
    void removeItem(ActionEvent event) {
        model.deleteItem(itemTable.getSelectionModel().getSelectedItem());
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
        confirmIfNotSaved();
        if (saved) {
            model.closeInventory();
            clearAddFields();
        }
        event.consume();
    }

    @FXML
    void openInv(ActionEvent event) {
        confirmIfNotSaved();
        if (saved) {
            File file = showOpenFileChooser();
            if (file != null) {
                model.bindFile(file.toPath());
                model.loadInventory();
                clearAddFields();
            }
        }
        event.consume();
    }

    @FXML
    void saveInv(ActionEvent event) {
        if (model.isBound()) {
            model.saveInventory();
            saved = true;
        } else {
            saveAsInv(event);
        }
        event.consume();
    }

    @FXML
    void saveAsInv(ActionEvent event) {
        File file = showSaveFileChooser();
        if (file != null) {
            model.bindFile(file.toPath());
            model.saveInventory();
            saved = true;
        }
        event.consume();
    }


    @FXML
    void deleteInv(ActionEvent event) {
        if (model.isBound()) {
            model.deleteInventory();
        }
        event.consume();
    }

    @FXML
    void onExit(ActionEvent event) {
        onExitRequest();
        event.consume();
    }

    private void onExitRequest() {
        confirmIfNotSaved();
        if (saved) {
            Platform.exit();
        }
    }

    /***************************************************************************
     *                                                                         *
     * Private Implementation                                                  *
     *                                                                         *
     **************************************************************************/


    private void showNotification(Node node, String message) {
        new Notification(message).show(node, Side.TOP, 5, 0);
    }

    private void setDirectory() {
        if (!fileChooser.getInitialDirectory().exists()) {
            fileChooser.setInitialDirectory(getDefaultDirectory());
        }
    }

    private File getDefaultDirectory() {
        File inventoryDirectory = new File(System.getProperty("user.dir") +
                File.separator + "inventories" + File.separator);
        if (!inventoryDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            inventoryDirectory.mkdir();
        }
        return inventoryDirectory;
    }

    private void clearAddFields() {
        for (TextField field : addFields) {
            field.clear();
        }
    }

    private void confirmIfNotSaved() {
        if (!saved) {
            saved = DialogFactories.getNotSavedWarning(stage).showAndWait().orElse(false);
        }
    }

    private File showOpenFileChooser() {
        fileChooser.setTitle("Open");
        setDirectory();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
        }
        return file;
    }

    private File showSaveFileChooser() {
        fileChooser.setTitle("Save As");
        setDirectory();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
        }
        return file;
    }

    private void selectAndFocus(TextField textField) {
        textField.selectAll();
        textField.requestFocus();
    }

    private boolean isDoubleValue(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
