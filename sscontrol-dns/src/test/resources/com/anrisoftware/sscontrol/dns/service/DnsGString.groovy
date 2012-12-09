package com.anrisoftware.sscontrol.dns.service
gss = "anrisoftware.com"
gssprojects = "anrisoftware-projects.com"

dns {
	bind_address "127.0.0.1"

	zone "$gss", "ns1.$gss", "hostmaster@$gss", {
		ns_record "ns1.$gss", "127.0.0.1"
		a_record "$gss", "192.168.0.49"
		mx_record "mx.$gss", "127.0.0.1"
		cname_record "www.$gss", "$gss"
		cname_record "ftp.$gss", "$gss"
	}

	zone "$gssprojects", "ns1.$gssprojects", "hostmaster@$gss", {
		ns_record "ns1.$gssprojects", "127.0.0.1"
		a_record "$gssprojects", "192.168.0.50"
		mx_record "mx.$gssprojects", "127.0.0.1"
		cname_record "www.$gssprojects", "$gssprojects"
		cname_record "ftp.$gssprojects", "$gssprojects"
	}
}
