package com.anrisoftware.sscontrol.dns.service
dns {
	zone "test.com", "ns1.test.com", "hostmaster@test.com", {
		cname_record "www.test.com", "test.com"
		cname_record "www.test.com", "test.com", { ttl 1 }
	}
}
