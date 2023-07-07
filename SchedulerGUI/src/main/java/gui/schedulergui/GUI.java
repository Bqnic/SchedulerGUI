package gui.schedulergui;

import gui.schedulergui.controllers.CompartmentEditDialogController;
import gui.schedulergui.controllers.CompartmentWrapper;
import gui.schedulergui.controllers.CompletedTasksDialog;
import gui.schedulergui.controllers.Controller;
import gui.schedulergui.utilities.Task;
import gui.schedulergui.utilities.TaskEditDialogController;
import gui.schedulergui.utilities.TaskWrapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.*;
import java.io.File;
import java.io.IOException;

public class GUI extends Application {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ObservableList<String> compartments = FXCollections.observableArrayList();
    private ObservableList<Task> completedTasks = FXCollections.observableArrayList();

    public GUI() {
        loadTasks();
        loadCompartments();
        loadCompletedTasks();
    }

    public Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("SchedulerGUI");
        this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

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
            controller.setCompletedTasks(completedTasks);

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
                String compartment = controller.getCompartment();
                if (!compartments.contains(compartment))
                    compartments.add(compartment);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showCompletedTasksDialog(ObservableList<Task> completedTasks){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CompletedTasksTableView.fxml"));
            AnchorPane tasks = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Completed tasks");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(tasks);
            dialogStage.setScene(scene);

            CompletedTasksDialog controller = loader.getController();
            controller.showCompletedTasks(completedTasks);

            dialogStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public ObservableList<String> getCompartments() {
        return compartments;
    }

    public void saveTasks(ObservableList<Task> itemsToSave){
        try {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/gui/schedulergui/saves/taskSaves.xml";
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //Wrapping to XML file
            TaskWrapper wrapper = new TaskWrapper();
            wrapper.setTasks(itemsToSave);

            m.marshal(wrapper, file);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTasks(){
        try{
            String filePath = System.getProperty("user.dir") + "/src/main/resources/gui/schedulergui/saves/taskSaves.xml";
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            if (file.length() != 0) {
                TaskWrapper wrapper = (TaskWrapper) um.unmarshal(file);

                if (wrapper.getTasks() != null)
                    tasks.addAll(wrapper.getTasks());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCompartments(){
        try{
            String filePath = System.getProperty("user.dir") + "/src/main/resources/gui/schedulergui/saves/compartmentSaves.xml";
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(CompartmentWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            if (file.length() != 0) {
                CompartmentWrapper wrapper = (CompartmentWrapper) um.unmarshal(file);

                if (wrapper.getCompartments() != null)
                    compartments.addAll(wrapper.getCompartments());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCompartments(){
        try{
            String filePath = System.getProperty("user.dir") + "/src/main/resources/gui/schedulergui/saves/compartmentSaves.xml";
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(CompartmentWrapper.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //Wrapping to XML file
            CompartmentWrapper wrapper = new CompartmentWrapper();
            wrapper.setCompartments(compartments);

            m.marshal(wrapper, file);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCompletedTasks(){
        try{
            String filePath = System.getProperty("user.dir") + "/src/main/resources/gui/schedulergui/saves/completedTasksSave.xml";
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskWrapper wrapper = new TaskWrapper();
            wrapper.setTasks(completedTasks);

            m.marshal(wrapper, file);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCompletedTasks(){
        try{
            String filePath = System.getProperty("user.dir") + "/src/main/resources/gui/schedulergui/saves/completedTasksSave.xml";
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            if (file.length() != 0) {
                TaskWrapper wrapper = (TaskWrapper) um.unmarshal(file);

                if (wrapper.getTasks() != null)
                    completedTasks.addAll(wrapper.getTasks());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}