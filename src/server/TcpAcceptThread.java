package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TcpAcceptThread extends Thread {
    private final ServerSocket serverSocket;
    private final List<ClientThread> clientThreads;
    private final BlockingQueue<Message> messageQueue;

    public TcpAcceptThread(ServerSocket serverSocket, List<ClientThread> clientThreads,
                           BlockingQueue<Message> messageQueue) {
        this.serverSocket = serverSocket;
        this.clientThreads = clientThreads;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted client");

            ClientThread clientThread = new ClientThread(clientSocket, messageQueue);
            clientThread.start();
            clientThreads.add(clientThread);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
