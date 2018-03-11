package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpThread extends Thread {
    private final Socket socket;

    public TcpThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String message = in.readLine();
                System.out.println("     " + message);
            }
        } catch (IOException e) {
            System.err.println("Lost connection with server");
        }
    }
}
