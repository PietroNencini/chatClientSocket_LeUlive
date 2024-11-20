package it.leulive.utils;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class GraphicUtils {
    
    public static TextArea createTextArea(boolean fromServer) {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(250);
        textArea.setPrefHeight(200);
        textArea.setFont(new Font("System", 15));
        textArea.setStyle("-fx-padding: 12 0;");
        if(fromServer) {
            textArea.setStyle(textArea.getStyle() + "-fx-background-color: cyan;");
        }
        return textArea;
    }

}
