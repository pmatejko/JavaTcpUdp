package server;

import server.dto.Client;
import server.dto.Message;
import server.dto.Protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class UdpThread extends Thread {
    private final DatagramSocket datagramSocket;
    private final List<Client> clients;
    private final BlockingQueue<Message> messageQueue;

    public UdpThread(DatagramSocket datagramSocket, List<Client> clients, BlockingQueue<Message> messageQueue) {
        this.datagramSocket = datagramSocket;
        this.clients = clients;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try {
            byte[] receiveBuffer = new byte[10000];

            while (true) {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);
                InetAddress senderAddress = receivePacket.getAddress();
                int senderPort = receivePacket.getPort();

                String content = new String(receivePacket.getData());
                Client author = clients.stream()
                        .filter(c -> senderPort == c.getPort() && senderAddress.equals(c.getInetAddress()))
                        .findFirst()
                        .get();
                Message message = new Message(author, content, Protocol.UDP);
                messageQueue.add(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
