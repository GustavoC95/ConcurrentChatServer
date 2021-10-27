package org.academiadecodigo.bootcamp65;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

public class UserThread extends Thread {

    private Socket socket;
    private ChatServer chatServer;
    private PrintWriter writer;
    private BufferedReader bReader;
    private String clientMessage;
    private String userName;

    public UserThread(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;

        try {
            this.bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {

            createUsername();

            printUsers();

            writer.println("Use /commands to show all available commands.");

            String serverMessage = "New connected user: " + userName;
            chatServer.broadcast(serverMessage, this);

            while (true) {

                clientMessage = bReader.readLine();

                if (clientMessage.equals("/list")) {
                    for (Map.Entry<String, UserThread> user : chatServer.getUsers().entrySet()) {
                        writer.println(user.getKey());
                    }
                    continue;
                }

                if (clientMessage.equals("/w")) {
                    printUsers();
                    writer.println("Who do you wanna whisper?");
                    clientMessage = bReader.readLine();
                    for (Map.Entry<String, UserThread> user : chatServer.getUsers().entrySet()) {
                        if (clientMessage.equals(user.getKey())) {
                            writer.println("Write your message to: " + user.getKey());
                            clientMessage = bReader.readLine();
                            chatServer.whisper("Whisper from_" + userName + ": " + clientMessage, user.getValue());
                        }
                    }
                    continue;
                }

                if (clientMessage.equals("/commands")) {
                    writer.println("Use /list to show all active users.");
                    writer.println("Use /w to whisper a user.");
                    writer.println("Use /bye to leave the server.");
                    continue;
                }

                if (clientMessage.equals("/bye")) {
                    chatServer.removeUser(userName, this);
                    socket.close();
                    serverMessage = userName + " has left the chat!";
                    chatServer.broadcast(userName + ": Goodbye everyone. Have a nice day.", this);
                    chatServer.broadcast(serverMessage, this);
                    continue;
                }

                serverMessage = userName + ": " + clientMessage;
                chatServer.broadcast(serverMessage, this);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void printUsers() {
        if (chatServer.hasUsers()) {
            writer.println("Connected users: " + chatServer.getUsers().keySet());
            return;
        }
        writer.println("No other users are online.");
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return "[" + localDateTime.getHour() + "h" + localDateTime.getMinute() + ":" + localDateTime.getSecond() + "] ";
    }

    public void createUsername() {
        try {
            writer.println("Enter your username: ");
            this.userName = bReader.readLine();
            if (chatServer.getUsers().containsKey(userName)) {
                writer.println("Username already in use.");
                createUsername();
            }
            chatServer.getUsers().put(this.userName, this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}