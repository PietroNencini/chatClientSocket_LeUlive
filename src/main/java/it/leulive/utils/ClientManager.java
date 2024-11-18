package it.leulive.utils;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

import it.leulive.threads.ListeningThread;
import it.leulive.threads.SendingThread;

public class ClientManager {

    private static String clientUsername;
    private static SendingThread  s_thread;
    private static ListeningThread  l_thread;
    private static ArrayList<String> known_users = new ArrayList<String>(); // In questo arrayList ci vanno tutti gli utenti con cui si ha una chat privata aperta

    public static void receiveMessage(String[] msg) {

        String username = msg[0];
        String msg_content = msg[1];
        String extra_username = msg[2];
        String definitive_message = username + ": ";                             // Stringa che contiene il messaggio finale da mostrare a livello grafico
        if(username.equals(ProtocolMessages.SERVER_USERNAME)) {     // Se il mittente è il server
            switch(msg_content) {
                                                                     // Si tratta di un messaggio di protocollo

                case ProtocolMessages.CONNECTION_ACCEPTED:
                    definitive_message += "Benvenuto nella chat " + clientUsername ;
                    break;
                case ProtocolMessages.CONNECTION_REFUSED:
                    definitive_message += "Qualcosa è andato storto nella connessione. Controllare che l'username inserito sia valido e che il server sia al momento disponibile";
                    break;


        
            }
        } else if(username.startsWith("*")){                  // Se invece inizia per "*"
            username = username.substring(1);             // Si tratta di un messaggio globale (necessario saperlo per decidere su quale dei riquadri chat inserirlo)
            //global_msg
        } else {                                                    // Se invece l'username è un altro mittente 
            if(!known_users.contains(username))                      // si tratta di un messaggio privato, controllo se conosco e ho una chat già aperta con questo utente
                known_users.add(username);
        }

        definitive_message += " - " + LocalTime.now();              // Aggiungi la data di ricezione del messaggio
    }
    
    public static ArrayList<String> getKnown_users() {
        return known_users;
    }


    public static void setKnown_users(ArrayList<String> known_users) {
        ClientManager.known_users = known_users;
    }


    /**
     * Da questo metodo viene inviata la richiesta di connessione al server e allo stesso tempo inizializzati i due thread base del client
     * @param username Nome utente inserito da interfaccia grafica, che verrà inviato al server
     * @throws IOException
     */
    public static void connectToServer(String username) throws IOException {
        Socket clientSocket = new Socket(/*todo aggiungere ip e porta*/);
        s_thread = new SendingThread(clientSocket);
        l_thread = new ListeningThread(clientSocket);
        s_thread.start();
        l_thread.start();
    }


    public String getClient_username() {
        return clientUsername;
    }

    public void setClient_username(String username) {
        clientUsername = username;
    }



}
