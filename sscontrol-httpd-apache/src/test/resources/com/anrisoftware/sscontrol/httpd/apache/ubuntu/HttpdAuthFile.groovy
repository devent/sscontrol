package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*

httpd {
	domain "test1.com", address: "192.168.0.50", {
		redirect to_www
		redirect http_to_https
	}
	ssl_domain "test1.com", address: "192.168.0.50", {
		certification_file certCrt.resource
		certification_key_file certKey.resource
		redirect to_www
		setup_auth provider: file, name: "private", {
			location "private"
			require valid_user
			require group: "admin"
			user "foo", password: "foopassword", group: "admin"
			user "bar", password: "barpassword"
		}
	}
}