package server;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;

public class JavaServer {
    private static final int PORT_NUMBER = 12345;
    private final List<Client> clients;
    private final BlockingQueue<Message> messageQueue;

    public JavaServer() {
        System.out.println("JAVA SERVER");
        clients = new CopyOnWriteArrayList<>();
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void start() throws IOException, InterruptedException {
        try (
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            DatagramSocket datagramSocket = new DatagramSocket(PORT_NUMBER)
        ) {
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            List<Thread> threads = List.of(new TcpAcceptThread(serverSocket, executorService, clients, messageQueue),
                    new TcpSenderThread(clients, messageQueue),
                    new UdpThread(datagramSocket, clients));

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
