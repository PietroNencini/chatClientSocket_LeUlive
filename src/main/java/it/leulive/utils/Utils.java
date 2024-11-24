package it.leulive.utils;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class Utils {

    public static TextArea createTextArea(boolean fromServer) {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(250);
        textArea.setPrefHeight(200);
        textArea.setFont(new Font("System", 15));
        textArea.setStyle("-fx-padding: 12 0;");
        if (fromServer) {
            textArea.setStyle(textArea.getStyle() + "-fx-background-color: cyan;");
        }
        return textArea;
    }

    /**
     * A livello client viene controllato che il messaggio e il suo destinatario non
     * siano stringhe vuote. Per tutti gli altri controlli sulla validità del
     * messaggio inserito se ne occupa il server
     * 
     * @param receiver destinatario del messaggio
     * @param text     testo del messaggio
     * @return -1 se il destinatario è vuoto. -2 se il testo del messaggio è vuoto.
     *         0 se va tutto bene
     */
    public static int checkIfValidMessage(String receiver, String text) {
        if (receiver.isEmpty()) {
            return -1;
        } else if (text.isEmpty()) {
            return -2;
        } else {
            return 0;
        }
    }

}
