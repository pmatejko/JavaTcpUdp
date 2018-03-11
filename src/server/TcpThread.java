package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class TcpThread extends Thread {
    private final ServerSocket serverSocket;
    private final List<ClientThread> clientThreads;

    public TcpThread(ServerSocket serverSocket, List<ClientThread> clientThreads) {
        this.serverSocket = serverSocket;
        this.clientThreads = clientThreads;
    }

    @Override
    public void run() {
        while (true) try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted client");

            ClientThread clientThread = new ClientThread(clientSocket, clientThreads);
            clientThread.start();
            clientThreads.add(clientThread);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
