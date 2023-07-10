package gui.schedulergui;

import gui.schedulergui.controllers.CompartmentEditDialogController;
import gui.schedulergui.controllers.CompletedTasksDialog;
import gui.schedulergui.controllers.Controller;
import gui.schedulergui.controllers.TaskEditDialogController;
import gui.schedulergui.utilities.CompartmentWrapper;
import gui.schedulergui.utilities.Task;
import gui.schedulergui.utilities.TaskWrapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public void saveTasks(ObservableList<Task> itemsToSave) {
        try {
            String filePath = "taskSaves.xml";
            Path savePath = getSavePath(filePath);

            JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping to XML file
            TaskWrapper wrapper = new TaskWrapper();
            wrapper.setTasks(itemsToSave);

            marshaller.marshal(wrapper, savePath.toFile());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTasks() {
        try {
            String filePath = "taskSaves.xml";
            Path savePath = getSavePath(filePath);

            if (Files.exists(savePath)) {
                JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                TaskWrapper wrapper = (TaskWrapper) unmarshaller.unmarshal(savePath.toFile());

                if (wrapper.getTasks() != null) {
                    tasks.addAll(wrapper.getTasks());
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getSavePath(String fileName) {
        Path saveDir;
        try {
            saveDir = getSaveDirectory();
            return saveDir.resolve(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create save directory", e);
        }
    }

    private Path getSaveDirectory() throws IOException {
        String userHome = System.getProperty("user.home");
        Path saveDir = Path.of(userHome, "SchedulerGUI", "saves");
        Files.createDirectories(saveDir);

        createFileIfNotExists("compartmentSaves.xml", saveDir);
        createFileIfNotExists("taskSaves.xml", saveDir);
        createFileIfNotExists("completedTasksSave.xml", saveDir);

        return saveDir;
    }

    private void createFileIfNotExists(String fileName, Path directory) throws IOException {
        Path filePath = directory.resolve(fileName);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);

            // Write initial content to the file
            switch (fileName) {
                case "compartmentSaves.xml" -> writeInitialCompartmentContent(filePath);
                case "taskSaves.xml" -> writeInitialTaskContent(filePath);
                case "completedTasksSave.xml" -> writeInitialCompletedTasksContent(filePath);
            }
        }
    }

    private void writeInitialCompartmentContent(Path filePath) throws IOException {
        // Write your initial compartment content to the file
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<compartments>\n" +
                "    <compartment>All tasks</compartment>\n" +
                "</compartments>\n";

        Files.writeString(filePath, content);
    }

    private void writeInitialTaskContent(Path filePath) throws IOException {
        // Write your initial task content to the file
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<tasks/>\n";

        Files.writeString(filePath, content);
    }

    private void writeInitialCompletedTasksContent(Path filePath) throws IOException {
        // Write your initial completed tasks content to the file
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<tasks/>\n";

        Files.writeString(filePath, content);
    }


    public void loadCompartments() {
        try {
            String filePath = "compartmentSaves.xml";
            Path savePath = getSavePath(filePath);

            if (Files.exists(savePath)) {
                JAXBContext context = JAXBContext.newInstance(CompartmentWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                CompartmentWrapper wrapper = (CompartmentWrapper) unmarshaller.unmarshal(savePath.toFile());

                if (wrapper.getCompartments() != null) {
                    compartments.addAll(wrapper.getCompartments());
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCompartments() {
        try {
            String filePath = "compartmentSaves.xml";
            Path savePath = getSavePath(filePath);

            JAXBContext context = JAXBContext.newInstance(CompartmentWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            CompartmentWrapper wrapper = new CompartmentWrapper();
            wrapper.setCompartments(compartments);

            marshaller.marshal(wrapper, savePath.toFile());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveCompletedTasks() {
        try {
            String fileName = "completedTasksSave.xml";
            Path savePath = getSavePath(fileName);

            JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TaskWrapper wrapper = new TaskWrapper();
            wrapper.setTasks(completedTasks);

            marshaller.marshal(wrapper, savePath.toFile());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCompletedTasks() {
        try {
            String fileName = "completedTasksSave.xml";
            Path savePath = getSavePath(fileName);

            if (Files.exists(savePath)) {
                JAXBContext context = JAXBContext.newInstance(TaskWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                TaskWrapper wrapper = (TaskWrapper) unmarshaller.unmarshal(savePath.toFile());

                if (wrapper.getTasks() != null) {
                    completedTasks.addAll(wrapper.getTasks());
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }



    public void showTipsScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Tips.fxml"));
            AnchorPane pane = loader.load();

            Stage tipsStage = new Stage();
            tipsStage.setTitle("Helpful tips");
            tipsStage.initModality(Modality.WINDOW_MODAL);
            tipsStage.initOwner(primaryStage);

            // Creating and setting properties for the label
            Label tipsLabel = new Label();
            tipsLabel.setText("CTRL + N - New task or new compartment\n(depends on where you click before using this key combination)\nCTRL + C - Complete task\nCTRL + E - Edit task\nCTRL + S - Save\nDelete - Deletes task (or compartment)\n");
                tipsLabel.setWrapText(true);

            // Adding the label to the pane
            pane.getChildren().add(tipsLabel);

            Scene scene = new Scene(pane);
            tipsStage.setScene(scene);

            tipsStage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}