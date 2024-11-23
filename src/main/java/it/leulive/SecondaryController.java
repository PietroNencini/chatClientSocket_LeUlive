package it.leulive;

import java.io.IOException;
import java.util.HashMap;

import it.leulive.utils.ClientManager;
import it.leulive.utils.GraphicUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;

public class SecondaryController {

    // Questo è il controller della view sulla chat   
    @FXML private TextArea msgContent;
    @FXML private RadioButton singleChoice;
    @FXML private RadioButton globalChoice;
    @FXML private TextField finalUser;
    @FXML private Alert invalid_msg = new Alert(AlertType.ERROR);
    @FXML private Accordion chat_container;

    HashMap<String, TitledPane> privateUsers = new HashMap<String, TitledPane>();

    @FXML private void exit() {
        ClientManager.sendMessage("server", "/!");  // Destinatario server in quanto messaggio di protocollo
        try {
            ClientManager.disconnectFromServer();
        } catch(IOException e) {
            System.out.println("Non sono riuscito a disconnettermi dal server");
        }
        System.exit(0);
    }

    @FXML private void enableFinalUser() {
        finalUser.setDisable(false);
    }

    @FXML private void disableFinalUser() {
        finalUser.setDisable(true);
    }

    @FXML
    public void appendMessage(String message, boolean global) {
        TextArea textArea = GraphicUtils.createTextArea(message.startsWith("server"));
        String msgUsername = message.split(":")[0];
        AnchorPane message_space;
        if(global) {
            message_space = (AnchorPane) chat_container.getPanes().get(0).getContent();
            message_space.getChildren().add(textArea);
        } else if(privateUsers.containsKey(msgUsername)) {
            message_space = (AnchorPane) privateUsers.get(privateUsers.get(msgUsername)).getContent();
            message_space.getChildren().add(textArea);
        } else {
            AnchorPane newPanelContent = new AnchorPane();
            newPanelContent.getChildren().add(textArea);
            TitledPane pane = new TitledPane(msgUsername, newPanelContent);
            privateUsers.put(msgUsername, pane);
            chat_container.getPanes().add(pane);
        }
    }

    @FXML private void confirmMessage() {
        String msgText = msgContent.getText();
        if(singleChoice.isSelected()){                                                  
            if(!(ClientManager.getKnown_users().contains(finalUser.getText()))){
                AnchorPane newPanelContent = new AnchorPane();
                TitledPane pane = new TitledPane("World Pane", newPanelContent);
                chat_container.getPanes().add(pane);
            } 
            
        } else {
            if(finalUser.getText().isEmpty()){
                invalid_msg.setHeaderText("Invalid Message");
                invalid_msg.setContentText("Username cannot be empty.");
                invalid_msg.showAndWait();
                return;
            } else {
                if(msgText.isEmpty()){
                    invalid_msg.setHeaderText("Invalid Message");
                    invalid_msg.setContentText("Message cannot be empty.");
                    invalid_msg.showAndWait();
                    return;
                }
            }
        }
    }


}