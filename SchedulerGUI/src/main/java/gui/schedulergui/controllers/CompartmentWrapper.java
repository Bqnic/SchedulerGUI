package gui.schedulergui.controllers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "compartments")
public class CompartmentWrapper {
    private List<String> compartments;
    @XmlElement(name = "compartment")
    public List<String> getCompartments() {
        return compartments;
    }

    public void setCompartments(List<String> compartments) {
        this.compartments = compartments;
    }
}
