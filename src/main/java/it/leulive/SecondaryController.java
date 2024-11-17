package it.leulive;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class SecondaryController {

    // Questo è il controller della view sulla chat   
    @FXML private TextArea msgContent;
    @FXML private ToggleGroup destination = new ToggleGroup();
    @FXML private TextField finalUser;
    @FXML private Alert invalid_msg = new Alert(AlertType.ERROR);

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
        //String destinationUser = destination.getSelectedToggle().
        if(finalUser.getText().length() < 4 || msgContent.getText().equals(""))
            invalid_msg.setContentText("Messaggio vuoto o utente destinatario non valido inserito");
    }
}