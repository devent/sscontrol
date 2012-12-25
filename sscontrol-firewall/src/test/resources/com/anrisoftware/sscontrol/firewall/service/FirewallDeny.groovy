package com.anrisoftware.sscontrol.firewall.service
firewall {

	// deny from all ports, all addresses to all ports to all addresses
	deny

	// deny port 22/tcp/udp on the host to anywhere
	deny 22

	// deny port 22/tcp on the host to anywhere
	deny 22, tcp

	// deny port 22/udp on the host to anywhere
	deny 22, udp

	// deny port 22/tcp/udp on the host to anywhere
	deny "ssh"

	// deny from anywhere to anywhere on the host
	deny_from any to any

	// deny from anywhere port 22 to anywhere port 23 on the host
	deny_from any, 22 to any, 23

	// deny from anywhere port 22 to anywhere port 23 on the host
	deny_from any, "ssh" to any, "www"

	// deny from 192.168.0.1 to anywhere port 23 on the host
	deny_from "192.168.0.1" to any, 23

	// deny from 192.168.0.1 port 22 to 127.0.0.1 port 23 on the host
	deny_from "192.168.0.1", 22 to "127.0.0.1", 23

	// deny from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	deny_from "192.168.0.1", 22, tcp to "127.0.0.1", 23, udp

	// deny from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	deny_from "192.168.0.1", "ssh", tcp to "127.0.0.1", 23, udp

	// deny from 192.168.0.1 port 22/tcp to 127.0.0.1 port 23/udp on the host
	deny_from "192.168.0.1", "ssh", tcp to "127.0.0.1", "web", udp

	// deny from 192.168.0.1 port 22/udp to 127.0.0.1 port 23/tcp on the host
	deny_from "192.168.0.1", 22, udp to "127.0.0.1", 23, tcp

}
