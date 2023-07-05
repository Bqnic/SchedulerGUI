package gui.schedulergui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import utilites.Priority;
import utilites.Task;

import java.time.LocalDate;
import java.util.Optional;

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
   @FXML
   private Label taskName;

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