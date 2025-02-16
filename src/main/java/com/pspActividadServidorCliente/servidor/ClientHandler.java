package com.pspActividadServidorCliente.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private String nombreUsuario;
    private boolean loggedIn = false;

    public ClientHandler(Socket socketClient) {
        this.clientSocket = socketClient;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                ) {

            ChatManager.userRegistration(clientSocket, out);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
