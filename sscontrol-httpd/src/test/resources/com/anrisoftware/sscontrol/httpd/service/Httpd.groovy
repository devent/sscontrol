package com.anrisoftware.sscontrol.httpd.service

def certFile = HttpdResources.class.getResource "cert_crt.txt"
def certKeyFile = HttpdResources.class.getResource "cert_key.txt"

httpd {
	domain "test1.com", address: "192.168.0.50", port: 80, {
		redirect to_www
		redirect http_to_https
	}
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
