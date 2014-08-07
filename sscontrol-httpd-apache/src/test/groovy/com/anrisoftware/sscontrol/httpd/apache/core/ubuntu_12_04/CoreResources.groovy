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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.apache.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum CoreResources {

    profile("UbuntuProfile.groovy", CoreResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", CoreResources.class.getResource("Httpd.groovy")),
    httpdDomainsScript("Httpd.groovy", CoreResources.class.getResource("HttpdDomains.groovy")),
    apacheOutExpected("/etc/init.d/apache2.out", CoreResources.class.getResource("apache_out_expected.txt")),
    robobeeDefaultConfExpected("/etc/apache2/sites-available/000-robobee-default.conf", CoreResources.class.getResource("default_conf_expected.txt")),
    domainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("domains_conf_expected.txt")),
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", CoreResources.class.getResource("test1_com_conf_expected.txt")),
    test1comSslConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", CoreResources.class.getResource("test1_com_ssl_conf_expected.txt")),
    test1comWeb("/var/www/test1.com/web", null),
    test1comCrtExpected("/var/www/test1.com/ssl/cert_crt.txt", CoreResources.class.getResource("cert_crt_expected.txt")),
    test1comKeyExpected("/var/www/test1.com/ssl/cert_key.txt", CoreResources.class.getResource("cert_key_expected.txt")),
    test2comCrtExpected("/var/www/test2.com/ssl/cert_crt.txt", CoreResources.class.getResource("cert_crt_expected.txt")),
    test2comKeyExpected("/var/www/test2.com/ssl/cert_key.txt", CoreResources.class.getResource("cert_key_expected.txt")),
    test2comCaExpected("/var/www/test2.com/ssl/cert_ca.txt", CoreResources.class.getResource("cert_ca_expected.txt")),
    ensiteOutExpected("/usr/sbin/a2ensite.out", CoreResources.class.getResource("ensite_out_expected.txt")),
    enmodOutExpected("/usr/sbin/a2enmod.out", CoreResources.class.getResource("enmod_out_expected.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", CoreResources.class.getResource("useradd_out_expected.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", CoreResources.class.getResource("groupadd_out_expected.txt")),
    chownOutExpected("/bin/chown.out", CoreResources.class.getResource("chown_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", CoreResources.class.getResource("chmod_out_expected.txt")),
    // domains users
    usersDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("users_conf_expected.txt")),
    usersDomainsConfExpected2("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("users_conf_expected2.txt")),
    usersEnsiteOutExpected("/usr/sbin/a2ensite.out", CoreResources.class.getResource("users_ensite_out_expected.txt")),
    usersEnmodOutExpected("/usr/sbin/a2enmod.out", CoreResources.class.getResource("users_enmod_out_expected.txt")),
    usersUseraddOutExpected("/usr/sbin/useradd.out", CoreResources.class.getResource("users_useradd_out_expected.txt")),
    usersGroupaddOutExpected("/usr/sbin/groupadd.out", CoreResources.class.getResource("users_groupadd_out_expected.txt")),
    usersChownOutExpected("/bin/chown.out", CoreResources.class.getResource("users_chown_out_expected.txt")),

    ResourcesUtils resources

    CoreResources(String path, URL resource) {
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
