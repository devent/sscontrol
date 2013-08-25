/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu
dns {
	serial 1, generate: false

	// bind the dns server to address only
	bind_address "127.0.0.1"

	// soa entry, default timers
	zone "example1.com", "ns.example1.com", "hostmaster@example1.com"

	// adds IPv4 address alias
	alias "localhost" address "127.0.0.1"
	alias "vbox" address "10.0.2.2"

	// sets the group to the root servers
	roots {
		servers "icann"
	}

	// sets recursive host
	recursive {
		servers "localhost"
	}

	// soa entry, default timers
	zone "example1.com", "ns.example1.com", "hostmaster@example1.com"
}