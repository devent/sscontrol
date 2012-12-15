package com.anrisoftware.sscontrol.dns.service
dns {
	zone "testa.com", "ns1.testa.com", "hostmaster@testa.com", {
		ns_record "ns1.testa.com", "192.168.0.49", {  ttl 1  }
		ns_record "ns2.testa.com", "192.168.0.50"
	}
}
