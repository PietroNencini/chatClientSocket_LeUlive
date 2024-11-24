package it.leulive.utils;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Utils {

    public static Label createLabel(boolean fromServer) {
        Label label = new Label();
        label.setWrapText(true);
        label.setFont(new Font("System", 16));
        label.setStyle("-fx-padding: 3 0;");
        if (fromServer) {
            label.setStyle(label.getStyle() + "-fx-text-fill: blue;");
        }
        return label;
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
