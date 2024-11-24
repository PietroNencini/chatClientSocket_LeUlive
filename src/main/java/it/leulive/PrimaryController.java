package it.leulive;

import java.io.IOException;

import it.leulive.utils.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class PrimaryController {
    @FXML
    private TextField username;
    @FXML
    private Label error_text;

    /**
     * Metodo chiamato al momento in cui l'utente inserisce il proprio username, da
     * qui viene chiamata la funzione di connessione al server
     * 
     * @throws IOException Se ci sono problemi di connessione al server (server
     *                     offline, porta o indirizzo errato ecc.)
     */
    @FXML
    private void startConnection() throws Exception {
        try {
            ClientManager.sendUsername(username.getText());
        } catch (IOException e) {
            System.out.println("Qualcosa è andato storto nella connessione");
        }
        if (ClientManager.isUsernameValid()) {
            ClientManager.startThread();
            App.setRoot("secondary");
        } else {
            error_text.setText("Inserire un altro username!");
        }
    }
}
