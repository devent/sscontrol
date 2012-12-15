package com.anrisoftware.sscontrol.dns.service
dns {
	zone "testa.com", "ns1.testa.com", "hostmaster@testa.com", {
		mx_record "mx1.testa.com", "192.168.0.49", { priority 20 }
		mx_record "mx2.testa.com", "192.168.0.50"
	}
}
