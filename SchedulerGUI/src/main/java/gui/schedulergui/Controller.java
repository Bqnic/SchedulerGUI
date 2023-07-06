package gui.schedulergui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utilites.Compartment;
import utilites.Priority;
import utilites.Task;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, LocalDate> creationColumn;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Task, Priority> priorityColumn;

    @FXML
    private TableView<Compartment> compartmentTable;
    @FXML
    private TableColumn<Compartment, String> compartmentNameColumn;

    @FXML
    private Label taskDetails;
    @FXML
    private Label taskName;

    private GUI gui;
    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private String currentCompartment;

    @FXML
    private void initialize(){
        //initialization of the columns in tableview of tasks
        creationColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().dateOfCreationProperty());
        nameColumn.setCellValueFactory(taskStringCellDataFeatures -> taskStringCellDataFeatures.getValue().nameProperty());
        deadlineColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().deadlineProperty());
        priorityColumn.setCellValueFactory(taskPriorityCellDataFeatures -> taskPriorityCellDataFeatures.getValue().priorityProperty());

        //initialization of the column in tableview of compartments
        compartmentNameColumn.setCellValueFactory(compartmentStringCellDataFeatures -> compartmentStringCellDataFeatures.getValue().compartmentNameProperty());

        //initialization of the task description
        showTaskDetails(null);
        taskTable.getSelectionModel().selectedItemProperty().addListener((observableValue, task, t1) -> showTaskDetails(t1));

        //initialization of different table views based on the selected compartment
        compartmentTable.getSelectionModel().selectedItemProperty().addListener((observableValue, compartment, t1) -> filteredTableView(t1.getCompartmentName()));
    }

    @FXML
    private void deleteTask(){
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure you want to delete the selected task?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.isPresent() && buttonType.get() == buttonTypeYes)
                taskTable.getItems().remove(selectedIndex);
        }

        //If user pressed "Delete" and didn't choose a task element, then check if he chose a compartment element
        else
            deleteCompartment();
    }

    @FXML
    private void deleteCompartment(){
        int selectedIndex = compartmentTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0){

            if (Objects.equals(compartmentTable.getItems().get(selectedIndex).getCompartmentName(), "GENERAL")){
                Alert cantDoThat = new Alert(Alert.AlertType.ERROR);
                cantDoThat.setTitle("Cannot do this");
                cantDoThat.setHeaderText("You cannot delete the GENERAL compartment.");
                cantDoThat.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure you want to delete the selected compartment and all of it's tasks?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.isPresent() && buttonType.get() == buttonTypeYes)
                compartmentTable.getItems().remove(selectedIndex);

        }
    }

    @FXML
    public void showTaskDetails(Task task){
        if (task != null) {
            taskDetails.setText(task.getDetails());
            taskName.setText(task.getName());
        }
        else{
            taskDetails.setText("");
            taskName.setText("");
        }

        taskDetails.setWrapText(true);
        taskName.setWrapText(true);
    }

    @FXML
    private void editTask(){
        Task task = taskTable.getSelectionModel().getSelectedItem();
        if (task != null) {
            gui.showEditTaskOverview(task, false);
            filteredTableView(currentCompartment);
            showTaskDetails(task);
        }
    }

    @FXML
    private void newTask(){
        Task task = new Task();
        Task newTask = gui.showEditTaskOverview(task, true);
        allTasks.add(newTask);


        //if im currently inside the compartment in which I added a task, then I need to update it immediately
        if (Objects.equals(newTask.getCompartment().getCompartmentName(), currentCompartment)){
            filteredTableView(currentCompartment);
        }
    }

    @FXML
    private void setNewCompartmentMenuItem(){
        gui.showEditCompartmentDialog();
    }

    //different table views for different compartments
    @FXML
    private void filteredTableView(String compartmentName){
        ObservableList<Task> items =
                allTasks.stream()
                                .filter(item -> item.getCompartment().getCompartmentName().equals(compartmentName))
                                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

        taskTable.setItems(items);
        currentCompartment = compartmentName;
    }

    public void setGUI (GUI gui){
        this.gui = gui;

        allTasks.addAll(gui.getTasks());
        compartmentTable.setItems(gui.getCompartments());
    }
}