package chatServer;

import chatServer.server.ChatServer;

public class Main {

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.execute();
    }
}
