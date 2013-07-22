package com.anrisoftware.sscontrol.mail.postfix.linux

mail {
	bind_addresses all

	relay "smtp.relayhost.com"
	name "mail.example.com"
	origin "example.com"
	database "maildb" user "root" password "password"

	masquerade {
		domains "mail.example.com"
		users "root"
	}

	domain "localhost.localdomain", { catchall destination: "@localhost" }
	domain "localhost", {
		alias "postmaster", destination: "root"
		alias "sysadmin", destination: "root"
		alias "webmaster", destination: "root"
		alias "abuse", destination: "root"
		alias "root", destination: "root"
		catchall destination: "root"
		user "root", password: "rootpasswd"
	}
}
