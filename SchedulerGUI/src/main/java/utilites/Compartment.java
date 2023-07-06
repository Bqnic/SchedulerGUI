package utilites;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Compartment {
    private final StringProperty compartmentName;

    public Compartment(String compartmentName) {
        this.compartmentName = new SimpleStringProperty(compartmentName);
    }

    public String getCompartmentName() {
        return compartmentName.get();
    }

    public StringProperty compartmentNameProperty() {
        return compartmentName;
    }

    public void setCompartmentName(String compartmentName) {
        this.compartmentName.set(compartmentName);
    }

    @Override
    public String toString() {
        return getCompartmentName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compartment that = (Compartment) o;
        return Objects.equals(compartmentName.get(), that.compartmentName.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(compartmentName);
    }
}
