/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service
dns {
	// serial number of SOA records
	serial 1, generate: false

	// bind the dns server to localhost only
	bind address: "127.0.0.1"

	// adds IPv4 address alias
	alias "localhost" address "127.0.0.1"

	// sets the group to the root servers
	roots { servers "icann" }

	// sets recursive host
	recursive { servers "localhost" }

	// soa entry, default timers
	zone "example1.com", primary: "ns.example1.com", email: "hostmaster@example1.com"
}
