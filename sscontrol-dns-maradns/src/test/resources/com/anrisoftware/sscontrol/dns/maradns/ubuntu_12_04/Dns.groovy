/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_12_04
dns {
	serial 1, generate: false

	// bind the dns server to address only
	bind address: "127.0.0.1"

	// soa entry, default timers
	zone "example1.com", primary: "ns.example1.com", email: "hostmaster@example1.com"

	// soa entry, with timers
	zone "example2.com", primary: "ns.example2.com", email: "hostmaster@example2.com", {
		ttl duration: "PT1S", minimum: "PT5S"
		refresh duration: "PT2S"
		retry duration: "PT3S"
		expire duration: "PT4S"
	}

	// soa entry, default timers
	zone "anrisoftware.com", primary: "ns1.anrisoftware.com", email: "hostmaster@anrisoftware.com", {

		// A record with ttl
		record ns, name: "ns1.anrisoftware.com", address: "127.0.0.1", {  ttl duration: "PT1S"  }

		// NS and A record
		record ns, name: "ns2.anrisoftware.com", address: "192.168.0.49"

		// A record
		record a, name: "anrisoftware.com", address: "192.168.0.49"

		// A record with ttl
		record a, name: "anrisoftware.com", address: "192.168.0.50", { ttl duration: "PT1S"  }

		// MX and A record and the domain name mx1.anrisoftware.com
		record mx, name: "mx1.anrisoftware.com", address: "192.168.0.49"

		// MX and A record with priority
		record mx, name: "mx2.anrisoftware.com", address: "192.168.0.49", priority: 20

		// CNAME record
		record cname, name: "www.anrisoftware.com", alias: "anrisoftware.com"

		// CNAME record with ttl
		record cname, name: "www.anrisoftware.com", alias: "anrisoftware.com", { ttl duration: "PT1S" }
	}

}
