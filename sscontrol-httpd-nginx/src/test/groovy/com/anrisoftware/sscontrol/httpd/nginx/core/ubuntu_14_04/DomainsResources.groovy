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
package com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.nginx.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum DomainsResources {

    profile("UbuntuProfile.groovy", DomainsResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", DomainsResources.class.getResource("Httpd.groovy")),
    certCrt("cert.crt", DomainsResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", DomainsResources.class.getResource("cert_key.txt")),
    // core expected
    test1comConf("/etc/nginx/sites-available/100-robobee-test1.com.conf", DomainsResources.class.getResource("test1_com_conf.txt")),
    test1comSslConf("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", DomainsResources.class.getResource("test1_com_ssl_conf.txt")),
    nginxConfExpected("/etc/nginx/nginx.conf", DomainsResources.class.getResource("nginx_conf_expected.txt")),
    robobeeConfExpected("/etc/nginx/conf.d/000-robobee_defaults.conf", DomainsResources.class.getResource("robobee_conf_expected.txt")),
    robobeeSitesConfExpected("/etc/nginx/conf.d/999-robobee_sites.conf", DomainsResources.class.getResource("robobee_sites_conf_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", DomainsResources.class.getResource("aptitude_out_expected.txt")),
    restartOutExpected("/etc/init.d/nginx.out", DomainsResources.class.getResource("restart_out_expected.txt")),
    lnOutExpected("/bin/ln.out", DomainsResources.class.getResource("ln_out_expected.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", DomainsResources.class.getResource("useradd_out_expected.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", DomainsResources.class.getResource("groupadd_out_expected.txt")),
    chownOutExpected("/bin/chown.out", DomainsResources.class.getResource("chown_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", DomainsResources.class.getResource("chmod_out_expected.txt")),
    // used ports
    netstatPortsCommand("/bin/netstat", DomainsResources.class.getResource("netstat_used_ports.txt")),
    // used ports proxy
    netstatPortsProxyCommand("/bin/netstat", DomainsResources.class.getResource("netstat_used_ports_proxy.txt")),
    // users existing
    usersExistingGroupsFile("/etc/group", DomainsResources.class.getResource("usersexist_group.txt")),
    usersExistingUsersFile("/etc/passwd", DomainsResources.class.getResource("usersexist_passwd.txt")),
    usersExistingGroupModOutExpected("/usr/sbin/groupmod.out", DomainsResources.class.getResource("usersexist_groupmod_out_expected.txt")),
    usersExistingUserModOutExpected("/usr/sbin/usermod.out", DomainsResources.class.getResource("usersexist_usermod_out_expected.txt")),
    usersExistingUseraddOutExpected("/usr/sbin/useradd.out", DomainsResources.class.getResource("usersexist_useradd_out_expected.txt")),
    usersExistingGroupaddOutExpected("/usr/sbin/groupadd.out", DomainsResources.class.getResource("usersexist_groupadd_out_expected.txt")),
    usersExistingPsOutExpected("/bin/ps.out", DomainsResources.class.getResource("usersexist_ps_out_expected.txt")),
    usersExistingKillOutExpected("/usr/bin/kill.out", DomainsResources.class.getResource("usersexist_kill_out_expected.txt")),

    ResourcesUtils resources

    DomainsResources(String path, URL resource) {
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
