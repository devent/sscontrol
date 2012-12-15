package com.anrisoftware.sscontrol.dns.service
dns {
	zone "testa.com", "ns1.testa.com", "hostmaster@testa.com", {
		cname_record "www.testa.com", "testa.com"
		cname_record "www.testb.com", "testb.com", { ttl 1 }
	}
}
