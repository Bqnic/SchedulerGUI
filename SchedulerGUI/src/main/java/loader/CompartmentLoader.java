package loader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilites.Compartment;


public class CompartmentLoader {
    public static ObservableList<Compartment> loadCompartments() {
        ObservableList<Compartment> compartments = FXCollections.observableArrayList();
        compartments.add(new Compartment("GENERAL"));
        compartments.add(new Compartment("FIZIKA"));
        compartments.add(new Compartment("OOP"));
        compartments.add(new Compartment("MATAN2"));
        compartments.add(new Compartment("OE"));

        return compartments;
    }
}
