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
import main.TaskLoader;
import utilites.Task;

import java.io.IOException;

public class GUI extends Application {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    public GUI(){
        tasks = TaskLoader.loadTasks();
    }

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("SchedulerGUI");

        //initRootLayout();
        showTasksOverview();
    }

    /*public void initRootLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void showTasksOverview(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("hello-view.fxml"));
            AnchorPane taskOverview = loader.load();

            Scene scene = new Scene(taskOverview);
            primaryStage.setScene(scene);
            primaryStage.show();
            //rootLayout.setCenter(taskOverview);

            Controller controller = loader.getController();
            controller.setGUI(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showEditTaskOverview(Task task, boolean newTask){
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
            controller.setDialogStage(dialogStage);
            controller.setTask(task);

            //show
            dialogStage.showAndWait();

            //if it's a new task and the input is alright, add it to the taskTable
            if (newTask && controller.isOkInput()){
                Task t = controller.getTask();
                tasks.add(t);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public static void main(String[] args) {
        launch();
    }
}