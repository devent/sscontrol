package com.anrisoftware.sscontrol.dns.service
host = "test-automatic-a-record-for-soa.com"
dns {
	serial 1
	bind_address "127.0.0.1"
	zone "$host", "ns1.$host", "hostmaster@$host", "192.168.0.49"
}
