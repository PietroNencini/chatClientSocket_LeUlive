package it.leulive.threads;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import it.leulive.utils.ProtocolMessages;


public class SendingThread extends Thread{
    private Socket clientSocket;
    private DataOutputStream out;
    private boolean confirm_received;

    /**
     * ACrea il Thread di invio messaggi 
     * @param s Socket del client, deve essere lo stesso del Thread che riceve
     * @throws IOException Se ci sono problemi nell'avvio del Thread
     */
    public SendingThread(Socket s) throws IOException{
        this.clientSocket = s;
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.confirm_received = false;
    }

    @Override
    public void run() {
        System.out.println("Questo thread è pronto a inviare messaggi");
    }

    /**
     * Controlla se si ha terminato la fase di connessione col server, quindi se è possibile iniziare a ricevere messaggi dalla chat
     * @return true se la connessione è già avvenuta
     */
    public boolean checkIfCanProceed() {
        return confirm_received;
    }

    /** 
     * Invia al server la richiesta iniziale di connessione, come previsto dal protocollo <i> LeUlive </i>. Fintanto che il client non riesce a connettersi al server, non viene permesso di proseguire nell'esecuzione, quindi di inizializzare il Thread di ricezione.
    */
    public void sendConnectionRequestMessage(String username) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));       // Il thread di ricezione non è stato ancora creato quando viene chiamata questa funzione, dunque non è possibile ricevere la conferma di ingresso nella chat dal server senza che ci sia un buffer di ricezione a parte.
        out.writeBytes(ProtocolMessages.SERVER_USERNAME + "\n");            // Il protocollo prevede di inviare il nome utente del server
        out.writeBytes(ProtocolMessages.CONNECTION_REQUEST + "\n");         // seguito dal messaggio specifico di richiesta
        String response;
        do {
            out.writeBytes(username);                                       // Infine si invia l'username finché il server non ci dice che quello scelto va bene (alcuni username non sono possibili, ad esempio "server")
            response = in.readLine();
        } while(response.equals(ProtocolMessages.CONNECTION_REFUSED));
        confirm_received = true;
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
