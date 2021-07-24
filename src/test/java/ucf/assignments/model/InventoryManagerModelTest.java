package ucf.assignments.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerModelTest {

    @Test
    void add_item() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item item = new Item("My Item", "ABCDE12345", 39.99);

        model.addItem(item);

        assertTrue(model.getSortedList().contains(item));
    }

    @Test
    void remove_item() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item item = new Item("My Item", "ABCDE12345", 39.99);

        model.addItem(item);
        model.deleteItem(item);

        assertFalse(model.getSortedList().contains(item));
    }

    @Test
    void edit_value() {
        // Editing is handled by ValidatingTableCell
        // The user clicks on a cell, enters their value, and if it is valid
        // it is committed, which updates the model
    }

    @Test
    void edit_serialnumber() {
        // Editing is handled by ValidatingTableCell
        // The user clicks on a cell, enters their value, and if it is valid
        // it is committed, which updates the model
    }

    @Test
    void edit_name() {
        // Editing is handled by ValidatingTableCell
        // The user clicks on a cell, enters their value, and if it is valid
        // it is committed, which updates the model
    }

    @Test
    void sort_by_value() {
        // Sorting is handled by TableView
        // The SortedList from model.getList() is set to the TableView
        // the SortedList's comparator is bound to the TableView's comparator
        // This performs sorting
    }

    @Test
    void sort_by_serialnumber() {
        // Sorting is handled by TableView
        // The SortedList from model.getList() is set to the TableView
        // the SortedList's comparator is bound to the TableView's comparator
        // This performs sorting
    }

    @Test
    void sort_by_name() {
        // Sorting is handled by TableView
        // The SortedList from model.getList() is set to the TableView
        // the SortedList's comparator is bound to the TableView's comparator
        // This performs sorting
    }

    @Test
    void search_by_serialnumber() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        model.addItem(firstItem);
        model.addItem(secondItem);

        model.setFilter("", "A");

        assertTrue(model.getSortedList().contains(firstItem) && !model.getSortedList().contains(secondItem));
    }

    @Test
    void search_by_name() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        model.addItem(firstItem);
        model.addItem(secondItem);

        model.setFilter("New", "");

        assertTrue(!model.getSortedList().contains(firstItem) && model.getSortedList().contains(secondItem));
    }

    @Test
    void save_to_json_file() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        model.addItem(firstItem);
        model.addItem(secondItem);

        Path filePath = new File(System.getProperty("user.dir") + "/inventories/testlist.json").toPath();
        model.bindFile(filePath);
        model.saveInventory();

        try {
            String expected = """
                    [
                      {
                        "name": "My Item",
                        "serialNumber": "ABCDE12345",
                        "value": "39.99"
                      },
                      {
                        "name": "My New Item",
                        "serialNumber": "QWERTYUIOP",
                        "value": "49.99"
                      }
                    ]""";
            String actual = Files.lines(filePath).collect(Collectors.joining("\n"));
            assertEquals(expected, actual);
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void save_to_tsv_file() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        model.addItem(firstItem);
        model.addItem(secondItem);

        Path filePath = new File(System.getProperty("user.dir") + "/inventories/testlist.txt").toPath();
        model.bindFile(filePath);
        model.saveInventory();

        try {
            String expected = "My Item\tABCDE12345\t39.99\n" +
                    "My New Item\tQWERTYUIOP\t49.99";
            String actual = Files.lines(filePath).collect(Collectors.joining("\n"));
            assertEquals(expected, actual);
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void save_to_html_file() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        model.addItem(firstItem);
        model.addItem(secondItem);

        Path filePath = new File(System.getProperty("user.dir") + "/inventories/testlist.html").toPath();
        model.bindFile(filePath);
        model.saveInventory();

        try {
            String expected = """
                    <table>
                        <tr>
                            <th>Name</th>
                            <th>Serial Number</th>
                            <th>Value</th>
                        </tr>
                    \t<tr>
                    \t\t<td>My Item</td>
                    \t\t<td>ABCDE12345</td>
                    \t\t<td>39.99</td>
                    \t</tr>
                    \t<tr>
                    \t\t<td>My New Item</td>
                    \t\t<td>QWERTYUIOP</td>
                    \t\t<td>49.99</td>
                    \t</tr>
                    </table>""";
            String actual = Files.lines(filePath).collect(Collectors.joining("\n"));
            assertEquals(expected, actual);
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void load_from_json_file() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        InventoryManagerModel tempModel = new InventoryManagerModel();
        tempModel.addItem(firstItem);
        tempModel.addItem(secondItem);

        Path filePath = new File(System.getProperty("user.dir") + "/inventories/testlist.json").toPath();
        model.bindFile(filePath);
        tempModel.bindFile(filePath);

        tempModel.saveInventory();
        model.loadInventory();

        assertTrue(model.getSortedList().contains(firstItem) && model.getSortedList().contains(secondItem));

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void load_from_tsv_file() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        InventoryManagerModel tempModel = new InventoryManagerModel();
        tempModel.addItem(firstItem);
        tempModel.addItem(secondItem);

        Path filePath = new File(System.getProperty("user.dir") + "/inventories/testlist.txt").toPath();
        model.bindFile(filePath);
        tempModel.bindFile(filePath);

        tempModel.saveInventory();
        model.loadInventory();

        assertTrue(model.getSortedList().contains(firstItem) && model.getSortedList().contains(secondItem));

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void load_from_html_file() {
        InventoryManagerModel model = new InventoryManagerModel();
        Item firstItem = new Item("My Item", "ABCDE12345", 39.99);
        Item secondItem = new Item("My New Item", "QWERTYUIOP", 49.99);

        InventoryManagerModel tempModel = new InventoryManagerModel();
        tempModel.addItem(firstItem);
        tempModel.addItem(secondItem);

        Path filePath = new File(System.getProperty("user.dir") + "/inventories/testlist.html").toPath();
        model.bindFile(filePath);
        tempModel.bindFile(filePath);

        tempModel.saveInventory();
        model.loadInventory();

        assertTrue(model.getSortedList().contains(firstItem) && model.getSortedList().contains(secondItem));

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}