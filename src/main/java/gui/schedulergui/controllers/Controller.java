package gui.schedulergui.controllers;

import gui.schedulergui.GUI;
import gui.schedulergui.utilities.Priority;
import gui.schedulergui.utilities.Task;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.WindowEvent;

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
    private TableView<String> compartmentTable;
    @FXML
    private TableColumn<String, String> compartmentNameColumn;

    @FXML
    private Label taskDetails;
    @FXML
    private Label taskName;

    private ObservableList<Task> completedTasks = FXCollections.observableArrayList();
    private GUI gui;
    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private String currentCompartment;
    private boolean saved = false;

    @FXML
    private void initialize(){
        //initialization of the columns in tableview of tasks
        creationColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().dateOfCreationProperty());
        nameColumn.setCellValueFactory(taskStringCellDataFeatures -> taskStringCellDataFeatures.getValue().nameProperty());
        deadlineColumn.setCellValueFactory(taskLocalDateCellDataFeatures -> taskLocalDateCellDataFeatures.getValue().deadlineProperty());
        priorityColumn.setCellValueFactory(taskPriorityCellDataFeatures -> taskPriorityCellDataFeatures.getValue().priorityProperty());

        //initialization of the column in tableview of compartments
        compartmentNameColumn.setCellValueFactory(stringStringCellDataFeatures -> new SimpleStringProperty(stringStringCellDataFeatures.getValue()));

        //initialization of the task description
        showTaskDetails(null);
        taskTable.getSelectionModel().selectedItemProperty().addListener((observableValue, task, t1) -> showTaskDetails(t1));

        //initialization of different table views based on the selected compartment
        compartmentTable.getSelectionModel().selectedItemProperty().addListener((observableValue, compartment, t1) -> filteredTableView(t1));

        //right-clicking a row in table view
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Edit");

        contextMenu.getItems().addAll(deleteItem, editItem);

        taskTable.setRowFactory(taskTableView -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    showContextMenu(contextMenu, mouseEvent);

                    deleteItem.setOnAction(e -> deleteTask());
                    editItem.setOnAction(e -> editTask());
                }
            });

            return row;
        });

        //enabling key combinations for shortcuts on tasks (deleting etc.)
        taskTable.setOnKeyPressed(this::KeyCombinationsForTaskTable);

        //enabling key combinations for shortcuts on compartments
        compartmentTable.setOnKeyPressed(this::KeyCombinationsForCompartmentTable);
    }

    //method that adds key combinations to GUI (primarily, so I can delete with "Delete" button lol)
    private void KeyCombinationsForTaskTable(KeyEvent event){
        KeyCodeCombination deleteCombination = new KeyCodeCombination(KeyCode.DELETE);
        if (deleteCombination.match(event))
            deleteTask();

        KeyCodeCombination newTaskCombination = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        if (newTaskCombination.match(event))
            newTask();

        KeyCodeCombination editTaskCombination = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
        if (editTaskCombination.match(event))
            editTask();

        KeyCodeCombination completeTaskCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        if (completeTaskCombination.match(event))
            completeTask();

        KeyCodeCombination savingEverything = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (savingEverything.match(event))
            saveEverything();
    }

    private void KeyCombinationsForCompartmentTable(KeyEvent event){
        KeyCodeCombination deleteCombination = new KeyCodeCombination(KeyCode.DELETE);
        if (deleteCombination.match(event))
            deleteCompartment();

        KeyCodeCombination newTaskCombination = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        if (newTaskCombination.match(event))
            setNewCompartmentMenuItem();
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

            if (buttonType.isPresent() && buttonType.get() == buttonTypeYes) {
                Task taskToRemove = taskTable.getItems().get(selectedIndex);
                taskTable.getItems().remove(taskToRemove);
                allTasks.remove(taskToRemove);
                saved = false;
            }
        }

        //If user pressed "Delete" and didn't choose a task element, then check if he chose a compartment element
        else deleteCompartment();
    }

    @FXML
    private void completeTask(){
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setHeaderText("Are you sure you completed this task?");
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.isPresent() && answer.get() == yesButton){
                Task taskToComplete = taskTable.getItems().get(selectedIndex);
                taskTable.getItems().remove(taskToComplete);
                allTasks.remove(taskToComplete);
                completedTasks.add(taskToComplete);
                saved = false;
            }
        }
    }

    @FXML
    private void deleteCompartment(){
        int selectedIndex = compartmentTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0){

            if (Objects.equals(compartmentTable.getItems().get(selectedIndex), "All tasks")){
                Alert cantDoThat = new Alert(Alert.AlertType.ERROR);
                cantDoThat.setTitle("Cannot do this");
                cantDoThat.setHeaderText("You cannot delete the all tasks compartment.");
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

            if (buttonType.isPresent() && buttonType.get() == buttonTypeYes) {
                String compartmentName = compartmentTable.getItems().get(selectedIndex);

                allTasks = allTasks.stream()
                        .filter(task -> !Objects.equals(task.getCompartment(), compartmentName))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                compartmentTable.getItems().remove(selectedIndex);
                saved = false;
            }

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
    private void showCompletedTasks(){
        gui.showCompletedTasksDialog(completedTasks);
    }

    @FXML
    private void editTask(){
        Task task = taskTable.getSelectionModel().getSelectedItem();
        if (task != null) {
            gui.showEditTaskOverview(task, false);
            filteredTableView(currentCompartment);
            showTaskDetails(task);
            saved = false;
        }
    }

    @FXML
    private void newTask(){
        Task task = new Task();
        Task newTask = gui.showEditTaskOverview(task, true);
        if (newTask != null) {
            allTasks.add(newTask);
            saved = false;
            //if im currently inside the compartment in which I added a task, then I need to update it immediately
            if (Objects.equals(newTask.getCompartment(), currentCompartment) || Objects.equals(currentCompartment, "All tasks")) {
                filteredTableView(currentCompartment);
            }
        }
    }

    @FXML
    private void setNewCompartmentMenuItem(){
        gui.showEditCompartmentDialog();
    }

    //different table views for different compartments
    @FXML
    private void filteredTableView(String compartmentName){
        ObservableList<Task> items;

        if (Objects.equals(compartmentName, "All tasks")){
            items = allTasks.stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        else {
             items = allTasks.stream()
                            .filter(item -> item.getCompartment().equals(compartmentName))
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        taskTable.setItems(items);
        currentCompartment = compartmentName;
    }

    @FXML
    private void saveEverything(){
        saved = true;
        gui.saveTasks(allTasks);
        gui.saveCompartments();
        gui.saveCompletedTasks();
    }

    public void setGUI (GUI gui){
        this.gui = gui;

        allTasks.addAll(gui.getTasks());
        compartmentTable.setItems(gui.getCompartments());

        //Activates when user tries to exit the app
        gui.primaryStage.setOnCloseRequest(e -> savingTasks(e));
    }

    //dynamically changes the position of context menu (in case of window resizing)
    private void showContextMenu(ContextMenu contextMenu, MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            contextMenu.setOnShowing(e -> {
                double x = event.getScreenX();
                double y = event.getScreenY();
                contextMenu.show(taskTable, x, y);
            });
            contextMenu.show(taskTable, event.getScreenX(), event.getScreenY());
        }
    }

    public void savingTasks(WindowEvent e){
        if (!saved) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit without saving?");
            alert.setHeaderText("Save before exiting?");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.OK) {
                gui.saveTasks(allTasks);
                gui.saveCompartments();
                gui.saveCompletedTasks();
                Platform.exit();
            }

            else e.consume();

        }
    }

    public void setCompletedTasks(ObservableList<Task> completedTasks){
        this.completedTasks = completedTasks;
    }
}