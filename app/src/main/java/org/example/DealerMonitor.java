package org.example;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import static org.zeromq.SocketType.DEALER;
import static org.zeromq.SocketType.PAIR;

public class DealerMonitor {
    public static void main(String[] args) throws Exception {
        var endpoint = "tcp://10.10.0.1:5555";
        var pairEndpoint = "inproc://monitor";
        try (var context = new ZContext();
             var dealer = context.createSocket(DEALER);
             var monitor = context.createSocket(PAIR);
             var poller = context.createPoller(2)) {
            poller.register(dealer, ZMQ.Poller.POLLIN);
            poller.register(monitor, ZMQ.Poller.POLLIN);
            // Initialize dealer before connect
            dealer.setLinger(0);

//            dealer.setHandshakeIvl(0);
//            dealer.setReconnectIVL(30000);
//            dealer.setSendTimeOut(5000);
//            dealer.setReceiveTimeOut(5000);
//            dealer.setHeartbeatIvl(1000);
//            dealer.setHeartbeatTimeout(2000);
//            dealer.setHeartbeatTtl(3000);
//            dealer.setReconnectIVL(-1);


            monitor.connect(pairEndpoint);
            dealer.monitor(pairEndpoint, zmq.ZMQ.ZMQ_EVENT_ALL);

            dealer.connect(endpoint);
            System.out.println("Starting the Dealear Monitor");
            while(!Thread.currentThread().isInterrupted()) {
                poller.poll();
                // Dealer socket
                if(poller.pollin(0)) {
                    dealer.send("Hello from client", 0);
                    var reply = dealer.recvStr(0);
                    System.out.println("Received reply: " + reply);
                    Thread.sleep(2000);
                }
                // Montior socket
                else if (poller.pollin(1)) {
                    var event = zmq.ZMQ.Event.read(monitor.base());
                    if(event.event == zmq.ZMQ.ZMQ_EVENT_DISCONNECTED) {
                        System.out.println("DISCONNECTED EVENT FIRED");
                    }
                    else {
                        System.out.println("Event:" + event.event);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Context or Socket unexpectedly closed..");
            e.printStackTrace();
        }
        System.out.println("All Jeromq resources have been successfully released");
    }
}
