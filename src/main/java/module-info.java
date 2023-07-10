module gui.schedulergui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires java.xml.bind;

    opens gui.schedulergui;

    exports gui.schedulergui;
    exports gui.schedulergui.utilities;
    opens gui.schedulergui.utilities;
    exports gui.schedulergui.controllers;
    opens gui.schedulergui.controllers;
}


