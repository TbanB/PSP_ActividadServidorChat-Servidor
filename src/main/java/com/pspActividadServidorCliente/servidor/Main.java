package com.pspActividadServidorCliente.servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("inicia servidor de chat");

        try {
            ServerSocket server = new ServerSocket();
            InetSocketAddress address = new InetSocketAddress("192.168.1.68",2001);
            server.bind(address);

            while (true) {
                Socket socketClient = server.accept();
                System.out.println("Nuevo cliente conectado: " + socketClient.getInetAddress());
                ClientHandler handler = new ClientHandler(socketClient);
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
