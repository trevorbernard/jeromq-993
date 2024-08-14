#include <zmq.hpp>
#include <string>
#include <iostream>

int main() {
    zmq::context_t context(1);
    zmq::socket_t dealer(context, zmq::socket_type::dealer);

    dealer.connect("tcp://10.10.0.1:5555");

    std::string message = "Hello from client";
    zmq::message_t request(message.data(), message.size());
    dealer.send(request, zmq::send_flags::none);

    zmq::message_t reply;
    dealer.recv(reply, zmq::recv_flags::none);
    std::string reply_str(static_cast<char*>(reply.data()), reply.size());
    std::cout << "Received reply: " << reply_str << std::endl;

    return 0;
}
