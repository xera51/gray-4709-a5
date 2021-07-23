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

// TODO potentially make key change if sn changed (currently controller is doing it - should not be)
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
        itemMap.addListener((MapChangeListener<String, Item>) change -> {
            if(change.wasAdded()) {
                itemList.add(change.getValueAdded());
            } else if(change.wasRemoved()) {
                itemList.remove(change.getValueRemoved());
            }
        });
    }

    public SortedList<Item> getList() {
        return sortedItemList;
    }

    public void add(Item item) {
        // We must remove an item if the key was previously taken,
        // so the ObservableList is alerted
        if (containsSerialNumber(item.getSerialNumber())) {
            itemMap.remove(item.getSerialNumber());
        }
        itemMap.put(item.getSerialNumber(), item);
    }

    public void remove(Item item) {
        itemMap.remove(item.getSerialNumber());
    }

    public void edit(Item oldItem, Item newItem) {
        if(oldItem.getSerialNumber().equals(newItem.getSerialNumber())) {
            itemMap.replace(newItem.getSerialNumber(), newItem);
        } else {
            if (containsSerialNumber(newItem.getSerialNumber())) {
                itemMap.remove(newItem.getSerialNumber());
            }
            itemMap.put(newItem.getSerialNumber(), newItem);
            itemMap.remove(oldItem.getSerialNumber());
        }
    }

    public void setFilter(String name, String serialNumber) {
        filteredItemList.setPredicate(item ->
                item.getName().toLowerCase().contains(name.toLowerCase())
                        && item.getSerialNumber().contains(serialNumber));
    }

    public boolean containsSerialNumber(String serialNumber) {
        return itemMap.containsKey(serialNumber);
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

    public void save() {
        dataAccessObject.saveItems(this.itemList);
    }

    public void load() {
        itemMap.clear();
        itemMap.putAll(
            dataAccessObject.getItems().stream().collect(
                Collectors.toMap(Item::getSerialNumber, Function.identity()))
        );
    }

    public void close() {
        itemMap.clear();
        dataAccessObject = null;
    }

    public void delete() {
        dataAccessObject.delete();
        close();
    }
}
