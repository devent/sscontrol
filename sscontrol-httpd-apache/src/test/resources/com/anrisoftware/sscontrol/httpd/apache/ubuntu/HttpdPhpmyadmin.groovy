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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*

httpd {
	domain "test1.com", address: "192.168.0.50", port: 80, { redirect to_www }
	ssl_domain "test1.com", address: "192.168.0.50", {
		certification_file certCrt.resource
		certification_key_file certKey.resource
		redirect to_www
	}
	ssl_domain "phpadmin.test1.com", address: "192.168.0.50", {
		certification_file certCrt.resource
		certification_key_file certKey.resource
		setup "phpmyadmin", alias: "phpmyadmin", {
			admin "root", password: "rootpass"
			control "phpmyadmin", password: "somepass", database: "phpmyadmin"
			server "127.0.0.1", port: 3306
		}
	}
}
