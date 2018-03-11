package client;

import java.net.MulticastSocket;

public class MulticastThread extends UdpThread {

    public MulticastThread(MulticastSocket multicastSocket) {
        super(multicastSocket);
    }
}
