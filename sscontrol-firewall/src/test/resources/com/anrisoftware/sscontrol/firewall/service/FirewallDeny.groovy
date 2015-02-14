/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.service
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
