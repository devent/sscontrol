package com.anrisoftware.sscontrol.dns.service
dns {
	serial 1
	bind_address "127.0.0.1"
	zone "test-origin-shortcut.com", "ns1.%", "hostmaster@%", {
		a_record "%", "192.168.0.49"
		ns_record "ns2.%", "192.168.0.49"
		mx_record "mx1.%", "192.168.0.49"
		cname_record "www.%", "%"
	}
}
