package it.leulive.utils;

import java.io.IOException;
import java.net.Socket;

import it.leulive.threads.ListeningThread;
import it.leulive.threads.SendingThread;

public class ClientManager {
    
    private static String clientUsername;
    private static SendingThread  s_thread;
    private static ListeningThread  l_thread;

    /**
     * Da questo metodo viene inviata la richiesta di connessione al server e allo stesso tempo inizializzati i due thread base del client
     * @param username Nome utente inserito da interfaccia grafica, che verrà inviato al server
     * @throws IOException
     */
    public static void connectToServer(String username) throws IOException {
        Socket clientSocket = new Socket(/*todo aggiungere ip e porta*/);
        s_thread = new SendingThread(clientSocket);
        l_thread = new ListeningThread(clientSocket);
        l_thread.start();
        s_thread.start();
    }


    public String getClient_username() {
        return clientUsername;
    }

    public void setClient_username(String username) {
        clientUsername = username;
    }


}
