package org.academiadecodigo.bootcamp65;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final int port = 8080;

    private ConcurrentHashMap<String, UserThread> users;
    private UserThread newUser;
    private ExecutorService pool;

    public ChatServer() {
        this.users = new ConcurrentHashMap<>();
        this.pool = Executors.newCachedThreadPool();
    }

    public void execute() {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user has connected");

                newUser = new UserThread(socket, this);
                pool.submit(newUser);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void broadcast(String message, UserThread userThread) {

        for (Map.Entry<String, UserThread> user : users.entrySet()) {
            if (user.getValue() == userThread) {
                continue;
            }
            user.getValue().sendMessage(user.getValue().getTime() + message);
        }
    }

    public void whisper(String message, UserThread userThread) {
        for (Map.Entry<String, UserThread> user : users.entrySet()) {
            if (user.getValue() == userThread) {
                user.getValue().sendMessage(user.getValue().getTime() + message);
            }
        }
    }

    public void removeUser(String userName, UserThread userThread) {

        boolean test = users.remove(userName, userThread);

        if (test) {
            System.out.println("The user " + userName + " has left the server.");
        }
    }

    public boolean hasUsers() {
        return !this.users.isEmpty();
    }

    public ConcurrentHashMap<String, UserThread> getUsers() {
        return users;
    }
}