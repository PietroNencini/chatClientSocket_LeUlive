package it.leulive.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SendingThread extends Thread{
    Socket clientSocket;
    DataOutputStream out;

    /**
     * Avvia l'esecuzione del Thread di invio 
     * @param s Socket del client
     * @throws IOException Se ci sono problemi nell'avvio del Thread
     */
    public SendingThread(Socket s) throws IOException{
        this.clientSocket = s;
        this.out = new DataOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Funzione che invia un messaggio al server affinché sia ricevuto dai client destinatari
     * @param message Messaggio effettivo
     * @param type <b>"*"<b> per inviare a tutti, altrimenti digitare l'username del destinatario specifico
     * @throws IOException se avvengono problemi nell'invio di dati
     */
    public void sendMessage(String message, String username) throws IOException{
        out.writeBytes(message + "\n");
        out.writeBytes(username + "\n");
        System.out.println("Messaggio inviato: " + message + ", username: " + username); 
    }
}
