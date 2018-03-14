package server;

import server.dto.Client;
import server.dto.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SenderThread extends Thread {

    private final DatagramSocket datagramSocket;
    private final List<Client> clients;
    private final BlockingQueue<Message> messageQueue;

    public SenderThread(DatagramSocket datagramSocket, List<Client> clients, BlockingQueue<Message> messageQueue) {
        this.datagramSocket = datagramSocket;
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
                    switch (message.getProtocol()) {
                        case TCP:
                            sendTcpMessage(client, messageString);
                            break;
                        case UDP:
                            sendUdpMessage(client, messageString);
                            break;
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendTcpMessage(Client client, String messageString) {
        client.getPrintWriter()
                .println(messageString);
    }

    private void sendUdpMessage(Client client, String messageString) throws IOException {
        byte[] sendBuffer = messageString.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.getInetAddress(), client.getPort());
        datagramSocket.send(sendPacket);
    }
}
