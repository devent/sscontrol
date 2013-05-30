package com.anrisoftware.sscontrol.mail.postfix.linux

mail {
	bind_addresses all

	relay "smtp.relayhost.com"
	name "mail.example.com"
	origin "example.com"

	masquerade {
		domains "mail.example.com"
		users "root"
	}

	destinations "foo.bar", "bar.bar"

	certificate file: "$tmp/example-com.crt", key: "$tmp/example-com.insecure.key", ca: "$tmp/example-com-ca.crt"
}
