/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.ubuntu_10_04

import com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.UbuntuResources

httpd {
	domain "test1.com", address: "192.168.0.50", { //.
		redirect to_www //.
	}
	ssl_domain "test1.com", address: "192.168.0.50", {
		certification_file UbuntuResources.certCrt.resource
		certification_key_file UbuntuResources.certKey.resource
		redirect to_www
	}
	ssl_domain "ldapadmin.test1.com", address: "192.168.0.50", {
		user "www-data", group: "www-data"
		certification_file UbuntuResources.certCrt.resource
		certification_key_file UbuntuResources.certKey.resource
		setup "phpldapadmin", alias: "ldapadmin", {
			server "Local LDAP/Server", host: "127.0.0.1", port: "ldap"
			server "Example.com LDAP/Server", host: "ldap.example.com", port: 389
			server "Secured LDAP/Server", host: "ldaps://ldap.example.com/"
		}
	}
}
