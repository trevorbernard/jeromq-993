#!/usr/bin/env bash

#sudo ip link add dummy0 type dummy
#sudo ip link set dummy0 up

sudo ip link add dummy0 type dummy
#sudo ip link set dummy0 multicast on
sudo ip addr add 10.10.0.1/24 dev dummy0
sudo ip link set dummy0 up
