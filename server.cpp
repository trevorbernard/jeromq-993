#include <zmq.hpp>
#include <string>
#include <iostream>

int main() {
    zmq::context_t context(1);
    zmq::socket_t router(context, zmq::socket_type::router);

    router.bind("tcp://10.10.0.1:5555");

    while (true) {
        zmq::message_t identity;
        zmq::message_t message;

        // Receive identity
        router.recv(identity, zmq::recv_flags::none);
        std::string identity_str(static_cast<char*>(identity.data()), identity.size());
        std::cout << "Received identity: " << identity_str << std::endl;

        // Receive message
        router.recv(message, zmq::recv_flags::none);
        std::string message_str(static_cast<char*>(message.data()), message.size());
        std::cout << "Received message: " << message_str << std::endl;

        // Send reply
        router.send(identity, zmq::send_flags::sndmore);
        std::string reply = "Reply from server";
        zmq::message_t reply_msg(reply.data(), reply.size());
        router.send(reply_msg, zmq::send_flags::none);
    }
    return 0;
}
