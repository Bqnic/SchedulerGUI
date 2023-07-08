package gui.schedulergui.controllers;

import gui.schedulergui.utilities.Priority;
import gui.schedulergui.utilities.Task;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class CompletedTasksDialog {
    @FXML
    private TableView<Task> completedTaskTable;
    @FXML
    private TableColumn<Task, LocalDate> creationColumn;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Task, Priority> priorityColumn;

    @FXML
    private Label numberOfCompletedTasks;


    @FXML
    private void initialize(){
        //initialization of the columns in tableview of tasks
        creationColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().dateOfCreationProperty());
        nameColumn.setCellValueFactory(taskStringCellDataFeatures -> taskStringCellDataFeatures.getValue().nameProperty());
        deadlineColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().deadlineProperty());
        priorityColumn.setCellValueFactory(taskPriorityCellDataFeatures -> taskPriorityCellDataFeatures.getValue().priorityProperty());
    }

    public void showCompletedTasks(ObservableList<Task> completedTasks) {
        completedTaskTable.setItems(completedTasks);
        numberOfCompletedTasks.setText(String.valueOf(completedTasks.size()));
    }


}
