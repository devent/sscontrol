package com.anrisoftware.sscontrol.dns.service
dns {
	zone "test.com", "ns1.test.com", "hostmaster@test.com", {
		a_record "test.com", "192.168.0.49", { ttl 1  }
		a_record "test.com", "192.168.0.50"
	}
}
