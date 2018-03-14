package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class TcpAcceptThread extends Thread {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final List<Client> clients;
    private final BlockingQueue<Message> messageQueue;

    public TcpAcceptThread(ServerSocket serverSocket, ExecutorService executorService,
                           List<Client> clients, BlockingQueue<Message> messageQueue) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
        this.clients = clients;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted client");

            Client client = new Client(clientSocket);
            ClientRunnable clientRunnable = new ClientRunnable(client, clients, messageQueue);

            executorService.submit(clientRunnable);
            clients.add(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
