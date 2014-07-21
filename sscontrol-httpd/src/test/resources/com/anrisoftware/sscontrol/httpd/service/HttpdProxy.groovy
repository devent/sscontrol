/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
    // reference service with id "idproxy"
    refservice "idproxy"
    // domain "test1.com"
    domain "test1.com", address: "192.168.0.50", {
        setup "proxy", service: "servicefoo", alias: "fooalias", address: "http://127.0.0.1:8080"
    }
    // domain "test1.com"
    ssl_domain "test1.com", address: "192.168.0.50", {
        setup "proxy", service: "servicebar", proxyname: "bar", address: "http://127.0.0.1:8080", {
            cache staticFiles: true, feeds: true
        }
        certificate file: certFile, key: certKeyFile
    }
}
