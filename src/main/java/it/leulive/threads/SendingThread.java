package it.leulive.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import it.leulive.utils.ClientManager;

public class SendingThread extends Thread {
    private Socket clientSocket;
    private DataOutputStream out;
    private ArrayList<String> messageQueue;

    /**
     * ACrea il Thread di invio messaggi
     * 
     * @param s Socket del client, deve essere lo stesso del Thread che riceve
     * @throws IOException Se ci sono problemi nell'avvio del Thread
     */
    public SendingThread(Socket s) throws IOException {
        this.clientSocket = s;
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.messageQueue = new ArrayList<String>(20);
    }

    @Override
    public void run() {
        System.out.println("Pronto a inviare messaggi");
        while (this.isAlive()) {
            synchronized (messageQueue) {
                while (!messageQueue.isEmpty()) {
                    String msg = messageQueue.remove(0);
                    String[] msg_parts = msg.split(":");
                    try {
                        out.writeBytes(msg_parts[0] + "\n"); // Username
                        out.writeBytes(msg_parts[1] + "\n"); // Testo del messaggio
                    } catch (IOException e) {
                        System.out.println("Non sono riuscito a inviare questo messaggio");
                    }
                }
            }
        }
    }

    /**
     * Inserisce un messaggio digitato dall'utente nella coda di messaggi, appena
     * sarà possibile per il Thread, verrà inviato immediatamente
     * 
     * @param msg messaggio da inserire nella
     */
    public void enqueueMessage(String msg) {
        synchronized (messageQueue) {
            messageQueue.add(msg);
        }
    }

}