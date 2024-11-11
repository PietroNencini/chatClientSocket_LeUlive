package it.leulive.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SendingThread extends Thread{
    Socket clientSocket;
    DataOutputStream out;
    String client_username;

    /**
     * Avvia l'esecuzion del Thread di invio 
     * @param s
     * @throws IOException
     */
    public SendingThread(Socket s) throws IOException{
        this.clientSocket = s;
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeBytes(client_username);
    }

    /**
     * Funzione che invia un messaggio al server affinché sia ricevuto dai client destinatari
     * @param message Messaggio effettivo
     * @param type <b>"*"<b> per inviare a tutti, altrimenti digitare l'username del destinatario specifico
     * @throws IOException se avvengono problemi nell'invio di dati
     */
    public void sendMessage(String message, String username) throws IOException{
        out.writeBytes(message + "\n");
        out.writeBytes(username);
        System.out.println("Messaggio inviato");
    }

    
    public String getClient_username() {
        return client_username;
    }

    public void setClient_username(String client_username) {
        this.client_username = client_username;
    }

}
