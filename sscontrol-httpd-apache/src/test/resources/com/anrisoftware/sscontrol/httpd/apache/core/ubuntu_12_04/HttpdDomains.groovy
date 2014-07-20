/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources

def ip1 = "127.0.0.1"
def http = 8080
def https1 = 8090
def https2 = 8092
def anrinstituteId = "anrinstitute_domain"
def muellerpublicId = "muellerpublic_domain"
def anrinstituteWordpress = "anrinstitute_wordpress";
def muellerpublicWordpress = "muellerpublic_wordpress";

httpd {
    domain "anr-institute.com", address: ip1, port: http, {
    }
    ssl_domain "anr-institute.com", id: anrinstituteId, address: ip1, port: https1, {
        certificate file: UbuntuResources.certCrt.resource, key: UbuntuResources.certKey.resource
    }
    domain "www.anr-institute.com", address: ip1, port: http, {
    }
    ssl_domain "anrisoftware.com", address: ip1, port: https2, {
        certificate file: UbuntuResources.certCrt.resource, key: UbuntuResources.certKey.resource, ca: UbuntuResources.certCa.resource
    }
    domain "www.mueller-public.de", address: ip1, port: http, {
    }
}
