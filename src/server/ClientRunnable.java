package server;

import server.dto.Client;
import server.dto.Message;
import server.dto.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ClientRunnable implements Runnable {
    private final Client client;
    private final List<Client> clients;
    private final BlockingQueue<Message> messageQueue;

    public ClientRunnable(Client client, List<Client> clients, BlockingQueue<Message> messageQueue) {
        this.client = client;
        this.clients = clients;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = client.getBufferedReader();
            client.setNickname(in.readLine());

            while (true) {
                String content = in.readLine();
                Message message = new Message(client, content, Protocol.TCP);
                messageQueue.add(message);
            }
        } catch (IOException e) {
            System.err.println("Lost connection with client: " + client.getNickname());
            clients.remove(client);
        }
    }
}
