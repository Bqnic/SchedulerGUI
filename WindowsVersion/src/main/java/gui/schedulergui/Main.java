package gui.schedulergui;

import javafx.application.Application;

public class Main {
    public static void main(String[] args){
        System.setProperty("prism.forceGPU", "true");
        Application.launch(GUI.class);
    }
}
