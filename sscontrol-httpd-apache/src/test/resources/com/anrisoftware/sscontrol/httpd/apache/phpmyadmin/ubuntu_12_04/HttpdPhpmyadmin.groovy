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
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources

httpd {
    ssl_domain "ubuntutest.com", address: "192.168.0.100", {
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
        memory limit: "24 MB", upload: "24 MB", post: "24 MB"
        setup "phpmyadmin", alias: "phpmyadmin", {
            admin "root", password: "mysqladminpassword"
            control "phpmyadmin", password: "phpmyadminpassword", database: "phpmyadmin"
            server "127.0.0.1", port: 3306
        }
    }
}
