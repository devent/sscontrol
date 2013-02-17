package com.anrisoftware.sscontrol.firewall.ufw.ubuntu
firewall {

	// deny from all ports, all addresses to all ports to all addresses
	deny

	// deny port 22/tcp/udp on the host to anywhere
	deny port: 22

	// deny port 22/tcp on the host to anywhere
	deny port: 22, proto: tcp

	// deny port 22/udp on the host to anywhere
	deny port: 22, proto: udp

	// deny port 22/tcp/udp on the host to anywhere
	deny port: "ssh"

	// deny from anywhere to anywhere on the host
	deny from: any to any

	// deny from anywhere port 22 to anywhere port 23 on the host
	deny from: any, port: 22 to any, port: 23

	// deny from anywhere port 22 to anywhere port 23 on the host
	deny from: any, port: "ssh" to any, port: "www"

	// deny from 192.168.0.1 to anywhere port 23 on the host
	deny from: "192.168.0.1" to any, port: 23

	// deny from 192.168.0.1 port 22 to 127.0.0.1 port 23 on the host
	deny from: "192.168.0.1", port: 22 to "127.0.0.1", port: 23

	// deny from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	deny from: "192.168.0.1", port: 22, proto: tcp to "127.0.0.1", port: 23, proto: udp

	// deny from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	deny from: "192.168.0.1", port: "ssh", proto: tcp to "127.0.0.1", port: 23, proto: udp

	// deny from 192.168.0.1 port ssh/tcp to 127.0.0.1 port http/udp on the host
	deny from: "192.168.0.1", port: "ssh", proto: tcp to "127.0.0.1", port: "http", proto: udp

	// deny from 192.168.0.1 port 22/udp to 127.0.0.1 port 23/tcp on the host
	deny from: "192.168.0.1", port: 22, proto: udp to "127.0.0.1", port: 23, proto: tcp

}
