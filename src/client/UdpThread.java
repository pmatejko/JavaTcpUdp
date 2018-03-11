package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UdpThread extends Thread {
    private final DatagramSocket datagramSocket;

    public UdpThread(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[10000];

        while(true) try {
            Arrays.fill(receiveBuffer, (byte)0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            datagramSocket.receive(receivePacket);

            String msg = new String(receivePacket.getData());
            System.out.println("received udp msg:\n" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
