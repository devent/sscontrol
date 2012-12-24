package com.anrisoftware.sscontrol.dns.maradns.ubuntu
dns {
	serial 1, false

	// bind the dns server to localhost only
	bind_address "127.0.0.1"

	// soa entry, default timers
	zone "example1.com", "ns.example1.com", "hostmaster@example1.com"

	// soa entry, with timers
	zone "example2.com", "ns.example2.com", "hostmaster@example2.com", {
		ttl 1
		refresh 2
		retry 3
		expire 4
		minimum_ttl 5
	}

	// soa entry, default timers
	zone "anrisoftware.com", "ns1.anrisoftware.com", "hostmaster@anrisoftware.com", {

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
