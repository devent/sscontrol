package com.anrisoftware.sscontrol.dns.service
dns {
	zone "test.com", "ns1.test.com", "hostmaster@test.com", {
		mx_record "mx1.test.com", "192.168.0.49", { priority 20 }
		mx_record "mx2.test.com", "192.168.0.49"
	}
}
