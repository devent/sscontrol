package com.anrisoftware.sscontrol.dns.service
host = "test-automatic-records.com"
dns {
	serial 1
	bind_address "127.0.0.1"
	zone "$host", "ns1.$host", "hostmaster@$host", {
		a_record "$host", "192.168.0.49"
		ns_record "ns2.$host", "192.168.0.49"
		mx_record "mx1.$host", "192.168.0.49"
		cname_record "www.$host", "$host"
	}
}
