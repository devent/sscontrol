package com.anrisoftware.sscontrol.dns.service
dns {
	zone "testa.com", "ns1.testa.com", "hostmaster@testa.com", {
		a_record "testa.com", "192.168.0.49", { ttl 1  }
		a_record "testb.com", "192.168.0.50"
		a_record "testc.com", "192.168.0.51"
	}
}
