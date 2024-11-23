package it.leulive.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

import it.leulive.SecondaryController;
import it.leulive.threads.ListeningThread;
import it.leulive.threads.SendingThread;
import javafx.application.Platform;

public class ClientManager {

    private static Socket clientSocket;
    private static String clientUsername;
    private static SendingThread s_thread;
    private static ListeningThread l_thread;
    private static ArrayList<String> known_users = new ArrayList<String>(); // In questo arrayList ci vanno tutti gli
                                                                            // utenti con cui si ha una chat privata
                                                                            // aperta
    private static SecondaryController chatController;
    private static boolean connectedWithServer = false;

    /**
     * Da questo metodo viene inviata la richiesta di connessione al server e allo
     * stesso tempo inizializzati i due thread base del client. <br>
     * Finché il server non invia la conferma, il Thread per l'ascolto dei messaggi
     * in arrivo non verrà attivato, pertanto non sarà ancora possibile procedere a
     * inviare e ricevere messaggi
     * 
     * @param username Nome utente inserito da interfaccia grafica, che verrà
     *                 inviato al server, per verificare la possibilità di poterlo
     *                 usare come username proprio
     * @throws IOException
     */
    public static void startThread() throws IOException {
        s_thread = new SendingThread(clientSocket);
        l_thread = new ListeningThread(clientSocket);
        s_thread.start();
        l_thread.start();
    }

    public static void inizializedSocket() throws IOException {
        clientSocket = new Socket("localhost", 7934);
    }

    public static void disconnectFromServer() throws IOException {
        closeThread();
        clientSocket.close();
    }

    public static void closeThread() throws IOException {
        s_thread.interrupt();
        l_thread.interrupt();
    }

    /**
     * Metodo che prende i messaggi in arrivo dal <b> Receiving Thread </b> e si
     * occupa in automatico di riconoscere di che tipo si trattano, quindi cosa
     * farne poi a livello grafico
     * 
     * @param msg Array contenente le 3 parti fondamentali del messaggio:
     *            <ul>
     *            <li><i>Username</i> del mittente ("server" se si tratta di un
     *            messaggio di protocollo/servizio)</li>
     *            <li><i> Messaggio testuale </i> effettivo</li>
     *            <li><i> Username </i> dell'utente la cui richiesta di
     *            conn./disconn. - Presente solo nei messaggi di servizio di questo
     *            tipo (dato che di solito i messaggi sono divisi in due parti e non
     *            3, in tal caso questo campo è <b>null</b>)</li>
     *            </ul>
     */
    public static synchronized void receiveMessage(String[] msg) throws IOException {
        String username = msg[0];
        String msg_content = msg[1];
        String extra_username = msg[2]; // ! ATTENZIONE: Può essere NULL
        boolean global = false;
        String definitive_message = username + ": "; // Stringa che contiene il messaggio finale da mostrare a livello
                                                     // grafico
        if (username.equals(ProtocolMessages.SERVER_USERNAME)) { // Se il mittente è il server si tratta di un messaggio
                                                                 // di protocollo
            switch (msg_content) { // Dunque il contenuto del messaggio sarà di sicuro una stringa tra quelle
                                   // conosciute
                case ProtocolMessages.CONNECTION_ACCEPTED:
                    definitive_message += "Benvenuto nella chat " + clientUsername;
                    connectedWithServer = true;
                    break;
                case ProtocolMessages.CONNECTION_REFUSED:
                    definitive_message += "Qualcosa è andato storto nella connessione. Controllare che l'username inserito sia valido e che il server sia al momento disponibile";
                    connectedWithServer = false;
                    // closeThread();
                    break;
                case ProtocolMessages.USER_JUST_CONNECTED:
                    definitive_message += extra_username + " si è unito alla chat!";
                case ProtocolMessages.USER_JUST_DISCONNECTED:
                    definitive_message += extra_username + " è uscito dalla chat";
                case ProtocolMessages.USER_NOT_FOUND:
                    definitive_message += extra_username + " - username non esistente";
                default:
                    System.out.println("Messaggio non riconosciuto");
            }
        } else if (username.startsWith("*")) { // Se invece inizia per "*"
            username = username.substring(1); // Si tratta di un messaggio globale (necessario saperlo per decidere su
                                              // quale dei riquadri chat inserirlo)
            global = true;
            definitive_message += msg_content;
        } else { // Se invece l'username è un altro mittente
            if (!known_users.contains(username)) // si tratta di un messaggio privato, controllo se conosco e ho una
                                                 // chat già aperta con questo utente
                known_users.add(username);
            definitive_message += msg_content;
        }

        definitive_message += " - " + LocalTime.now() + "\n"; // Aggiungi la data di ricezione del messaggio
        final String MESSAGE = definitive_message; // Per poter inviare al controller questi due dati è necessario
                                                   // renderli delle costanti
        final boolean GLOBAL_MESSAGE = global; // (Per esigenza di JavaFX)
        Platform.runLater(() -> chatController.appendMessage(MESSAGE, GLOBAL_MESSAGE));
    }

    /**
     * Chiama il Thread di invio già in esecuzione per inviare un messaggio,
     * seguendo lo standard del protocollo
     * 
     * @param username Nome utente destinatario
     * @param msg_text Testo del messaggio
     */
    public static void sendMessage(String username, String msg_text) {
        s_thread.enqueueMessage(username + ":" + msg_text);
    }

    public static void connectToServer(String username) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeBytes(ProtocolMessages.SERVER_USERNAME + "\n"); // Il protocollo prevede di inviare il nome utente del
                                                                 // server
        out.writeBytes(ProtocolMessages.CONNECTION_REQUEST + "\n"); // seguito dal messaggio specifico di richiesta
        out.writeBytes(username + "\n"); // Infine si invia l'username finché il server non ci dice che quello
                                         // scelto va
        // bene (alcuni username non sono possibili, ad esempio "server")
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

}
