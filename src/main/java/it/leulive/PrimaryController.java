package it.leulive;

import java.io.IOException;

import it.leulive.utils.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PrimaryController {
    TextArea username;
    @FXML
    private void startConnection() throws IOException {
        ClientManager.connectToServer(username.getText());
    }
}
