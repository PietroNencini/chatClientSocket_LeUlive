package it.leulive.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import it.leulive.SecondaryController;
import it.leulive.threads.ListeningThread;
import javafx.application.Platform;

public class ClientManager {

    private static Socket clientSocket;
    private static String clientUsername;
    private static ListeningThread l_thread;
    private static ArrayList<String> known_users = new ArrayList<String>(); // In questo arrayList ci vanno tutti gli
                                                                            // utenti con cui si ha una chat privata
                                                                            // aperta
    private static SecondaryController chatController;
    private static boolean connectedWithServer = false;
    private static DataOutputStream out;

    /**
     * Da questo metodo vengono inizializzati i due thread base del client.
     * 
     * @throws IOException
     */
    public static void startThread() throws IOException {
        l_thread = new ListeningThread(clientSocket);
        l_thread.start();
    }

    public static void inizializeSocket() throws IOException {
        clientSocket = new Socket("localhost", 7934);
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    public static void disconnectFromServer() throws IOException {
        l_thread.interrupt();
        clientSocket.close();
    }

    /**
     * Metodo che prende i messaggi in arrivo dal Receiving Thread e si
     * occupa in automatico di riconoscere di che tipo si trattano, quindi cosa
     * farne poi a livello grafico
     * 
     * @param msg Array contenente le 3 parti fondamentali del messaggio:
     *            - Username destinatario ("server" se si tratta di un messaggio di
     *            protocollo/servizio)
     *            - Messaggio testuale effettivo
     *            - Username del mittente
     */

    public static synchronized void receiveMessage(String receiver, String msg_content, String sender)
            throws IOException {
        boolean global = false;
        String definitive_message = receiver + ": "; // Stringa che contiene il messaggio da mostrare a livello grafico
        if (receiver.equals(ProtocolMessages.SERVER_USERNAME)) {
            switch (msg_content) {
                case ProtocolMessages.USER_JUST_CONNECTED:
                    definitive_message += sender + " si è unito alla chat!";
                    global = true;
                    break;
                case ProtocolMessages.USER_JUST_DISCONNECTED:
                    definitive_message += sender + " è uscito dalla chat";
                    global = true;
                    break;
                case ProtocolMessages.USER_NOT_FOUND:
                    definitive_message += sender + " - username non esistente";
                    break;
                default:
                    System.out.println("Messaggio non riconosciuto");
            }
        } else if (receiver.startsWith("*")) { // Se invece inizia per "*"
            receiver = receiver.substring(1); // Si tratta di un messaggio globale (necessario saperlo per decidere su
                                              // quale dei riquadri chat inserirlo)
            global = true;
            definitive_message = receiver + ": " + msg_content;
        } else { // Se invece l'username è un altro mittente
            definitive_message += msg_content;
        }
        definitive_message += " - " + printMessageWithTime();
        // Per poter inviare al controller questi due dati è necessario renderli delle
        // costanti
        final String MESSAGE = definitive_message;
        final boolean GLOBAL_MESSAGE = global;
        Platform.runLater(() -> chatController.appendMessage(MESSAGE, GLOBAL_MESSAGE));
    }

    public static String printMessageWithTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }

    public static void sendMessage(String dest, String msg) throws IOException {
        out.writeBytes(dest + "\n"); // Username del destinatario
        out.writeBytes(msg + "\n");
    }

    public static boolean isUsernameValid() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String response = in.readLine();
        System.out.println(response);
        return (response.equals(ProtocolMessages.CONNECTION_ACCEPTED));
    }

    public static void sendUsername(String username) throws IOException {
        out.writeBytes(ProtocolMessages.SERVER_USERNAME + "\n"); // Nome utente del server
        out.writeBytes(ProtocolMessages.CONNECTION_REQUEST + "\n"); // Seguito dal messaggio specifico di richiesta
        out.writeBytes(username + "\n"); // Username che si vuole utilizzare
        clientUsername = username;
    }

    public static ArrayList<String> getKnown_users() {
        return known_users;
    }

    public static String getClient_username() {
        return clientUsername;
    }

    public static void setClient_username(String username) {
        clientUsername = username;
    }

    public static void setChatController(SecondaryController chatController) {
        ClientManager.chatController = chatController;
    }

    public static SecondaryController getChatController() {
        return chatController;
    }

    public static boolean isConnected() {
        return connectedWithServer;
    }

    public static void addKnownUser(String username) {
        ClientManager.known_users.add(username);
    }

}
