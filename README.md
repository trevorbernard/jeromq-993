# JeroMQ Issue #993

## Overview

The goal of this repository is to reliably recreate Issue
[#993](https://github.com/zeromq/jeromq/issues/993) in an automated
fashion and establish it as a regression test. This issue pertains to
unexpected reconnection attempts by a `DEALER` socket after the
connection has been closed, which could lead to unnecessary resource
consumption and potential network instability.

## Establishing a Control with cppzmq

To establish a control and baseline, cppzmq will be used to verify the
expected behavior of DEALER/ROUTER sockets under network instability
conditions. The expected behavior is that once a DEALER socket is
closed, the associated file descriptor should never attempt to
reconnect.

### Steps:

1. Setup the cppzmq Environment: Create a simple cppzmq-based program
   to simulate the DEALER/ROUTER interaction.

2. Simulate Network Instability: Introduce network instability using a
   dummy network device.

3. Verify Expected Behavior: Ensure that no reconnection attempts are
   made once the DEALER socket is closed.

## Creating a Dummy Network Device

To simulate network outages, we will create a dummy network device
that can be programmatically brought up and down.

``` bash
# Create device
sudo ip link add dummy0 type dummy
sudo ip addr add 10.10.0.1/24 dev dummy0
sudo ip link set dummy0 up
# Delete device
sudo ip link delete dummy0
```

This allows us to simulate network outages by toggling the state of
dummy0.

## Sniffing Network Packets

We want to capture all SYN and SYN-ACK packets to detect any
extraneous connection attempts from previously closed sockets.

``` bash
sudo $(which tcpdump) -i lo 'tcp[13] & 0x02 != 0 and port 5555'
```

### Explanation:

* `tcp[13]`: This refers to the TCP flags field in the TCP header.

* `& 0x02 != 0`: This performs a bitwise AND operation with `0x02`
  (the SYN flag) to check if the SYN flag is set.

* and port `5555`: Filters packets on port `5555`.

This command will capture packets with the SYN flag set on port `5555`
on the loopback interface (`lo`).
