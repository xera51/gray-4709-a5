/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Christopher Gray
 */

package ucf.assignments.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ucf.assignments.model.Item;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class InventoryJSONSerializer implements Serializer {

    private static final Type BIG_DECIMAL_PROPERTY_TYPE = new TypeToken<ObjectProperty<BigDecimal>>() {
    }.getType();
    private static final Type COLLECTION_TYPE = new TypeToken<ArrayList<Item>>() {
    }.getType();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(SimpleStringProperty.class, new StringPropertySerializer())
            .registerTypeAdapter(BIG_DECIMAL_PROPERTY_TYPE, new BigDecimalPropertySerializer())
            .registerTypeAdapter(StringProperty.class, new StringPropertyDeserializer())
            .registerTypeAdapter(BIG_DECIMAL_PROPERTY_TYPE, new BigDecimalPropertyDeserializer())
            .create();

    @Override
    public Collection<Item> from(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        else return GSON.fromJson(text, COLLECTION_TYPE);
    }

    @Override
    public String to(Collection<Item> items) {
        return GSON.toJson(items);
    }


    private static class StringPropertySerializer implements JsonSerializer<SimpleStringProperty> {
        public JsonElement serialize(SimpleStringProperty src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getValue());
        }
    }

    private static class BigDecimalPropertySerializer implements JsonSerializer<ObjectProperty<BigDecimal>> {
        public JsonElement serialize(ObjectProperty<BigDecimal> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getValue().toString());
        }
    }

    private static class StringPropertyDeserializer implements JsonDeserializer<StringProperty> {
        public StringProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new SimpleStringProperty(json.getAsJsonPrimitive().getAsString());
        }
    }

    private static class BigDecimalPropertyDeserializer implements JsonDeserializer<ObjectProperty<BigDecimal>> {
        public ObjectProperty<BigDecimal> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new SimpleObjectProperty<>(json.getAsJsonPrimitive().getAsBigDecimal());
        }
    }
}
