package it.leulive.threads;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ListeningThread extends Thread{
     Socket s;
     BufferedReader in;

    public ListeningThread(Socket clientSocket) throws IOException{
          this.s = clientSocket;
          this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String ascolta() throws IOException{
        String messaggioRIcevuto = in.readLine();
        String mittente = in.readLine();

        String info = messaggioRIcevuto + ":" + mittente;
        return messaggioRIcevuto;
    }
   
 
    }

