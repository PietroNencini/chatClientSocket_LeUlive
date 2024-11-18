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

