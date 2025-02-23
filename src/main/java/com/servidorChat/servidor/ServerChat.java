package com.servidorChat.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChat {
    /**
     * Iniciomos la variable PORT que almacena el entero que indicará el puerto al que escuchar
     * **/
    private static final int PORT = 2001;

    public static void main(String[] args) {
        System.out.println("Inicio servidor multihilo");

        /**
         * Iniciamos un nuevo servicio escuchando al puerto 2001
         * **/
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("servidor escuchando en el puerto: " + PORT);
            while (true) {
                /**
                 * Mientras este levantado el servidor estará escuchando al puerto indicado
                 * esperando la conexión de un nuevo cliente
                 * **/
                Socket clientSocket = server.accept();

                /**
                 * Una vez establecida la conexión con el nuevo cliente creamos un hilo
                 * que gestionará la comunicación con este nuevo cliente.
                 */
                ClientHandler handler = new ClientHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
