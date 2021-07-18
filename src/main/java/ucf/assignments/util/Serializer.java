/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.util;

import ucf.assignments.model.Item;

import java.util.Collection;

// Valid formatting is assumed for now
public interface Serializer {

    Collection<Item> from(String text);

    String to(Collection<Item> items);
}
