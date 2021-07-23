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
import net.xera51.javafx.control.Notification;
import ucf.assignments.controls.LimitedTextField;
import ucf.assignments.factories.DialogFactories;
import ucf.assignments.factories.ValidatingTableCell;
import ucf.assignments.model.InventoryManagerModel;
import ucf.assignments.model.Item;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class InventoryManagerController {

    InventoryManagerModel model = new InventoryManagerModel();
    FileChooser fileChooser = new FileChooser();
    Stage stage;

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

    @FXML
    private ImageView editImageView;


    public InventoryManagerController(Stage stage) {
        this.stage = stage;
    }

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
                () -> new LimitedTextField(10),
                (sn, cell) -> {
                    if (sn.matches("[A-Za-z0-9]{10}")) {
                        if(model.containsSerialNumber(sn)) {
                            return DialogFactories.getDuplicateSerialDialog(
                                    model.getItemBySerialNumber(sn),
                                    (Item)((TableCell)cell).getTableRow().getItem(),
                                    stage).showAndWait().orElse(false);
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                },
                new DefaultStringConverter(),
                "Serial Numbers must be exactly 10 characters and only contain letters and numbers"
        ));
        nameColumn.setCellFactory(param -> new ValidatingTableCell<>(
                () -> new LimitedTextField(256),
                (name, cell) -> name.length() >= 2,
                new DefaultStringConverter(),
                "Items must be at least 2 characters"
        ));
        // TODO editing being weird with comma
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

        serialNumberColumn.setOnEditCommit(event -> {
            Item newItem = new Item(event.getRowValue().getName(), event.getNewValue(), event.getRowValue().getValue());
            model.edit(event.getRowValue(), newItem);
            saved = false;
        });
        nameColumn.setOnEditCommit(event -> saved = false);
        valueColumn.setOnEditCommit(event -> saved = false);


        itemTable.setItems(model.getList());
        model.getList().comparatorProperty().bind(itemTable.comparatorProperty());

        // Add Button set-up
        addButton.disableProperty().bind(
                nameField.textProperty().isEmpty()
                        .or(serialNumberField.textProperty().isEmpty())
                        .or(valueField.textProperty().isEmpty())
        );

        // Remove Button set-up
        removeButton.disableProperty().bind(itemTable.getSelectionModel().selectedIndexProperty().lessThan(0));

        // Search Icon setup
        InputStream searchIcon = this.getClass().getResourceAsStream("/ucf/assignments/fxml/SearchIcon.png");
        if(searchIcon != null) {
            editImageView.setImage(new Image(searchIcon));
        }

        // Stage setup
        stage.setOnCloseRequest(event -> {
            onExitRequest();
            event.consume();
        });
    }

    @FXML
    void printMap(ActionEvent event) {
        // help
        event.consume();
    }

    @FXML
    void addItem(ActionEvent event) {
        if (nameField.getText().length() < 2) {
            nameField.selectAll();
            nameField.requestFocus();
            showNotification(nameField, "Items must be at least 2 characters");
        } else if (!serialNumberField.getText().matches("[A-Za-z0-9]{10}")) {
            serialNumberField.selectAll();
            serialNumberField.requestFocus();
            showNotification(serialNumberField,
                    "Serial Numbers must be exactly 10 characters and only contain letters and numbers");
        } else {
            try {
                BigDecimal value = BigDecimal.valueOf(Double.parseDouble(valueField.getText()));
                Item newItem = new Item(nameField.getText(), serialNumberField.getText(), value);
                if(!model.containsSerialNumber(serialNumberField.getText())) {
                    model.add(newItem);
                    nameField.clear();
                    serialNumberField.clear();
                    valueField.clear();
                    saved = false;
                } else {
                    if (DialogFactories.getDuplicateSerialDialog(
                            model.getItemBySerialNumber(serialNumberField.getText()),
                            newItem,
                            stage).showAndWait().orElse(false)) {
                        model.add(newItem);
                        nameField.clear();
                        serialNumberField.clear();
                        valueField.clear();
                        saved = false;
                    } else {
                        serialNumberField.selectAll();
                        serialNumberField.requestFocus();
                    }
                }
            } catch (NumberFormatException e) {
                valueField.selectAll();
                valueField.requestFocus();
                showNotification(valueField, "Must be a valid currency value");
            }
        }
        event.consume();
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
            saved = DialogFactories.getNotSavedWarning(stage).showAndWait().orElse(false);
        }
        if (saved) {
            model.close();
            nameField.clear();
            serialNumberField.clear();
            valueField.clear();
        }
        event.consume();
    }

    @FXML
    void openInv(ActionEvent event) {
        if (!saved) {
            saved = DialogFactories.getNotSavedWarning(stage).showAndWait().orElse(false);
        }
        if (saved) {
            File file = fileChooser.showOpenDialog(itemTable.getScene().getWindow());
            if (file != null) {
                model.bindFile(file.toPath());
                model.load();
                nameField.clear();
                serialNumberField.clear();
                valueField.clear();
            }
        }
        event.consume();
    }

    @FXML
    void saveInv(ActionEvent event) {
        if(model.isBound()) {
            model.save();
            saved = true;
        } else {
            saveAsInv(event);
        }
        event.consume();
    }

    @FXML
    void saveAsInv(ActionEvent event) {
        File file = fileChooser.showSaveDialog(itemTable.getScene().getWindow());
        if(file != null) {
            model.bindFile(file.toPath());
            model.save();
            saved = true;
        }
        event.consume();
    }


    @FXML
    void deleteInv(ActionEvent event) {
        if(model.isBound()) {
            model.delete();
        }
        event.consume();
    }

    @FXML
    void onExit(ActionEvent event) {
        onExitRequest();
        event.consume();
    }

    void onExitRequest() {
        if(!saved) {
            saved = DialogFactories.getNotSavedWarning(stage).showAndWait().orElse(false);
        }
        if(saved) {
            Platform.exit();
        }
    }

    private void showNotification(Node node, String message) {
        new Notification(message).show(node, Side.TOP, 5, 0);
    }
}
