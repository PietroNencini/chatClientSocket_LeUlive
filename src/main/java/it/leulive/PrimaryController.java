package it.leulive;

import java.io.IOException;

import it.leulive.utils.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PrimaryController {
    @FXML private TextArea username;
    @FXML private Label error_msg;
    /**
     * Metodo chiamato al momento in cui l'utente inserisce il proprio username, da qui viene chiamata la funzione di connessione al server
     * @throws IOException Se ci sono problemi di connessione al server (server offline, porta o indirizzo errato ecc.)
    */
    @FXML private void startConnection() throws IOException {
        if(username.getText().length() > 4)
            ClientManager.connectToServer(username.getText());
        else 
            error_msg.setText("Almeno 4 caratteri");
    }


}
