package server;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class JavaServer {
    private static final int PORT_NUMBER = 12345;
    private final List<ClientThread> clientThreads;
    private final BlockingQueue<Message> messageQueue;

    public JavaServer() {
        System.out.println("JAVA SERVER");
        clientThreads = new CopyOnWriteArrayList<>();
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void start() throws IOException, InterruptedException {
        try (
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            DatagramSocket datagramSocket = new DatagramSocket(PORT_NUMBER)
        ) {
            List<Thread> threads = List.of(new TcpAcceptThread(serverSocket, clientThreads, messageQueue),
                    new TcpSenderThread(clientThreads, messageQueue),
                    new UdpThread(datagramSocket, clientThreads));

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        JavaServer javaServer = new JavaServer();
        javaServer.start();
    }

}
