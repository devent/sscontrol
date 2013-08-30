package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*

httpd {
	domain "test1.com", address: "192.168.0.50", port: 80, {
		redirect to_www
		redirect http_to_https
	}
	ssl_domain "test1.com", address: "192.168.0.50", {
		certification_file certCrt.resource
		certification_key_file certKey.resource
		redirect to_www
	}
	domain "test2.com", address: "192.168.0.51", root: "test2", {
		redirect to_www
		redirect http_to_https
	}
	ssl_domain "test2.com", address: "192.168.0.51", use: "test2", {
		redirect to_www
		certification_file certCrt.resource
		certification_key_file certKey.resource
	}
}
