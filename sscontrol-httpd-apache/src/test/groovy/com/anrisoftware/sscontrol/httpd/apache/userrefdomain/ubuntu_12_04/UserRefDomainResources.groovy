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
package com.anrisoftware.sscontrol.httpd.apache.userrefdomain.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.resources.ResourcesUtils
import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UserRefDomainResources {

    profile("UbuntuProfile.groovy", UserRefDomainResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", UserRefDomainResources.class.getResource("HttpdUserRefDomain.groovy")),
    defaultConfExpected("/etc/apache2/sites-available/000-robobee-default.conf", UserRefDomainResources.class.getResource("default_conf_expected.txt")),
    domainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", UserRefDomainResources.class.getResource("domains_conf_expected.txt")),
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", UserRefDomainResources.class.getResource("test1com_conf_expected.txt")),
    test1comSslConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", UserRefDomainResources.class.getResource("test1com_ssl_conf_expected.txt")),
    test1comCrtExpected("/var/www/test1.com/ssl/cert_crt.txt", UbuntuResources.class.getResource("cert_crt.txt")),
    test1comKeyExpected("/var/www/test1.com/ssl/cert_key.txt", UbuntuResources.class.getResource("cert_key.txt")),
    test2comSslConfExpected("/etc/apache2/sites-available/100-robobee-test2.com-ssl.conf", UserRefDomainResources.class.getResource("test2com_ssl_conf_expected.txt")),
    test2comCrtExpected("/var/www/test1.com/ssl/cert_crt.txt", UbuntuResources.class.getResource("cert_crt.txt")),
    test2comKeyExpected("/var/www/test1.com/ssl/cert_key.txt", UbuntuResources.class.getResource("cert_key.txt")),
    wwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", UserRefDomainResources.class.getResource("wwwtest1com_conf_expected.txt")),
    test3comConfExpected("/etc/apache2/sites-available/100-robobee-test3.com.conf", UserRefDomainResources.class.getResource("test3com_conf_expected.txt")),
    wwwtest3comConfExpected("/etc/apache2/sites-available/100-robobee-www.test3.com.conf", UserRefDomainResources.class.getResource("wwwtest3com_conf_expected.txt")),
    test4comConfExpected("/etc/apache2/sites-available/100-robobee-test4.com.conf", UserRefDomainResources.class.getResource("test4com_conf_expected.txt")),
    wwwtest4comConfExpected("/etc/apache2/sites-available/100-robobee-www.test4.com.conf", UserRefDomainResources.class.getResource("wwwtest4com_conf_expected.txt")),
    ensiteOutExpected("/usr/sbin/a2ensite.out", UserRefDomainResources.class.getResource("ensite_out_expected.txt")),
    enmodOutExpected("/usr/sbin/a2enmod.out", UserRefDomainResources.class.getResource("enmod_out_expected.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", UserRefDomainResources.class.getResource("useradd_out_expected.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", UserRefDomainResources.class.getResource("groupadd_out_expected.txt")),
    chownOutExpected("/bin/chown.out", UserRefDomainResources.class.getResource("chown_out_expected.txt")),

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
