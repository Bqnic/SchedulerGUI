package gui.schedulergui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CompartmentEditDialogController {

    @FXML
    private TextField compartmentField;

    private String compartment;
    private Stage dialogStage;
    private boolean okInput = false;
    @FXML
    private void applyButton(){
        if(isValidInput()){
            compartment = compartmentField.getText();
            okInput = true;
        }

        if (okInput)
            dialogStage.close();
    }

    @FXML
    private void cancelButton(){
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public boolean isValidInput(){
        String errorMessage = "";
        if (compartmentField.getText() == null || compartmentField.getText().length() == 0)
            errorMessage += "Name of the compartment is not valid.\n";

        if (!errorMessage.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Correct invalid fields.");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }

    public String getCompartment() {
        return compartment;
    }

    public boolean isOkInput() {
        return okInput;
    }
}
