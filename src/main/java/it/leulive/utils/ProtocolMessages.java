package it.leulive.utils;

public final class ProtocolMessages {                           // Inviata da:
    public static final String CONNECTION_REQUEST = "/+";       // CLIENT
    public static final String CONNECTION_ACCEPTED = "+";       // SERVER
    public static final String CONNECTION_REFUSED = "-";        // SERVER
    public static final String GLOBAL_MESSAGE = "*";            // CLIENT
    public static final String DISCONNECTION_MESSAGE = "/!";    // CLIENT
    public static final String SERVER_USERNAME = "server";      // CLIENT/SERVER
    public static final String USER_JUST_CONNECTED = "#+";      // CLIENT/SERVER
    public static final String USER_JUST_DISCONNECTED = "#-";   // CLIENT/SERVER
    public static final String USER_NOT_FOUND = "#!";           // CLIENT/SERVER
}
