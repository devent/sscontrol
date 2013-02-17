package com.anrisoftware.sscontrol.firewall.ufw.ubuntu
firewall {

	// allow from all ports, all addresses to all ports to all addresses
	allow

	// allow port 22/tcp/udp on the host to anywhere
	allow port: 22

	// allow port 22/tcp on the host to anywhere
	allow port: 22, proto: tcp

	// allow port 22/udp on the host to anywhere
	allow port: 22, proto: udp

	// allow port 22/tcp/udp on the host to anywhere
	allow port: "ssh"

	// allow from anywhere to anywhere on the host
	allow from: any to any

	// allow from anywhere port 22 to anywhere port 23 on the host
	allow from: any, port: 22 to any, port: 23

	// allow from anywhere port 22 to anywhere port 23 on the host
	allow from: any, port: "ssh" to any, port: "www"

	// allow from 192.168.0.1 to anywhere port 23 on the host
	allow from: "192.168.0.1" to any, port: 23

	// allow from 192.168.0.1 port 22 to 127.0.0.1 port 23 on the host
	allow from: "192.168.0.1", port: 22 to "127.0.0.1", port: 23

	// allow from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	allow from: "192.168.0.1", port: 22, proto: tcp to "127.0.0.1", port: 23, proto: udp

	// allow from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	allow from: "192.168.0.1", port: "ssh", proto: tcp to "127.0.0.1", port: 23, proto: udp

	// allow from 192.168.0.1 port ssh/tcp to 127.0.0.1 port http/udp on the host
	allow from: "192.168.0.1", port: "ssh", proto: tcp to "127.0.0.1", port: "http", proto: udp

	// allow from 192.168.0.1 port 22/udp to 127.0.0.1 port 23/tcp on the host
	allow from: "192.168.0.1", port: 22, proto: udp to "127.0.0.1", port: 23, proto: tcp

}
