package it.leulive;

import java.io.IOException;
import java.util.HashMap;

import it.leulive.utils.ClientManager;
import it.leulive.utils.ProtocolMessages;
import it.leulive.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;

public class SecondaryController {

    // Questo è il controller della view sulla chat
    @FXML
    private TextArea msgContent;
    @FXML
    private RadioButton singleChoice;
    @FXML
    private RadioButton globalChoice;
    @FXML
    private TextField finalUser;
    @FXML
    private Alert invalid_msg = new Alert(AlertType.ERROR);
    @FXML
    private Accordion chat_container;

    HashMap<String, TitledPane> privateUsers = new HashMap<String, TitledPane>();

    @FXML
    private void exit() throws IOException {
        ClientManager.sendMessage("server", "/!"); // Destinatario server in quanto messaggio di protocollo
        try {
            ClientManager.disconnectFromServer();
        } catch (IOException e) {
            System.out.println("Non sono riuscito a disconnettermi dal server");
        }
        System.exit(0);
    }

    @FXML
    private void enableFinalUser() {
        finalUser.setDisable(false);
    }

    @FXML
    private void disableFinalUser() {
        finalUser.setDisable(true);
    }

    @FXML
    public void appendMessage(String message, boolean global) {
        String msgUsername = message.split(":")[0];
        Label label = Utils.createLabel(msgUsername.equals(ProtocolMessages.SERVER_USERNAME));
        label.setText(message);
        VBox newPanelContent;
        if (global) {
            newPanelContent = (VBox) chat_container.getPanes().get(0).getContent();
        } else if (!ClientManager.getKnown_users().contains(msgUsername)) {
            newPanelContent = new VBox();
            TitledPane pane = new TitledPane(msgUsername, newPanelContent);
            chat_container.getPanes().add(pane);
            ClientManager.addKnownUser(msgUsername);
            privateUsers.put(msgUsername, pane);
        } else {
            newPanelContent = (VBox) privateUsers.get(msgUsername).getContent();
        }
        newPanelContent.getChildren().add(label);
    }

    @FXML
    private void confirmMessage() throws IOException {
        String destText;
        String msgText = msgContent.getText().trim();
        if (singleChoice.isSelected()) {
            destText = finalUser.getText();
        } else {
            destText = ProtocolMessages.GLOBAL_MESSAGE;
        }
        switch (Utils.checkIfValidMessage(destText, msgText)) {
            case -1:
                invalid_msg.setHeaderText("ERRORE");
                invalid_msg.setContentText("Il destinatario inserito non è valido");
                invalid_msg.showAndWait();
                return;
            case -2:
                invalid_msg.setHeaderText("ERRORE");
                invalid_msg.setContentText("Il messaggio non può essere vuoto");
                invalid_msg.showAndWait();
                return;
            case 0:
                System.out.println("Messaggio valido");
                break;
        }
        ClientManager.sendMessage(destText, msgText);
        // Manca controllo se utente esiste!
        VBox newPanelContent;
        Label label = Utils.createLabel(false);
        label.setText("tu: " + msgText + " - " + ClientManager.printMessageWithTime());
        if (singleChoice.isSelected() && (!ClientManager.getKnown_users().contains(destText))) {
            newPanelContent = new VBox();
            TitledPane pane = new TitledPane(destText, newPanelContent);
            chat_container.getPanes().add(pane);
            ClientManager.addKnownUser(destText);
            privateUsers.put(destText, pane);
        } else if (singleChoice.isSelected()) {
            newPanelContent = (VBox) privateUsers.get(destText).getContent();
        } else {
            newPanelContent = (VBox) chat_container.getPanes().get(0).getContent();
        }
        newPanelContent.getChildren().add(label);
    }
}
