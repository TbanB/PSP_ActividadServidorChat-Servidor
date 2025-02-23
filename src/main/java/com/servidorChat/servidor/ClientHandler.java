package com.servidorChat.servidor;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {

    /**
     * creamos una lista CopyOnWriteArrayList que nos permite iterar por su contenido sin tener problemas de concurrencia.
     */
    private static List<PrintWriter> clientsList = new CopyOnWriteArrayList<>();
    private Socket socket;
    private PrintWriter output;
    private String userName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Este metodo nos permite emitir los mensajes a cada uno de los usuarios añadidos a la lista clientList
     * @param message
     */
    public static void broadcast(String message) {
        LocalDateTime nowTime = LocalDateTime.now();
        String formattedNowTime = nowTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        for (PrintWriter writer : clientsList) {
            writer.println("+ " + formattedNowTime + " - " + message);
        }
    }

    @Override
    public void run() {
        try {
            /**
             * declaramos InputStreamReader y le pasamos cómo parámetro el socket creado
             * al iniciar la escucha del nuevo cliente. InputStreamReader funciona cómo un decodificador convirtiendo
             * el flujo de bytes que vienen desde el socket en caracteres.
             */
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            /**
             * declaramos el BufferedReader para capturar lo que el usuario escribe sin necesidad de leer línea a línea,
             * recibe el InputStreamReader para decodificar los valores recibidos cómo bites desde el socket
             */
            BufferedReader input = new BufferedReader(inputStreamReader);

            /**
             * Declaramos el OutputStreamWriter para codificar el mensaje escrito por el usuario convirtiendo los caracteres en bytes
             */
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            /**
             * PrintWriter nos permite enviar el texto recibido por el cliente al otro extremo del socket-
             */
            output = new PrintWriter(outputStreamWriter);

            output.println("Nombre de usuario:");
            userName = input.readLine();

            clientsList.add(output);

            broadcast(">>> El usuario: " + userName + "se ha unido al chat");

            String message;
            while ((message = input.readLine()) != null ) {
                if (message.equalsIgnoreCase("/salir")) {
                    broadcast(">>> El usuario: " + userName + "ha salido del chat");
                    break;
                }

                broadcast(userName + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Erro en la cominicación con cliente: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                output.close();
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error al intentar cerrar el socket");
                throw new RuntimeException(e);
            }
        }
    }
}
