/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service

mail {
	reset domains: yes, users: no, aliases: no
	debug logging: 1
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
