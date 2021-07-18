/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.util;

import ucf.assignments.model.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryTSVSerializer implements Serializer {

    @Override
    public Collection<Item> from(String text) {
        List<Item> items = new ArrayList<>();

        if (!text.isEmpty()) {
            List<String> lines = Arrays.stream(text.replace("\r", "").split("\n"))
                    .collect(Collectors.toList());
            for (String line : lines) {
                String[] fields = line.split("\t");
                items.add(new Item(fields[0], fields[1], new BigDecimal(fields[2])));
            }
        }
        return items;
    }

    @Override
    public String to(Collection<Item> items) {
        StringBuilder builder = new StringBuilder();
        for (Item item : items) {
            builder.append(item.getName())
                    .append("\t")
                    .append(item.getSerialNumber())
                    .append("\t")
                    .append(item.getValue())
                    .append("\n");
        }
        return builder.toString();
    }
}
