package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class WriterThread extends Thread {
    private final Socket socket;
    private final DatagramSocket datagramSocket;
    private final MulticastSocket multicastSocket;

    private final InetAddress multicastGroup;

    private final PrintWriter out;
    private final Scanner scanner;


    public WriterThread(Socket socket, DatagramSocket datagramSocket, MulticastSocket multicastSocket,
                        InetAddress multicastGroup) throws IOException {
        this.socket = socket;
        this.datagramSocket = datagramSocket;
        this.multicastSocket = multicastSocket;

        this.multicastGroup = multicastGroup;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(System.in);
    }


    @Override
    public void run() {
        System.out.println("Insert your nickname");
        out.println(scanner.nextLine());

        while (true) {
            System.out.println("Choose: T - TCP, U - UDP, M - Multicast");
            String protocol = scanner.nextLine();

            switch (protocol) {
                case "U":
                    sendUdpMessage();
                    break;
                case "M":
                    sendMulticastMessage();
                    break;
                case "T":
                    sendTcpMessage();
                    break;
            }
        }
    }


    private void sendTcpMessage() {
        System.out.println("Write message");
        String message = scanner.nextLine();
        out.println(message);
        System.out.println("Sent message: " + message);
    }

    private void sendUdpMessage() {
        try {
            byte[] sendBuffer =
                    ("░░░░░░░▄▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▄░░░░░░░\n" +
                    "░░░░░░█░░▄▀▀▀▀▀▀▀▀▀▀▀▀▀▄░░█░░░░░░\n" +
                    "░░░░░░█░█░▀░░░░░▀░░▀░░░░█░█░░░░░\n" +
                    "░░░░░░█░█░░░░░░░░░▄▀▀▄░▀░█░█▄▀▀▄░\n" +
                    "█▀▀█▄░█░█░░▀░░░░░░█░░░▀▄▄█▄▀░░░█░\n" +
                    "▀▄▄░▀██░█▄░░▀░░░▄▄▀░░░░░░░░░░░░▀▄\n" +
                    "░░▀█▄▄█░░█░░░░▄░░█░░░▄█░░░▄░▄█░░█\n" +
                    "░░░░░▀█░░▀▄▀░░░░░█░██░▄░░▄░░▄░███\n" +
                    "░░░░░▄█▄░░░▀▀▀▀▀▀▀▀▄░░▀▀▀▀▀▀▀░▄▀░░\n" +
                    "░░░░█░░▄█▀█▀▀█▀▀▀▀▀▀▀█▀▀█▀█▀▀█░░░░\n" +
                    "░░░░▀▀▀▀░░▀▀▀░░░░░░░░▀▀▀░░▀▀░░░░░").getBytes();

            DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
                    socket.getInetAddress(), socket.getPort());
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMulticastMessage() {
        try {
            byte[] sendBuffer =
                    ("░░░░░░░░░░░░▄▄▄▄░░░░░░░░░░░░░░░░░░░░░░░▄▄▄▄▄\n" +
                    "░░░█░░░░▄▀█▀▀▄░░▀▀▀▄░░░░▐█░░░░░░░░░▄▀█▀▀▄░░░░▀█▄\n" +
                    "░░█░░░░▀░▐▌░░▐▌░░░░░▀░░░▐█░░░░░░░░░▀░▐▌░░▐▌░░░░░█▀\n" +
                    "░▐▌░░░░░░░▀▄▄▀░░░░░░░░░░▐█▄▄░░░░░░░░░▀▄▄▀░░░░░░▐▌\n" +
                    "░█░░░░░░░░░░░░░░░░░░░░░░░░░▀█░░░░░░░░░░░░░░░░░░█\n" +
                    "▐█░░░░░░░░░░░░░░░░░░░░░░░░░░█▌░░░░░░░░░░░░░░░░░░█\n" +
                    "▐█░░░░░░░░░░░░░░░░░░░░░░░░░░█▌░░░░░░░░░░░░░░░░░░█\n" +
                    "░█░░░░░░░░░░░░░░░░░░░░█▄░░░▄█░░░░░░░░░░░░░░░░░░█\n" +
                    "░▐▌░░░░░░░░░░░░░░░░░░░░▀███▀░░░░░░░░░░░░░░░░░░░▐▌\n" +
                    "░░█░░░░░░░░░░░░░░░░░▀▄░░░░░░░░░░▄▀░░░░░░░░░░░░█\n" +
                    "░░░█░░░░░░░░░░░░░░░░░░▀▄▄▄▄▄▄▄▀▀░░░░░░░░░░░░░░█").getBytes();

            DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length,
                    multicastGroup, multicastSocket.getLocalPort());

            multicastSocket.leaveGroup(multicastGroup);
            multicastSocket.send(datagramPacket);
            multicastSocket.joinGroup(multicastGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
