package it.leulive;

import java.io.IOException;

import it.leulive.utils.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {
    @FXML private TextArea username;
    @FXML private Alert error_alert = new Alert(AlertType.ERROR, "ERRORE DI CONNESSIONE");
    /**
     * Metodo chiamato al momento in cui l'utente inserisce il proprio username, da qui viene chiamata la funzione di connessione al server
     * @throws IOException Se ci sono problemi di connessione al server (server offline, porta o indirizzo errato ecc.)
    */
    @FXML private void startConnection() throws Exception { 
        try {
            ClientManager.connectToServer(username.getText());
        } catch(IOException e) {
            System.out.println("Qualcosa è andato storto nella connessione");
            error_alert.setContentText("Errore nella connessione");
        }
    }
}
