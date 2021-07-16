package ucf.assignments.model;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import java.math.BigDecimal;

public class Item {

    StringProperty name = new SimpleStringProperty();
    StringProperty serialNumber = new SimpleStringProperty();
    ObjectProperty<BigDecimal> value = new SimpleObjectProperty<>();

    public Item(String name, String serialNumber, double value) {
        this(name, serialNumber, BigDecimal.valueOf(value));
    }

    public Item(String name, String serialNumber, BigDecimal value) {
        if(validate(name, serialNumber, value)) {
            this.setName(name);
            this.setSerialNumber(serialNumber);
            this.setValue(value);
        }
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public StringProperty serialNumberProperty() {
        return serialNumber;
    }

    public BigDecimal getValue() {
        return value.get();
    }

    public void setValue(BigDecimal value) {
        this.value.set(value);
    }

    public double getValueAsDouble() {
        return value.get().doubleValue();
    }

    public ObjectProperty<BigDecimal> valueProperty() {
        return value;
    }

    public void setValueFromDouble(double value) {
        this.value.set(BigDecimal.valueOf(value));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (name == null ? 0 : this.getName().hashCode());
        hash = 31 * hash + (serialNumber == null ? 0 : this.getSerialNumber().hashCode());
        hash = 31 * hash + (value == null ? 0 : this.getValue().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Item i)) return false;
        return this.getName().equals(i.getName())
                && this.getSerialNumber().equals(i.getSerialNumber())
                && this.getValue().equals(i.getValue());
    }

    @Override
    public String toString() {
        return "Item{" +
                "name=" + this.getName() +
                "serial_number=" + this.getSerialNumber() +
                "value=" + this.getValueAsDouble();
    }

    private boolean validate(String name, String serialNumber, BigDecimal value) {
        if (name == null) {
            throw new NullPointerException(
                    "name cannot be null");
        } else if (serialNumber == null) {
            throw new NullPointerException(
                    "serialNumber cannot be null");
        } else if (value == null) {
            throw new NullPointerException(
                    "value cannot be null");
        } else if (name.length() < 2 || name.length() > 256) {
            throw new IllegalArgumentException(
                    "name must be between 2 and 256 characters");
        } else if (!serialNumber.matches("[A-Za-z0-9]{10}")) {
            throw new IllegalArgumentException(
                    "serial number must contain exactly 10 letter or digit characters");
        } else {
            return true;
        }
    }

    public static Callback<Item, Observable[]> extractor() {
        return (Item item) -> new Observable[]{
                item.nameProperty(),
                item.serialNumberProperty(),
                item.valueProperty()
        };
    }

}
