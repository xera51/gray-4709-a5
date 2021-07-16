package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ucf.assignments.model.InventoryManagerModel;
import ucf.assignments.model.Item;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryManagerController {

    InventoryManagerModel model = new InventoryManagerModel();

    @FXML
    private TableView<Item> itemTable;

    @FXML
    private TableColumn<Item, String> serialNumberColumn;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, String> valueColumn;

    public void initialize() {

        // Table set-up
        serialNumberColumn.setText("Serial Number");
        nameColumn.setText("Name");
        valueColumn.setText("Value");

        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        itemTable.setItems(model.getList());
    }
}
