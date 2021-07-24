/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.model;

import ucf.assignments.util.InventoryHTMLSerializer;
import ucf.assignments.util.InventoryJSONSerializer;
import ucf.assignments.util.InventoryTSVSerializer;
import ucf.assignments.util.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

// TODO what to do with exceptions?
public class InventoryFileDAO implements InventoryDAO {

    private final Serializer serializer;
    private final Path path;

    public InventoryFileDAO(Path path) {
        this.serializer = createSerializer(path);
        this.path = path;
    }

    @Override
    public void create() {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Item> getItems() {
        try {
            return serializer.from(Files.readString(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveItems(Collection<Item> items) {
        try {
            Files.write(path, Arrays.asList(serializer.to(items).split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Serializer createSerializer(Path path) {
        String fileName = path.toString().toLowerCase();
        if (fileName.endsWith(".json")) {
            return new InventoryJSONSerializer();
        } else if (fileName.endsWith(".html")) {
            return new InventoryHTMLSerializer();
        } else if (fileName.endsWith(".txt")) {
            return new InventoryTSVSerializer();
        } else {
            throw new IllegalArgumentException("Unsupported file type");
        }
    }
}
