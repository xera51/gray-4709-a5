package ucf.assignments.model;

import java.util.Collection;

public interface InventoryDAO {

    void create();

    Collection<Item> getItems();

    void saveItems(Collection<Item> items);

    void delete();
}
