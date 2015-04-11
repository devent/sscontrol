/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

httpd {
    domain "test1.com", address: "192.168.0.50", {
        setup "static", id: "static-test1.com", location: "/static", {

            // set index
            index files: "index.\$geo.html, index.htm, index.html"
            index mode: IndexMode.auto

            // set expires duration
            expires "P1D"

            // disable access log
            access log: no

            // adds the headers
            headers "Cache-Control: public"

            // add jpg|png|gif|jpeg|...| cache
            cache staticFiles: true
        }
    }
}
