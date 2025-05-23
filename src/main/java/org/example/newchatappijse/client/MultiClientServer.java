package org.example.newchatappijse.client;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MultiClientServer {
    private static final int PORT = 5000;
    private static final long serverStartTime = System.currentTimeMillis();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String command;
                while ((command = in.readLine()) != null) {
                    switch (command.toUpperCase()) {
                        case "TIME":
                            out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                            break;
                        case "DATE":
                            out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            break;
                        case "UPDATE":
                            long secondsElapsed = (System.currentTimeMillis() - serverStartTime) / 1000;
                            out.println("Server uptime: " + secondsElapsed + " seconds");
                            break;
                        case "BYE":
                            out.println("Goodbye!");
                            socket.close();
                            return;
                        default:
                            out.println("Invalid command");
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected.");
            }
        }
    }
}

