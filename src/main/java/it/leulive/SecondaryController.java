package it.leulive;

import it.leulive.utils.ClientManager;
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

    @FXML private void exit() {
        // richiesta di disconnessione al server
        System.exit(0);
    }

    @FXML private void enableFinalUser() {
        finalUser.setDisable(false);
    }

    @FXML private void disableFinalUser() {
        finalUser.setDisable(true);
    }


    @FXML private void confirmMessage() {
        String msgText = msgContent.getText();
        if(singleChoice.isSelected()){
            if(!(ClientManager.getKnown_users().contains(finalUser))){
                AnchorPane newPanelContent = new AnchorPane();
                TitledPane pane = new TitledPane("World Pane", newPanelContent);
                chat_container.getPanes().add(pane);
            }
        }
        
           
    }
}