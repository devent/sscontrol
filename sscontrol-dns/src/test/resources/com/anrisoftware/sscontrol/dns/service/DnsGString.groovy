/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service
gss = "anrisoftware.com"
gssprojects = "anrisoftware-projects.com"

dns {
	bind "127.0.0.1"

	zone "$gss", primary: "ns1.$gss", email: "hostmaster@$gss", {
		ns_record "ns1.$gss", "127.0.0.1"
		a_record "$gss", "192.168.0.49"
		mx_record "mx.$gss", "127.0.0.1"
		cname_record "www.$gss", "$gss"
		cname_record "ftp.$gss", "$gss"
	}

	zone "$gssprojects", primary: "ns1.$gssprojects", email: "hostmaster@$gss", {
		ns_record "ns1.$gssprojects", "127.0.0.1"
		a_record "$gssprojects", "192.168.0.50"
		mx_record "mx.$gssprojects", "127.0.0.1"
		cname_record "www.$gssprojects", "$gssprojects"
		cname_record "ftp.$gssprojects", "$gssprojects"
	}
}
