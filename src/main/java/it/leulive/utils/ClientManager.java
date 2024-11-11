package it.leulive.utils;

import java.io.IOException;
import java.net.Socket;

import it.leulive.threads.ListeningThread;
import it.leulive.threads.SendingThread;

public class ClientManager {
    
    private static SendingThread  s_thread;
    private static ListeningThread  l_thread;

    public static void connectToServer(String username) throws IOException {
        Socket clientSocket = new Socket(/*todo aggiungere ip e porta*/);
        s_thread = new SendingThread(clientSocket);
        l_thread = new ListeningThread(clientSocket);
        s_thread.setClient_username(username);
        l_thread.start();
        s_thread.start();
    }


}
