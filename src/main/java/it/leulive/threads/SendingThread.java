package it.leulive.threads;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import it.leulive.utils.ProtocolMessages;


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

    public void sendConnectionRequestMessage(String username) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.writeBytes(ProtocolMessages.SERVER_USERNAME + "\n");
        out.writeBytes(ProtocolMessages.CONNECTION_REQUEST);
        String response;
        do {
            out.writeBytes(username);
            response = in.readLine();
        } while(response.equals(ProtocolMessages.CONNECTION_REFUSEDED));
    }

    /**
     * Funzione che invia un messaggio al server affinché sia ricevuto dai client destinatari
     * @param message Messaggio effettivo
     * @param type <b>"*"<b> per inviare a tutti, altrimenti digitare l'username del destinatario specifico
     * @throws IOException se avvengono problemi nell'invio di dati
     */
    public void sendMessage(String username, String message) throws IOException{
        out.writeBytes(message + "\n");
        out.writeBytes(username + "\n");
        System.out.println("Messaggio inviato: " + message + ", username: " + username); 
    }
}
