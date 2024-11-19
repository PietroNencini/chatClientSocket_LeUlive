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

        /**
     * Da questo metodo viene inviata la richiesta di connessione al server e allo stesso tempo inizializzati i due thread base del client. <br> Finché il server non invia la conferma, il Thread per l'ascolto dei messaggi in arrivo non verrà attivato, pertanto non sarà ancora possibile procedere a inviare e ricevere messaggi
     * @param username Nome utente inserito da interfaccia grafica, che verrà inviato al server, per verificare la possibilità di poterlo usare come username proprio
     * @throws IOException
     */
    public static void connectToServer(String username) throws Exception {
        Socket clientSocket = new Socket(/*todo aggiungere ip e porta*/);
        s_thread = new SendingThread(clientSocket);
        l_thread = new ListeningThread(clientSocket);
        s_thread.start();
        while(!s_thread.checkIfCanProceed()) {
            Thread.currentThread().sleep(50);
        }
        l_thread.start();
    }

    /**
     * Metodo che prende i messaggi in arrivo dal <b> Receiving Thread </b> e si occupa in automatico di riconoscere di che tipo si trattano, quindi cosa farne poi  a livello grafico
     * @param msg Array contenente le 3 parti fondamentali del messaggio: <ul> 
     *              <li> <i>Username</i> del mittente ("server" se si tratta di un messaggio di protocollo/servizio)</li>
     *              <li> <i> Messaggio testuale </i> effettivo </li> 
     *              <li> <i> Username </i> dell'utente la cui richiesta di conn./disconn. - Presente solo nei messaggi di servizio di questo tipo (dato che di solito i messaggi sono divisi in due parti e non 3, in tal caso questo campo è <b>null</b>)  </li> </ul> 
     */
    public static String receiveMessage(String[] msg) {
        String username = msg[0];
        String msg_content = msg[1];
        String extra_username = msg[2];                             //! ATTENZIONE: Può essere NULL

        String definitive_message = username + ": ";
                        // Stringa che contiene il messaggio finale da mostrare a livello grafico
        if(username.equals(ProtocolMessages.SERVER_USERNAME)) {     // Se il mittente è il server si tratta di un messaggio di protocollo
            switch(msg_content) {                                   // Dunque il contenuto del messaggio sarà di sicuro una stringa tra quelle conosciute
                case ProtocolMessages.CONNECTION_ACCEPTED:
                    definitive_message += "Benvenuto nella chat " + clientUsername ;
                    break;
                case ProtocolMessages.CONNECTION_REFUSED:
                    definitive_message += "Qualcosa è andato storto nella connessione. Controllare che l'username inserito sia valido e che il server sia al momento disponibile";
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
        } else if(username.startsWith("*")){                  // Se invece inizia per "*"
            username = username.substring(1);             // Si tratta di un messaggio globale (necessario saperlo per decidere su quale dei riquadri chat inserirlo)
            //global_msg
            definitive_message += msg_content;
        } else {                                                    // Se invece l'username è un altro mittente 
            if(!known_users.contains(username))                      // si tratta di un messaggio privato, controllo se conosco e ho una chat già aperta con questo utente
                known_users.add(username);
            definitive_message += msg_content;
        }

        definitive_message += " - " + LocalTime.now() + "\n";              // Aggiungi la data di ricezione del messaggio
        return definitive_message;
    }
    
    public static ArrayList<String> getKnown_users() {
        return known_users;
    }

    public String getClient_username() {
        return clientUsername;
    }

    public void setClient_username(String username) {
        clientUsername = username;
    }



}
