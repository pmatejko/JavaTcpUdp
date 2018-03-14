package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class UdpThread extends Thread {
    private final DatagramSocket datagramSocket;
    private final List<Client> clients;

    public UdpThread(DatagramSocket datagramSocket, List<Client> clients) {
        this.datagramSocket = datagramSocket;
        this.clients = clients;
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

                String msg = new String(receivePacket.getData());
                byte[] sendBuffer = msg.getBytes();
                System.out.println("received udp msg:\n" + msg);

                for (Client client : clients) {
                    InetAddress clientAddress = client.getInetAddress();
                    int clientPort = client.getPort();

                    if (senderPort != clientPort || !senderAddress.equals(clientAddress)) {
                        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                        datagramSocket.send(sendPacket);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
