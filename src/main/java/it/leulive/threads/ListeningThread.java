package it.leulive.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListeningThread extends Thread{
    Socket clientSocket;
    BufferedReader in;

    public ListeningThread(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            do {
                String newMessage = listen();
            } while(true);
        } catch (IOException e) {
            System.out.println("Non riesco a ricevere messaggi");
            e.printStackTrace();
        }
    }


    public String listen() throws IOException{
        String receivedMessage = in.readLine();
        String sender = in.readLine();

        String info = sender + " : " + receivedMessage;
        return info;
    }


}

