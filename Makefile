CXX = g++
CXXFLAGS = -std=c++11 -Wall
LDFLAGS = -lzmq

all: router_server dealer_client

server: router_server.cpp
	$(CXX) $(CXXFLAGS) -o router_server router_server.cpp $(LDFLAGS)

client: dealer_client.cpp
	$(CXX) $(CXXFLAGS) -o dealer_client dealer_client.cpp $(LDFLAGS)

clean:
	rm -f router_server dealer_client
