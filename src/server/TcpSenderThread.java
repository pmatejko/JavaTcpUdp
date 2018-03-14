package server;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TcpSenderThread extends Thread {
    private final List<ClientThread> clientThreads;
    private final BlockingQueue<Message> messageQueue;

    public TcpSenderThread(List<ClientThread> clientThreads, BlockingQueue<Message> messageQueue) {
        this.clientThreads = clientThreads;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) try {
            Message message = messageQueue.take();
            ClientThread authorThread = message.getAuthor();
            String messageString = authorThread.getNickname() + ": " + message.getContent();
            System.out.println(messageString);

            for (ClientThread clientThread : clientThreads) {
                if (clientThread != authorThread) {
                    clientThread.getPrintWriter()
                            .println(messageString);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
