package server;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TcpSenderThread extends Thread {
    private final List<Client> clients;
    private final BlockingQueue<Message> messageQueue;

    public TcpSenderThread(List<Client> clients, BlockingQueue<Message> messageQueue) {
        this.clients = clients;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) try {
            Message message = messageQueue.take();
            Client author = message.getAuthor();
            String messageString = author.getNickname() + ": " + message.getContent();
            System.out.println(messageString);

            for (Client client : clients) {
                if (client != author) {
                    client.getPrintWriter()
                            .println(messageString);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
