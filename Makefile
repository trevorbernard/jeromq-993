CXX = g++
CXXFLAGS = -std=c++11 -Wall
LDFLAGS = -lzmq

all: server client

server: server.cpp
	$(CXX) $(CXXFLAGS) -o server server.cpp $(LDFLAGS)

client: client.cpp
	$(CXX) $(CXXFLAGS) -o client client.cpp $(LDFLAGS)

clean:
	rm -f server client
