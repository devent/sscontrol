/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "piwik", id: "piwikid", {
            debug level: 4
            override mode: update
        }
    }
    ssl_domain "test1.com", address: "192.168.0.51", {
        certificate file: UbuntuResources.certCrt.resource, key: UbuntuResources.certKey.resource
        setup "piwik", ref: "piwikid"
    }
    domain "test2.com", address: "192.168.0.52", {
        setup "piwik", id: "piwikid", alias: "/piwik", prefix: "test2piwik", {
            //.
        }
    }
}
