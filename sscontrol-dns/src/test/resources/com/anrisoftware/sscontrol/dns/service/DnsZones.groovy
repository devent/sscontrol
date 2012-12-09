package com.anrisoftware.sscontrol.dns.service
dns {
	zone "example1.com", "ns.example1.com", "hostmaster@example1.com"
	zone "example2.com", "ns.example2.com", "hostmaster@example2.com", {
		ttl 1
		refresh 2
		retry 3
		expire 4
		minimum_ttl 5
	}
}
