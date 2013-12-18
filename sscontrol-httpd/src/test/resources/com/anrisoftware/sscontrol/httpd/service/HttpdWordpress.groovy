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

import com.anrisoftware.sscontrol.httpd.statements.wordpress.MultiSite

def certFile = ServicesResources.class.getResource "cert_crt.txt"
def certKeyFile = ServicesResources.class.getResource "cert_key.txt"

httpd {
    domain "test1.com", address: "192.168.0.50", { //.
        redirect to_www //.
    }
    ssl_domain "test1.com", address: "192.168.0.50", {
        certification_file certFile
        certification_key_file certKeyFile
    }
    domain "www.test1.com", address: "192.168.0.51", {
        user "www-data", group: "www-data"
        setup "wordpress", id: "wordpress3", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
        }
    }
    ssl_domain "www.test1.com", address: "192.168.0.51", {
        user "www-data", group: "www-data"
        certification_file certFile
        certification_key_file certKeyFile
        setup "wordpress", ref: "wordpress3"
    }
    domain "www.test2.com", address: "192.168.0.51", {
        setup "wordpress", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            multisite MultiSite.subdir
        }
    }
    domain "www.test3.com", address: "192.168.0.51", {
        setup "wordpress", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            multisite "subdomain"
        }
    }
    domain "www.testid.com", id: "testid", address: "192.168.0.51", {
        user "www-data", group: "www-data"
        setup "wordpress", id: "wordpress3", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
        }
    }
    domain "www.testref.com", address: "192.168.0.51", {
        user "www-data", group: "www-data"
        setup "wordpress", ref: "wordpress3", refdomain: "testid"
    }
}
