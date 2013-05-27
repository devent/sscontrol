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

	certificate file: "$tmp/example-com.crt", key: "$tmp/example-com.insecure.key", ca: "$tmp/example-com-ca.crt"

	domain "example.com"

	domain "mail.blobber.org", { catchall destination: "@blobber.org" }

	domain "blobber.org", {
		user "xandros", password: "somepass1"
		user "vivita", password: "somepass2"
		user "claudio", password: "somepass3", { enabled false }
		alias "postmaster", destination: "postmaster@localhost"
		alias "abuse", destination: "abuse@localhost"
		alias "karl", destination: "karl.vovianda@galias.com"
		alias "claudio", destination: "claudio.vovianda@galias.com", { enabled false }
	}

	domain "whopper.nu", {
		alias "postmaster", destination: "postmaster@localhost"
		alias "abuse", destination: "abuse@localhost"
		catchall destination: "xandros@blobber.org"
	}

	domain "lala.com", { catchall destination: "@whupper.nu" }

	domain "dudie.com", {
		catchall destination: "@whupper.nu"
		enabled false
	}
}
