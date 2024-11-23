package it.leulive.threads;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import it.leulive.utils.ClientManager;
import it.leulive.utils.ProtocolMessages;


public class SendingThread extends Thread{
    private Socket clientSocket;
    private DataOutputStream out;
    private ArrayList<String> messageQueue;

    /**
     * ACrea il Thread di invio messaggi 
     * @param s Socket del client, deve essere lo stesso del Thread che riceve
     * @throws IOException Se ci sono problemi nell'avvio del Thread
     */
    public SendingThread(Socket s) throws IOException{
        this.clientSocket = s;
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.messageQueue = new ArrayList<String>(20);
    }
    
    @Override
    public void run() {
        System.out.println("Pronto a connettermi al server");
        try {
            sendConnectionRequestMessage(ClientManager.getClient_username());
        } catch(IOException e) {
            System.out.println("ERRORE: Server non raggiungibile o username invalido!");
            return;
        } 
        System.out.println("Connessione effettuata - pronto a inviare messaggi");
        while(this.isAlive() && ClientManager.isConnected()) {
            synchronized (messageQueue) {
                while(!messageQueue.isEmpty()) {
                    String msg = messageQueue.remove(0);
                    String[] msg_parts = msg.split(":");
                    try {
                        out.writeBytes(msg_parts[0]);   //  Username
                        out.writeBytes(msg_parts[1]);   //  Testo del messaggio
                    } catch(IOException e) {
                        System.out.println("Non sono riuscito a inviare questo messaggio");
                    }
                }                    
            }
        }
    }

    /**
     * Inserisce un messaggio digitato dall'utente nella coda di messaggi, appena sarà possibile per il Thread, verrà inviato immediatamente
     * @param msg messaggio da inserire nella
     */
    public void enqueueMessage(String msg) {
        synchronized (messageQueue) {
            messageQueue.add(msg);
        }
    }

    //todo modificare
    /** 
     * Invia al server la richiesta iniziale di connessione, come previsto dal protocollo <i> LeUlive </i>. Fintanto che il client non riesce a connettersi al server, non viene permesso di proseguire nell'esecuzione, quindi di inizializzare il Thread di ricezione.
    */
    private void sendConnectionRequestMessage(String username) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));       // Il thread di ricezione non è stato ancora creato quando viene chiamata questa funzione, dunque non è possibile ricevere la conferma di ingresso nella chat dal server senza che ci sia un buffer di ricezione a parte.
        out.writeBytes(ProtocolMessages.SERVER_USERNAME + "\n");            // Il protocollo prevede di inviare il nome utente del server
        out.writeBytes(ProtocolMessages.CONNECTION_REQUEST + "\n");         // seguito dal messaggio specifico di richiesta
        String response;
        do {
            out.writeBytes(username);                                       // Infine si invia l'username finché il server non ci dice che quello scelto va bene (alcuni username non sono possibili, ad esempio "server")
            response = in.readLine();
        } while(response.equals(ProtocolMessages.CONNECTION_REFUSED));
    }

}