package com.pspActividadServidorCliente.servidor;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatManager {
    private static Map<String, PrintWriter> connectedUsers = Collections.synchronizedMap(new HashMap<>());

    public static void userRegistration(String userName, PrintWriter out) {
        System.out.printf("1. -- userRegistration socket", userName);
        System.out.printf("2. -- userRegistration out", out.toString());
        connectedUsers.put(userName, out);
    }

    public static void userRemove(Socket socket) {
        connectedUsers.remove(socket);
    }

    public static void sendMessage(Socket emitterName, Socket targetUser, String message) {
        PrintWriter out = connectedUsers.get(targetUser);
        if (out != null) {
            out.println("[" + emitterName + "]:  " + message);
        }
    }
}