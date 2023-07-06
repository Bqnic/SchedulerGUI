package gui.schedulergui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilites.Compartment;
import utilites.Priority;
import utilites.Task;

import java.time.LocalDate;
import java.util.Objects;

public class TaskEditDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private SplitMenuButton priorityField;
    @FXML
    private TextArea detailsField;
    @FXML
    private ChoiceBox<Compartment> compartmentChoice;
    private Task task;
    private Stage dialogStage;
    private boolean okInput = false;

    private GUI gui;
    public void setGUI(GUI gui){
        this.gui = gui;
        compartmentChoice.setItems(gui.getCompartments());
    }
    public void setTask(Task task){
        this.task = task;
        nameField.setText(task.getName());
        deadlinePicker.setPromptText(task.getDeadline().toString());
        priorityField.setText(task.getPriority().toString());
        detailsField.setText(task.getDetails());
        detailsField.setWrapText(true);
    }

    public boolean isOkInput() {
        return okInput;
    }

    public Task getTask(){
        return task;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    @FXML
    private void applyButton(){
        if (isValidInput()){
            task.setName(nameField.getText());
            task.setDeadline(deadlinePicker.getValue() == null ? LocalDate.now() : deadlinePicker.getValue());
            task.setDetails(detailsField.getText());
            task.setPriority(Priority.valueOf(priorityField.getText()));
            task.setCompartment(compartmentChoice.getValue() == null ? new Compartment("GENERAL") : compartmentChoice.getValue());
            okInput = true;
        }

        if (okInput) {
            dialogStage.close();
        }
    }

    @FXML
    private void cancelButton(){
        dialogStage.close();
    }

    @FXML
    private void highPriority(){
        priorityField.setText("HIGH");
    }

    @FXML
    private void mediumPriority(){
        priorityField.setText("MEDIUM");
    }

    @FXML
    private void lowPriority(){
        priorityField.setText("LOW");
    }

    public boolean isValidInput(){
        String errorMessage = "";
        if (nameField.getText() == null || nameField.getText().length() == 0)
            errorMessage += "Name of the task is not valid.\n";
        if (Objects.equals(priorityField.getText(), "NONE"))
            errorMessage += "Priority of task is not set\n";

        if (!errorMessage.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Error");
            alert.setHeaderText("Correct invalid fields.");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }

}
