/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.userrefdomain.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils;

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UserRefDomainResources {

    profile("UbuntuProfile.groovy", UserRefDomainResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", UserRefDomainResources.class.getResource("HttpdUserRefDomain.groovy")),
    certCrt("cert.crt", UserRefDomainResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", UserRefDomainResources.class.getResource("cert_key.txt")),
    // expected
    test1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", UserRefDomainResources.class.getResource("test1com_conf.txt")),
    test1comSslConf("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", UserRefDomainResources.class.getResource("test1com_ssl_conf.txt")),
    test2comSslConf("/etc/nginx/sites-available/100-robobee-test2.com-ssl.conf", UserRefDomainResources.class.getResource("test2com_ssl_conf.txt")),
    test3comConf("/etc/nginx/sites-available/100-robobee-test3.com.conf", UserRefDomainResources.class.getResource("test3com_conf.txt")),
    wwwtest3comConf("/etc/nginx/sites-available/100-robobee-www.test3.com.conf", UserRefDomainResources.class.getResource("wwwtest3com_conf.txt")),
    test4comConf("/etc/nginx/sites-available/100-robobee-test4.com.conf", UserRefDomainResources.class.getResource("test4com_conf.txt")),
    wwwtest4comConf("/etc/nginx/sites-available/100-robobee-www.test4.com.conf", UserRefDomainResources.class.getResource("wwwtest4com_conf.txt")),
    runcommandsLogExpected("/runcommands.log", UserRefDomainResources.class.getResource("runcommands_expected.txt")),
    nginxConfExpected("/etc/nginx/nginx.conf", UserRefDomainResources.class.getResource("nginx_conf_expected.txt")),
    robobeeConfExpected("/etc/nginx/conf.d/000-robobee_defaults.conf", UserRefDomainResources.class.getResource("robobee_conf_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", UserRefDomainResources.class.getResource("aptitude_out_expected.txt")),
    nginxOutExpected("/etc/init.d/nginx.out", UserRefDomainResources.class.getResource("nginx_out_expected.txt")),
    lnOutExpected("/bin/ln.out", UserRefDomainResources.class.getResource("ln_out_expected.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", UserRefDomainResources.class.getResource("useradd_out_expected.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", UserRefDomainResources.class.getResource("groupadd_out_expected.txt")),
    chownOutExpected("/bin/chown.out", UserRefDomainResources.class.getResource("chown_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", UserRefDomainResources.class.getResource("chmod_out_expected.txt")),
    // used ports
    netstatPortsCommand("/bin/netstat", UserRefDomainResources.class.getResource("netstat_used_ports.txt")),

    ResourcesUtils resources

    UserRefDomainResources(String path, URL resource) {
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
