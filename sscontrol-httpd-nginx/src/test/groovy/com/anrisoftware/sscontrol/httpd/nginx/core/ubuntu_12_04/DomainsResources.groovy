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
package com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum DomainsResources {

    profile("UbuntuProfile.groovy", DomainsResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", DomainsResources.class.getResource("Httpd.groovy")),
    // files
    certCrt("cert.crt", DomainsResources.class.getResource("cert_crt.txt")),
    certKey("cert.key", DomainsResources.class.getResource("cert_key.txt")),
    // domains expected
    domainsTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", DomainsResources.class.getResource("domains_test1comconf_expected.txt")),
    domainsTest1comSslConfExpected("/etc/nginx/sites-available/100-robobee-test1.com-ssl.conf", DomainsResources.class.getResource("domains_test1comsslconf_expected.txt")),
    domainsRuncommandsLogExpected("/runcommands.log", DomainsResources.class.getResource("domains_runcommands_expected.txt")),
    domainsNginxConfExpected("/etc/nginx/nginx.conf", DomainsResources.class.getResource("domains_nginxconf_expected.txt")),
    domainsDefaultsConfExpected("/etc/nginx/conf.d/000-robobee_defaults.conf", DomainsResources.class.getResource("domains_defaultsconf_expected.txt")),
    domainsAptitudeOutExpected("/usr/bin/aptitude.out", DomainsResources.class.getResource("domains_aptitude_out_expected.txt")),
    domainsNginxOutExpected("/etc/init.d/nginx.out", DomainsResources.class.getResource("domains_nginx_out_expected.txt")),
    domainsLnOutExpected("/bin/ln.out", DomainsResources.class.getResource("domains_ln_out_expected.txt")),
    domainsUseraddOutExpected("/usr/sbin/useradd.out", DomainsResources.class.getResource("domains_useradd_out_expected.txt")),
    domainsGroupaddOutExpected("/usr/sbin/groupadd.out", DomainsResources.class.getResource("domains_groupadd_out_expected.txt")),
    domainsChownOutExpected("/bin/chown.out", DomainsResources.class.getResource("domains_chown_out_expected.txt")),
    domainsChmodOutExpected("/bin/chmod.out", DomainsResources.class.getResource("domains_chmod_out_expected.txt")),
    // used ports
    usedportsNetstatCommand("/bin/netstat", DomainsResources.class.getResource("usedports_netstat.txt")),
    usedportsRuncommandsLogExpected("/runcommands.log", DomainsResources.class.getResource("usedports_runcommands_expected.txt")),
    // used ports proxy
    usedportsproxyNetstatPortsCommand("/bin/netstat", DomainsResources.class.getResource("usedportsproxy_netstat.txt")),
    usedportsproxyRuncommandsLogExpected("/runcommands.log", DomainsResources.class.getResource("usedportsproxy_runcommands_expected.txt")),
    // users existing
    usersExistingRuncommandsLogExpected("/runcommands.log", DomainsResources.class.getResource("usersexist_runcommands_expected.txt")),
    usersExistingGroupsFile("/etc/group", DomainsResources.class.getResource("usersexist_group.txt")),
    usersExistingUsersFile("/etc/passwd", DomainsResources.class.getResource("usersexist_passwd.txt")),
    usersExistingGroupModOutExpected("/usr/sbin/groupmod.out", DomainsResources.class.getResource("usersexist_groupmod_out_expected.txt")),
    usersExistingUserModOutExpected("/usr/sbin/usermod.out", DomainsResources.class.getResource("usersexist_usermod_out_expected.txt")),
    usersExistingUseraddOutExpected("/usr/sbin/useradd.out", DomainsResources.class.getResource("usersexist_useradd_out_expected.txt")),
    usersExistingGroupaddOutExpected("/usr/sbin/groupadd.out", DomainsResources.class.getResource("usersexist_groupadd_out_expected.txt")),
    usersExistingChownOutExpected("/bin/chown.out", DomainsResources.class.getResource("usersexist_chown_out_expected.txt")),
    usersExistingChmodOutExpected("/bin/chmod.out", DomainsResources.class.getResource("usersexist_chmod_out_expected.txt")),
    usersExistingPsOutExpected("/bin/ps.out", DomainsResources.class.getResource("usersexist_ps_out_expected.txt")),
    usersExistingKillOutExpected("/usr/bin/kill.out", DomainsResources.class.getResource("usersexist_kill_out_expected.txt")),
    // thin user existing
    thinUserExistingRuncommandsLogExpected("/runcommands.log", DomainsResources.class.getResource("thinuserexist_runcommands_expected.txt")),
    thinUserExistingGroupsFile("/etc/group", DomainsResources.class.getResource("usersexist_group.txt")),
    thinUserExistingUsersFile("/etc/passwd", DomainsResources.class.getResource("usersexist_passwd.txt")),
    thinUserExistingPsCommand("/bin/ps", DomainsResources.class.getResource("thinuserexist_ps_command.txt")),
    thinUserExistingGroupModOutExpected("/usr/sbin/groupmod.out", DomainsResources.class.getResource("usersexist_groupmod_out_expected.txt")),
    thinUserExistingUserModOutExpected("/usr/sbin/usermod.out", DomainsResources.class.getResource("usersexist_usermod_out_expected.txt")),
    thinUserExistingUseraddOutExpected("/usr/sbin/useradd.out", DomainsResources.class.getResource("usersexist_useradd_out_expected.txt")),
    thinUserExistingGroupaddOutExpected("/usr/sbin/groupadd.out", DomainsResources.class.getResource("usersexist_groupadd_out_expected.txt")),
    thinUserExistingChownOutExpected("/bin/chown.out", DomainsResources.class.getResource("usersexist_chown_out_expected.txt")),
    thinUserExistingChmodOutExpected("/bin/chmod.out", DomainsResources.class.getResource("usersexist_chmod_out_expected.txt")),
    thinUserExistingPsOutExpected("/bin/ps.out", DomainsResources.class.getResource("usersexist_ps_out_expected.txt")),
    thinUserExistingKillOutExpected("/usr/bin/kill.out", DomainsResources.class.getResource("usersexist_kill_out_expected.txt")),
    thinUserExistingThinOutExpected("/etc/init.d/thin.out", DomainsResources.class.getResource("thinuserexist_thin_out_expected.txt")),
    // auth basic expected
    httpdScriptAuthBasic("Httpd.groovy", DomainsResources.class.getResource("HttpdAuthBasic.groovy")),
    authbasicprivateGroupFile("private.group", DomainsResources.class.getResource("privategroup.txt")),
    authbasicprivatePasswdFile("private.passwd", DomainsResources.class.getResource("privatepasswd.txt")),
    authbasicTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", DomainsResources.class.getResource("authbasic_test1comconf_expected.txt")),
    authbasicTest2comConfExpected("/etc/nginx/sites-available/100-robobee-test2.com.conf", DomainsResources.class.getResource("authbasic_test2comconf_expected.txt")),
    authbasicRuncommandsLogExpected("/runcommands.log", DomainsResources.class.getResource("authbasic_runcommands_expected.txt")),
    // auth basic proxy expected
    httpdScriptAuthBasicProxy("Httpd.groovy", DomainsResources.class.getResource("HttpdAuthBasicProxy.groovy")),
    authbasicproxyTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", DomainsResources.class.getResource("authbasicproxy_test1comconf_expected.txt")),
    authbasicproxyTest2comConfExpected("/etc/nginx/sites-available/100-robobee-test2.com.conf", DomainsResources.class.getResource("authbasicproxy_test2comconf_expected.txt")),
    // auth basic limit expected
    httpdScriptAuthBasicLimit("Httpd.groovy", DomainsResources.class.getResource("HttpdAuthBasicLimit.groovy")),
    authbasiclimitTest1comConfExpected("/etc/nginx/sites-available/100-robobee-test1.com.conf", DomainsResources.class.getResource("authbasiclimit_test1comconf_expected.txt")),
    authbasiclimitTest2comConfExpected("/etc/nginx/sites-available/100-robobee-test2.com.conf", DomainsResources.class.getResource("authbasiclimit_test2comconf_expected.txt")),

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
