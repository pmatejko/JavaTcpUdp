package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientThread extends Thread {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BlockingQueue<Message> messageQueue;
    private String nickname;

    public ClientThread(Socket clientSocket, BlockingQueue<Message> messageQueue) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.messageQueue = messageQueue;
    }

    public InetAddress getInetAddress() {
        return clientSocket.getInetAddress();
    }

    public int getPort() {
        return clientSocket.getPort();
    }

    public String getNickname() {
        return nickname;
    }

    public PrintWriter getPrintWriter() {
        return out;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            nickname = in.readLine();

            while (true) {
                String msg = in.readLine();
                messageQueue.add(new Message(this, msg));
            }
        } catch (IOException e) {
            System.err.println("Lost connection with client: " + nickname);
        }
    }
}
