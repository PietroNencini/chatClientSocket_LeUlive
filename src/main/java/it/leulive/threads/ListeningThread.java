package it.leulive.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListeningThread extends Thread{
     Socket s;
     BufferedReader in;

    public ListeningThread(Socket clientSocket) throws IOException{
          this.s = clientSocket;
          this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            do {
                listen();
            } while(true);
        } catch (IOException e) {
            System.out.println("Non riesco a ricevere messaggi");
            e.printStackTrace();
        }
    }


    public String listen() throws IOException{
        String messaggioRIcevuto = in.readLine();
        String mittente = in.readLine();

        String info = messaggioRIcevuto + ":" + mittente;
        return info;
    }
   
 
    }

