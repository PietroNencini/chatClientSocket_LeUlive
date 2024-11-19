package it.leulive.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import it.leulive.utils.ClientManager;
import it.leulive.utils.ProtocolMessages;

public class ListeningThread extends Thread{
    Socket clientSocket;
    BufferedReader in;
    String[] last_message;

    public ListeningThread(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            do {
                this.last_message = listen();
                ClientManager.receiveMessage(this.last_message);
            } while(true);
        } catch (IOException e) {
            System.out.println("Non riesco a ricevere messaggi");
            e.printStackTrace();
        }
    }

    /**
     * Ascolta i dati in arrivo dal server della chat, tramite il Buffer di ingresso <br> Normalmente si ricevono 2 dati principali: <b> Mittente </b> e <b> Messaggio </b>. Quando però si riceve invece un messaggio di servizio che informa che un utente si è collegato o scollegato dalla chat, allora il mittente è il server (perché è un messaggio di servizio), ma si vuole sapere anche chi è tale utente, quindi si ha una terza componente del messaggio finale. 
     * @return L'array di stringhe contenente le parti del messaggio, a meno che tale messaggio non sia di tipo speciale (come indicato prima), messaggio[2] è <b><code>null</code></b> e non deve essere guardato
     * @throws IOException se ci sono problemi nella ricezione di dati dal server
     */
    public String[] listen() throws IOException{
        String sender = in.readLine();
        String received_message = in.readLine();
        String extra_username = null;
        if(sender.equals(ProtocolMessages.SERVER_USERNAME) && (received_message.equals(ProtocolMessages.USER_JUST_CONNECTED) || received_message.equals(ProtocolMessages.USER_JUST_DISCONNECTED)))
            extra_username = in.readLine();
        String[] info = new String[3];
        info[0] = sender;
        info[1] = received_message;
        info[2] = extra_username;
        return info; 
    }

}

