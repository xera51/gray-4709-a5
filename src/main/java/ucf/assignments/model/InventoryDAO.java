/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.model;

import java.util.Collection;

public interface InventoryDAO {

    void create();

    Collection<Item> getItems();

    void saveItems(Collection<Item> items);

    void delete();
}
