package org.example;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.zeromq.SocketType.DEALER;

public class DealerClient {
    static ZContext context = new ZContext();

    public static void main(String[] args) throws Exception {
        var threads = new Thread[3];
        var endpoint = "tcp://10.10.0.1:5555";

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                System.out.println("Spawning dealer client " + idx);
                try (var dealer = context.createSocket(DEALER)) {
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
                    int msgCount = 0;
                    while (!Thread.currentThread().isInterrupted()) {
                        dealer.send("Hello from client[" + idx + "]: " + msgCount++, 0);
                        var reply = dealer.recvStr(0);
                        System.out.println("Received reply: " + reply);

                        Thread.sleep(2000);
                    }
                } catch (Exception ex) {
                    System.err.println("Context or Socket unexpectedly closed...");
                    ex.printStackTrace();
                }
            });
            threads[i].start();
        }

        Thread.sleep(20000);

        System.out.println("Start interrupting the worker threads");

        for (int i = 0; i < 5; i++) {
            System.out.println("Interrupting Thread " + i);
            threads[i].interrupt();
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.sleep(5000);
        }

        threads[4].join();
        System.out.println("All threads have been interrupted.");
    }
}
