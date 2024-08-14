package org.example;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.zeromq.SocketType.ROUTER;

public class App {
    public static void main(String[] args) throws Exception {
        var endpoint = "tcp://*:" + 60000;
        try (var context = new ZContext();
             var router = context.createSocket(ROUTER)) {
            // Initialize router before bind
            router.setTCPKeepAlive(1);
            router.setTCPKeepAliveCount(1);
            router.setTCPKeepAliveIdle(8);
            router.setTCPKeepAliveInterval(5);
            router.setLinger(0);
            router.setHandshakeIvl(0);
            router.setSendTimeOut(5000);
            router.setReceiveTimeOut(5000);

            router.bind(endpoint);
            // Register socket to poller of size 1
            try (var poller = context.createPoller(1)) {
                poller.register(router, ZMQ.Poller.POLLIN);

                while (!Thread.currentThread().isInterrupted()) {
                    poller.poll(500);

                    // Check to see if we have waiting messages
                    if (poller.pollin(0)) {
                        // Receive identity
                        var identity = router.recv(0);
                        System.out.println("Received identity: " + new String(identity));

                        // Receive message
                        var message = router.recvStr(0);
                        System.out.println("Received message: " + message);

                        // Send reply
                        router.send(identity, ZMQ.SNDMORE);
                        var reply = "Reply from server";
                        router.send(reply, 0);
                    }
                }
                System.out.println("Main thread interrupted. Closing poller.");
            }
        } catch (Exception e) {
            System.err.println("Context or Socket unexpectedly closed..");
            e.printStackTrace();
        }
        System.out.println("All Jeromq resources have been successfully released");
    }
}