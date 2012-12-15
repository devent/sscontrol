package com.anrisoftware.sscontrol.dns.service
dns {
	zone "testa.com", "ns1.testa.com", "hostmaster@testa.com", {
		a_record "testa.com", "192.168.0.49"
		ns_record "ns2.testa.com"
		mx_record "mx1.testa.com"
		cname_record "www.testa.com", "testa.com"
	}
}
