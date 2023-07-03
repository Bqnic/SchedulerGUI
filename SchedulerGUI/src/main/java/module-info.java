module gui.schedulergui {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens gui.schedulergui to javafx.fxml;
    exports gui.schedulergui;
}