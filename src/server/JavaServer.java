package server;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JavaServer {
    private static final int PORT_NUMBER = 12345;
    private ServerSocket serverSocket;
    private DatagramSocket datagramSocket;
    private List<ClientThread> clientThreads;

    public JavaServer() {
        System.out.println("JAVA SERVER");
        clientThreads = new CopyOnWriteArrayList<>();
    }

    public void start() throws IOException, InterruptedException {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            datagramSocket = new DatagramSocket(PORT_NUMBER);
            System.out.println("Server created");

            TcpThread tcpThread = new TcpThread(serverSocket, clientThreads);
            UdpThread udpThread = new UdpThread(datagramSocket, clientThreads);

            tcpThread.start();
            udpThread.start();

            tcpThread.join();
            udpThread.join();
        } finally {
            if (serverSocket != null)
                serverSocket.close();
            if (datagramSocket != null)
                datagramSocket.close();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JavaServer javaServer = new JavaServer();
        javaServer.start();
    }

}
