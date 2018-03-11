package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {
    private final Socket clientSocket;
    private final List<ClientThread> clientThreads;
    private String clientNick;

    public ClientThread(Socket clientSocket, List<ClientThread> clientThreads) {
        this.clientSocket = clientSocket;
        this.clientThreads = clientThreads;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientNick = in.readLine();

            while (true) {
                String msg = clientNick + ": " + in.readLine();
                System.out.println(msg);

                for (ClientThread clientThread: clientThreads) {
                    if (clientThread != this) {
                        PrintWriter out = new PrintWriter(clientThread.getClientSocket().getOutputStream(), true);
                        out.println(msg);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lost connection with client: " + clientNick);
        }
    }
}
