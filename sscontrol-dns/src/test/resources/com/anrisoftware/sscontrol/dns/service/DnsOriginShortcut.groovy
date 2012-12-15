package com.anrisoftware.sscontrol.dns.service
dns {
	zone "testa.com", "ns1.%", "hostmaster@%", {
		a_record "%", "192.168.0.49"
		ns_record "ns2.%", "192.168.0.50"
		mx_record "mx1.%", "192.168.0.51"
		cname_record "www.%", "%"
	}
}
