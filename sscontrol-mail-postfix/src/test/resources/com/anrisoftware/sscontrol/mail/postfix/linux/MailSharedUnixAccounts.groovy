package com.anrisoftware.sscontrol.mail.postfix.linux

mail {
	bind_addresses all

	relay "smtp.relayhost.com"
	name "mail.example.com"
	origin "example.com"
	destinations "foo.bar", "bar.bar"

	masquerade {
		domains "mail.example.com"
		users "root"
	}
}
