package it.leulive.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import it.leulive.utils.ClientManager;
import it.leulive.utils.ProtocolMessages;

public class ListeningThread extends Thread {
    Socket clientSocket;
    BufferedReader in;
    String[] last_message;

    public ListeningThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Ascolta i dati in arrivo dal server della chat, tramite il Buffer di ingresso
     * Normalmente si ricevono 2 dati principali: Mittente e Messaggio. Quando però
     * si riceve invece un messaggio di servizio che informa che
     * un utente si è collegato o scollegato dalla chat, allora il mittente è il
     * server (perché è un messaggio di servizio), ma si vuole sapere anche chi è
     * tale utente, quindi si ha una terza componente del messaggio finale.
     * 
     * @throws IOException se ci sono problemi nella ricezione di dati dal server
     */
    @Override
    public void run() {
        try {
            do {
                String sender = in.readLine();
                String received_message = in.readLine();
                System.out.println(received_message);
                String extra_username = null;
                if (sender.equals(ProtocolMessages.SERVER_USERNAME)
                        && ((received_message.equals(ProtocolMessages.USER_JUST_CONNECTED)
                                || received_message.equals(ProtocolMessages.USER_JUST_DISCONNECTED)))) {
                    extra_username = in.readLine();
                }
                ClientManager.receiveMessage(sender, received_message, extra_username);
            } while (true);
        } catch (IOException e) {
            System.out.println("Non riesco a ricevere messaggi");
            e.printStackTrace();
        }
    }

}
