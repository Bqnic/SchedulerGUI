package gui.schedulergui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utilites.Priority;
import utilites.Task;

import java.time.LocalDate;

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
   private Label taskDetails;

    private GUI gui;

    @FXML
    private void initialize(){
        //initialization of the columns in tableview
        creationColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().dateOfCreationProperty());
        nameColumn.setCellValueFactory(taskStringCellDataFeatures -> taskStringCellDataFeatures.getValue().nameProperty());
        deadlineColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().deadlineProperty());
        priorityColumn.setCellValueFactory(taskPriorityCellDataFeatures -> taskPriorityCellDataFeatures.getValue().priorityProperty());

        //initialization of the task description
        showTaskDetails(null);
        taskTable.getSelectionModel().selectedItemProperty().addListener((observableValue, task, t1) -> showTaskDetails(t1));
    }

    @FXML
    private void deleteTask(){
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0)
            taskTable.getItems().remove(selectedIndex);
    }

    @FXML
    public void showTaskDetails(Task task){
        if (task != null)
            taskDetails.setText(task.getDetails());

        else{
            taskDetails.setText("");
        }

        taskDetails.setWrapText(true);
    }

    @FXML
    private void editTask(){
        Task task = taskTable.getSelectionModel().getSelectedItem();
        if (task != null) {
            gui.showEditTaskOverview(task, false);
            showTaskDetails(task);
        }
    }

    @FXML
    private void newTask(){
        Task task = new Task();
        gui.showEditTaskOverview(task, true);
        taskTable.setItems(gui.getTasks());
    }

    public void setGUI (GUI gui){
        this.gui = gui;

        taskTable.setItems(gui.getTasks());
    }
}