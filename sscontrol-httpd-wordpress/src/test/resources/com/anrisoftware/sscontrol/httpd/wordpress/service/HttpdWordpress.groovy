/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.service

import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.wordpress.MultiSite;

def certFile = ServicesResources.class.getResource "cert_crt.txt"
def certKeyFile = ServicesResources.class.getResource "cert_key.txt"

httpd {
    domain "www.test1.com", address: "192.168.0.51", {
        user "www-data", group: "www-data"
        setup "wordpress", id: "wordpress3", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            backup target: "/var/backups"
            force login: true, admin: true
            plugins "wp-typography, link-indication, broken-link-checker"
            themes "picochic, tagebuch"
        }
    }
    ssl_domain "www.test1.com", address: "192.168.0.51", {
        user "www-data", group: "www-data"
        certificate file: certFile, key: certKeyFile
        setup "wordpress", ref: "wordpress3"
    }
    domain "www.test2.com", address: "192.168.0.51", {
        setup "wordpress", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            multisite setup: MultiSite.subdir
        }
    }
    domain "www.test3.com", address: "192.168.0.51", {
        setup "wordpress", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            multisite setup: "subdomain"
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
    domain "www.testold.com", address: "192.168.0.51", {
        setup "wordpress", alias: "wordpress3", prefix: "wordpressold", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            override mode: YesNoFlag.no
        }
    }
    domain "www.testupdate.com", address: "192.168.0.51", {
        setup "wordpress", alias: "wordpress3", prefix: "wordpressold", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            override mode: OverrideMode.update
        }
    }
}
