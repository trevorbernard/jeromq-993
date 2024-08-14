package org.example;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.zeromq.SocketType.DEALER;

public class DealerClient {
    public static void main(String[] args) throws Exception {
        var endpoint = "tcp://10.10.0.1:5555";
        try (var context = new ZContext();
             var dealer = context.createSocket(DEALER)) {
            // Initialize dealer before connect
            dealer.setTCPKeepAlive(1);
            dealer.setTCPKeepAliveCount(1);
            dealer.setTCPKeepAliveIdle(8);
            dealer.setTCPKeepAliveInterval(5);
            dealer.setLinger(0);
            dealer.setHandshakeIvl(0);
            dealer.setReconnectIVL(30000);
            dealer.setSendTimeOut(5000);
            dealer.setReceiveTimeOut(5000);

            dealer.connect(endpoint);
            // Register dealer to poller of size 1

            while (!Thread.currentThread().isInterrupted()) {
                dealer.send("Hello from client", 0);
                var reply = dealer.recvStr(0);
                System.out.println("Received reply: " + reply);

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.err.println("Context or Socket unexpectedly closed..");
            e.printStackTrace();
        }
        System.out.println("All Jeromq resources have been successfully released");
    }
}