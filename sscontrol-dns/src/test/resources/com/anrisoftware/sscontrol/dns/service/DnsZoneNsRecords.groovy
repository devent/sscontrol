package com.anrisoftware.sscontrol.dns.service
dns {
	zone "test.com", "ns1.test.com", "hostmaster@test.com", {
		ns_record "ns1.test.com", "127.0.0.1", {  ttl 1  }
		ns_record "ns2.test.com", "192.168.0.49"
	}
}
