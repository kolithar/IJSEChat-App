package org.example.newchatappijse.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientApp {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Thread receiver = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println("Server: " + response);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });

            receiver.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                out.println(command);
                if (command.equalsIgnoreCase("BYE")) {
                    break;
                }
            }

            receiver.join(); // Wait for receiver thread to finish
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


