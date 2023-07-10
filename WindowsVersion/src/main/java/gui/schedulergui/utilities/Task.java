package gui.schedulergui.utilities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Objects;

public class Task implements Comparable<Task>{

    private final StringProperty name;
    private final ObjectProperty<Priority> priority;
    private final ObjectProperty<LocalDate> dateOfCreation;
    private final ObjectProperty<LocalDate> deadline;
    private final StringProperty details;
    private final StringProperty compartment;

    public Task(String name, Priority priority, LocalDate dateOfCreation, LocalDate deadline) {
        StringProperty nameProperty = new SimpleStringProperty(name);
        ObjectProperty<Priority> priorityObjectProperty = new SimpleObjectProperty<>(priority);
        ObjectProperty<LocalDate> dateObjectProperty = new SimpleObjectProperty<>(dateOfCreation);
        ObjectProperty<LocalDate> deadlineProperty = new SimpleObjectProperty<>(deadline);

        this.name = nameProperty;
        this.priority = priorityObjectProperty;
        this.dateOfCreation = dateObjectProperty;
        this.deadline = deadlineProperty;
        this.details = new SimpleStringProperty("No details for this task.");
        this.compartment = new SimpleStringProperty("All tasks");
    }

    public Task(){
        this.name = new SimpleStringProperty("");
        this.priority = new SimpleObjectProperty<>(Priority.NONE);
        this.dateOfCreation = new SimpleObjectProperty<>(LocalDate.now());
        this.deadline = new SimpleObjectProperty<>(LocalDate.now());
        this.details = new SimpleStringProperty("");
        this.compartment = new SimpleStringProperty("All tasks");
    }

    public Task(String name, Priority priority, LocalDate dateOfCreation, LocalDate deadline, String details) {
        StringProperty nameProperty = new SimpleStringProperty(name);
        ObjectProperty<Priority> priorityObjectProperty = new SimpleObjectProperty<>(priority);
        ObjectProperty<LocalDate> dateObjectProperty = new SimpleObjectProperty<>(dateOfCreation);
        ObjectProperty<LocalDate> deadlineProperty = new SimpleObjectProperty<>(deadline);
        StringProperty detailsProperty = new SimpleStringProperty(details);

        this.name = nameProperty;
        this.priority = priorityObjectProperty;
        this.dateOfCreation = dateObjectProperty;
        this.deadline = deadlineProperty;
        this.details = detailsProperty;
        this.compartment = new SimpleStringProperty("All tasks");
    }

    public Task(String name, Priority priority, LocalDate dateOfCreation, LocalDate deadline, String details, String compartment) {
        StringProperty nameProperty = new SimpleStringProperty(name);
        ObjectProperty<Priority> priorityObjectProperty = new SimpleObjectProperty<>(priority);
        ObjectProperty<LocalDate> dateObjectProperty = new SimpleObjectProperty<>(dateOfCreation);
        ObjectProperty<LocalDate> deadlineProperty = new SimpleObjectProperty<>(deadline);
        StringProperty detailsProperty = new SimpleStringProperty(details);
        StringProperty compartmentProperty = new SimpleStringProperty(compartment);

        this.name = nameProperty;
        this.priority = priorityObjectProperty;
        this.dateOfCreation = dateObjectProperty;
        this.deadline = deadlineProperty;
        this.details = detailsProperty;
        this.compartment = compartmentProperty;
    }

    public String getCompartment() {
        return compartment.get();
    }

    public StringProperty compartmentProperty() {
        return compartment;
    }

    public void setCompartment(String compartment) {
        this.compartment.set(compartment);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDetails() {
        return details.get();
    }

    public StringProperty detailsProperty() {
        return details;
    }

    public void setDetails(String details) {
        this.details.set(details);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Priority getPriority() {
        return priority.get();
    }

    public ObjectProperty<Priority> priorityProperty() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority.set(priority);
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getDateOfCreation() {
        return dateOfCreation.get();
    }

    public ObjectProperty<LocalDate> dateOfCreationProperty() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation.set(dateOfCreation);
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getDeadline() {
        return deadline.get();
    }

    public ObjectProperty<LocalDate> deadlineProperty() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline.set(deadline);
    }

    @Override
    public String toString() {
        return dateOfCreation + ":" + name + ":" + deadline + ":" + priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && priority == task.priority && Objects.equals(dateOfCreation, task.dateOfCreation) && Objects.equals(deadline, task.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority, dateOfCreation, deadline);
    }

    @Override
    public int compareTo(Task task) {
        int byPriority =  this.getPriority().compareTo(task.getPriority());
        if (byPriority != 0)
            return byPriority;

        int byDateOfCreation = this.getDateOfCreation().compareTo(task.getDateOfCreation());
        if (byDateOfCreation != 0)
            return byDateOfCreation;

        int byDeadline = this.getDeadline().compareTo(task.getDeadline());
        if (byDeadline != 0)
            return byDeadline;

        return this.getName().compareTo(task.getName());
    }
}
