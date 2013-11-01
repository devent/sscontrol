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
	serial 1

	// bind the dns server to localhost only
	bind address: "127.0.0.1"

	// soa entry, default timers
	zone "example1.com", primary: "ns.example1.com", email: "hostmaster@example1.com"

	// soa entry, with timers
	zone "example2.com", primary: "ns.example2.com", email: "hostmaster@example2.com", {
		ttl duration: "PT1S", minimum: "PT2S"
		refresh duration: "PT2S"
		retry duration: "PT3S"
		expire duration: "PT4S"
	}

	// soa entry, default timers
	zone "anrisoftware.com", primary: "ns1.anrisoftware.com", email: "hostmaster@anrisoftware.com", {

		// A record with ttl
		ns_record "ns1.anrisoftware.com", "127.0.0.1", {  ttl 1  }

		// NS and A record with default ttl, and the domain name ns2.anrisoftware.com
		ns_record "ns2.anrisoftware.com", "192.168.0.49"

		// A record with default ttl
		a_record "anrisoftware.com", "192.168.0.49"

		// A record with ttl
		a_record "anrisoftware.com", "192.168.0.50", { ttl 1  }

		// MX and A record with default ttl, and the domain name mx1.anrisoftware.com, priority 10
		mx_record "mx1.anrisoftware.com", "192.168.0.49"

		// MX and A record with default ttl, and the domain name mx2.anrisoftware.com
		mx_record "mx2.anrisoftware.com", "192.168.0.49", { priority 20 }

		// CNAME record with default ttl
		cname_record "www.anrisoftware.com", "anrisoftware.com"

		// CNAME record with ttl
		cname_record "www.anrisoftware.com", "anrisoftware.com", { ttl 1 }
	}

}
