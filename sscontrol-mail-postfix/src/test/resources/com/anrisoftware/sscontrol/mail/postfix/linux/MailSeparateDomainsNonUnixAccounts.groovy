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

	domain "example.com", {
		user "info"
		user "sales"
		user "disabled-user", { enabled false }
		alias "postmaster", destination: "postmaster"
		alias "disabled-alias", destination: "postmaster", { enabled false }
		catchall destination: "jim"
	}

	domain "disabled.domain", { enabled false }
}
