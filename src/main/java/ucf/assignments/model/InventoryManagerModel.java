package ucf.assignments.model;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.function.Function;
import java.util.stream.Collectors;

// TODO potentially make key change if sn changed (currently controller is doing it - should not be)
public class InventoryManagerModel {
    private final ObservableMap<String, Item> itemMap = FXCollections.observableHashMap();
    private final ObservableList<Item> itemList = FXCollections.observableArrayList(Item.extractor());
    private final FilteredList<Item> filteredItemList = new FilteredList<>(itemList);
    private final SortedList<Item> sortedItemList = new SortedList<>(filteredItemList);

    private InventoryDAO dao;

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

    public boolean add(Item item) {
        return itemMap.putIfAbsent(item.getSerialNumber(), item) == null;
    }

    public boolean edit(Item oldItem, Item newItem) {
        if(oldItem.getSerialNumber().equals(newItem.getSerialNumber())) {
            itemMap.replace(newItem.getSerialNumber(), newItem);
            return true;
        } else
            if (this.add(newItem)) {
                itemMap.remove(oldItem.getSerialNumber());
                return true;
            } else {
                return false;
        }
    }

    // TODO return boolean indicating success/failure
    public void remove(Item item) {
        itemMap.remove(item.getSerialNumber());
    }

    public void setFilter(String name, String serialNumber) {
        filteredItemList.setPredicate(item ->
                item.getName().toLowerCase().contains(name.toLowerCase())
                        && item.getSerialNumber().toLowerCase().contains(serialNumber.toLowerCase()));
    }

    public ObservableMap<String, Item> getMap() {
        return itemMap;
    }

    public boolean containsSerialNumber(String serialNumber) {
        return itemMap.containsKey(serialNumber);
    }

    public void setDao(InventoryDAO dao) {
        this.dao = dao;
    }

    public InventoryDAO getDao() {
        return dao;
    }

    public void save() {
        dao.saveItems(this.itemList);
    }

    public void load() {
        itemMap.clear();
        itemMap.putAll(
            dao.getItems().stream().collect(
                Collectors.toMap(Item::getSerialNumber, Function.identity()))
        );
    }

    public void close() {
        itemMap.clear();
        dao = null;
    }

    public void delete() {
        dao.delete();
        dao = null;
    }
}
