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

public class InventoryHTMLSerializer implements Serializer {

    private static final String header = """
            <table>
                <tr>
                    <th>Name</th>
                    <th>Serial Number</th>
                    <th>Value</th>
                </tr>""" + "\n";

    @Override
    public Collection<Item> from(String text) {
        List<Item> items = new ArrayList<>();

        if (!text.isEmpty()) {
            String[] rows = text.replace("\r", "")
                    .replace(header, "")
                    .replace("</table>\n", "")
                    .replace("\t<tr>\n", "")
                    .split("\t</tr>\n");
            for (String row : rows) {
                items.add(htmlTableDataToItem(row));
            }
        }

        return items;
    }

    @Override
    public String to(Collection<Item> items) {
        StringBuilder builder = new StringBuilder(header);
        for (Item item : items) {
            builder.append("\t<tr>\n")
                    .append(itemToHtmlTableData(item))
                    .append("\t</tr>\n");
        }
        builder.append("</table>\n");
        return builder.toString();
    }

    private Item htmlTableDataToItem(String row) {
        List<String> fields = Arrays.stream(row.split("\n"))
                .map(s -> s.replace("\t\t<td>", "").replace("</td>", ""))
                .collect(Collectors.toList());
        return new Item(fields.get(0), fields.get(1), new BigDecimal(fields.get(2)));
    }

    private String itemToHtmlTableData(Item item) {
        return "\t\t<td>" + item.getName() + "</td>\n" +
                "\t\t<td>" + item.getSerialNumber() + "</td>\n" +
                "\t\t<td>" + item.getValue() + "</td>\n";
    }
}
