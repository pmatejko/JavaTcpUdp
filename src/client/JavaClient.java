package client;

import java.io.IOException;
import java.net.*;

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
        Socket socket = null;
        DatagramSocket datagramSocket = null;
        MulticastSocket multicastSocket = null;

        try {
            socket = new Socket(HOST_NAME, SERVER_PORT_NUMBER);
            datagramSocket = new DatagramSocket(socket.getLocalPort());
            multicastSocket = new MulticastSocket(MULTICAST_PORT_NUMBER);
            multicastSocket.joinGroup(MULTICAST_GROUP);

            WriterThread writerThread = new WriterThread(socket, datagramSocket, multicastSocket, MULTICAST_GROUP);
            TcpThread tcpThread = new TcpThread(socket);
            UdpThread udpThread = new UdpThread(datagramSocket);
            MulticastThread multicastThread = new MulticastThread(multicastSocket);

            writerThread.start();
            tcpThread.start();
            udpThread.start();
            multicastThread.start();

            writerThread.join();
            tcpThread.join();
            udpThread.join();
            multicastThread.join();
        } finally {
            if (socket != null)
                socket.close();
            if (datagramSocket != null)
                datagramSocket.close();
            if (multicastSocket != null)
                multicastSocket.close();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JavaClient javaClient = new JavaClient();
        javaClient.start();
    }

}
