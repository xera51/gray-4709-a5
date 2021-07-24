package ucf.assignments.model;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

// NOTE: Should make at least some fields properties
public class InventoryManagerModel {
    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final ObservableMap<String, Item> itemMap = FXCollections.observableHashMap();
    private final ObservableList<Item> itemList = FXCollections.observableArrayList(Item.extractor());
    private final FilteredList<Item> filteredItemList = new FilteredList<>(itemList);
    private final SortedList<Item> sortedItemList = new SortedList<>(filteredItemList);

    private InventoryDAO dataAccessObject;


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/
    public InventoryManagerModel() {
        // Updates the List when the Map updates
        itemMap.addListener((MapChangeListener<String, Item>) change -> {
            if (change.wasAdded()) {
                itemList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                itemList.remove(change.getValueRemoved());
            }
        });
    }


    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    public SortedList<Item> getSortedList() {
        return sortedItemList;
    }

    public void setFilter(String name, String serialNumber) {
        filteredItemList.setPredicate(item ->
                item.getName().toLowerCase().contains(name.toLowerCase())
                        && item.getSerialNumber().contains(serialNumber));
    }

    public void addItem(Item item) {
        // We must remove an item if the key was previously taken,
        // so the ObservableList is alerted
        if (containsSerialNumber(item.getSerialNumber())) {
            itemMap.remove(item.getSerialNumber());
        }
        itemMap.put(item.getSerialNumber(), item);
    }

    public void deleteItem(Item item) {
        itemMap.remove(item.getSerialNumber());
    }

    public void loadInventory() {
        if (!isBound()) return;

        itemMap.clear();
        itemMap.putAll(
                dataAccessObject.getItems().stream().collect(
                        Collectors.toMap(Item::getSerialNumber, Function.identity()))
        );
    }

    public void saveInventory() {
        if (!isBound()) return;
        dataAccessObject.saveItems(this.itemList);
    }

    public void deleteInventory() {
        dataAccessObject.delete();
        closeInventory();
    }

    public void closeInventory() {
        itemMap.clear();
        unbind();
    }

    public boolean containsSerialNumber(String serialNumber) {
        return itemMap.containsKey(serialNumber);
    }

    // NOTE: Pushes the item to the end of the list if in an unsorted state
    // Set over Map to prevent duplicates might be better, but that
    // comes with the issue of locating Items by their Serial Number
    // being time consuming. (Cant be in another thread either because it
    // is used when the item needs to be displayed to the user immediately)
    // Alternatively, Some way to ensure this operation inserts the item back
    // where it was could be a solution. Either way, this would need to be changed
    public void updateSerialNumber(String oldSerialNumber, String newSerialNumber) {
        Item item = itemMap.remove(oldSerialNumber);
        item.setSerialNumber(newSerialNumber);
        itemMap.put(newSerialNumber, item);
    }

    public Item getItemBySerialNumber(String serialNumber) {
        return itemMap.get(serialNumber);
    }

    public void bindFile(Path path) {
        dataAccessObject = new InventoryFileDAO(path);
    }

    public void unbind() {
        dataAccessObject = null;
    }

    public boolean isBound() {
        return dataAccessObject != null;
    }

}
