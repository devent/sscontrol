/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.proxy.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.nginx.resources.ResourcesUtils

/**
 * Proxy Nginx resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum ProxyResources {

    profile("UbuntuProfile.groovy", ProxyResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", ProxyResources.class.getResource("HttpdProxy.groovy")),
    certCrt("cert.crt", ProxyResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", ProxyResources.class.getResource("cert_key.txt")),
    proxyConf("/etc/nginx/conf.d/010-robobee_proxy.conf", ProxyResources.class.getResource("proxy_conf_expected.txt")),
    test1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", ProxyResources.class.getResource("test1_com_conf.txt")),
    test1comSslConf("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", ProxyResources.class.getResource("test1_com_ssl_conf.txt")),
    wwwtest1comConf("/etc/nginx/sites-available/100-robobee-www.test1.com.conf", ProxyResources.class.getResource("www_test1_com_conf.txt")),
    wwwtest1comSslConf("/etc/nginx/sites-available/100-robobee-www.test1.com-ssl.conf", ProxyResources.class.getResource("www_test1_com_ssl_conf.txt")),
    sitefooProxyConf("/etc/nginx/conf.d/020-robobee-sitefoo-proxy.conf", ProxyResources.class.getResource("sitefoo_proxy_conf.txt")),

    ResourcesUtils resources

    ProxyResources(String path, URL resource) {
        this.resources = new ResourcesUtils(path: path, resource: resource)
    }

    String getPath() {
        resources.path
    }

    URL getResource() {
        resources.resource
    }

    File asFile(File parent) {
        resources.asFile parent
    }

    void createFile(File parent) {
        resources.createFile parent
    }

    void createCommand(File parent) {
        resources.createCommand parent
    }

    String replaced(File parent, def search, def replace) {
        resources.replaced parent, search, replace
    }

    String toString() {
        resources.toString()
    }
}
