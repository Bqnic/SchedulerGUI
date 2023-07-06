package gui.schedulergui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loader.CompartmentLoader;
import loader.TaskLoader;
import utilites.Compartment;
import utilites.Task;

import java.io.IOException;

public class GUI extends Application {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ObservableList<Compartment> compartments = FXCollections.observableArrayList();

    public GUI() {
        tasks = TaskLoader.loadTasks();
        compartments = CompartmentLoader.loadCompartments();
    }

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("SchedulerGUI");

        showTasksOverview();
    }

    public void showTasksOverview(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("mainGUI.fxml"));
            BorderPane taskOverview = loader.load();

            Scene scene = new Scene(taskOverview);
            primaryStage.setScene(scene);
            primaryStage.show();

            Controller controller = loader.getController();
            controller.setGUI(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Task showEditTaskOverview(Task task, boolean newTask){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("TaskEditDialog.fxml"));
            AnchorPane editTask = loader.load();

            //Creating dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editTask);
            dialogStage.setScene(scene);

            //Giving task to controller
            TaskEditDialogController controller = loader.getController();
            controller.setGUI(this);
            controller.setDialogStage(dialogStage);
            controller.setTask(task);

            dialogStage.showAndWait();

            //if it's a new task and the input is alright, add it to the taskTable
            if (newTask && controller.isOkInput()){
                Task t = controller.getTask();
                tasks.add(t);
                return t;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void showEditCompartmentDialog(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CompartmentEditDialog.fxml"));
            BorderPane editCompartment = loader.load();

            //Creating dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Compartment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editCompartment);
            dialogStage.setScene(scene);

            //Giving compartment to controller
            CompartmentEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkInput()) {
                Compartment compartment = controller.getCompartment();
                if (!compartments.contains(compartment))
                    compartments.add(compartment);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public ObservableList<Compartment> getCompartments() {
        return compartments;
    }

    public static void main(String[] args) {
        launch();
    }
}