/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

def certFile = ServicesResources.class.getResource "cert_crt.txt"
def certKeyFile = ServicesResources.class.getResource "cert_key.txt"

httpd {
	domain "test1.com", address: "192.168.0.50", port: 80, {
		redirect to_www
		redirect http_to_https
	}
	domain "foo.test1.com", address: "192.168.0.50"
	ssl_domain "test1.com", address: "192.168.0.50", {
		certification_file certFile
		certification_key_file certKeyFile
		redirect to_www
	}
	domain "test2.com", address: "192.168.0.51", root: "test2", {
		redirect to_www
		redirect http_to_https
	}
	ssl_domain "test2.com", address: "192.168.0.51", use: "test2", {
		redirect to_www
		certification_file certFile
		certification_key_file certKeyFile
	}
}
