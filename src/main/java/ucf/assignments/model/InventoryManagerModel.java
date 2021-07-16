package ucf.assignments.model;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class InventoryManagerModel {
    private final ObservableMap<String, Item> itemMap = FXCollections.observableHashMap();
    private final ObservableList<Item> itemList = FXCollections.observableArrayList(Item.extractor());
    private final FilteredList<Item> filteredItemList = new FilteredList<>(itemList);
    private final SortedList<Item> sortedItemList = new SortedList<>(filteredItemList);

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



}
