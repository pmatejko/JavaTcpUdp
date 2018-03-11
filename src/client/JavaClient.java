package client;

import java.io.IOException;
import java.net.*;
import java.util.List;

public class JavaClient {
    private final String HOST_NAME = "localhost";
    private final int SERVER_PORT_NUMBER = 12345;
    private final InetAddress MULTICAST_GROUP;
    private final int MULTICAST_PORT_NUMBER = 12346;


    public JavaClient() throws UnknownHostException {
        System.out.println("JAVA CLIENT");
        MULTICAST_GROUP = InetAddress.getByName("228.5.6.7");
    }


    public void start() throws IOException, InterruptedException {

        try (
            Socket socket = new Socket(HOST_NAME, SERVER_PORT_NUMBER);
            DatagramSocket datagramSocket = new DatagramSocket(socket.getLocalPort());
            MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_PORT_NUMBER);
        ) {
            multicastSocket.joinGroup(MULTICAST_GROUP);

            List<Thread> threads = List.of(new WriterThread(socket, datagramSocket, multicastSocket, MULTICAST_GROUP),
                    new TcpThread(socket), new UdpThread(datagramSocket), new MulticastThread(multicastSocket));

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JavaClient javaClient = new JavaClient();
        javaClient.start();
    }

}
