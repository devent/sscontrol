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

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum CoreResources {

    profile("UbuntuProfile.groovy", CoreResources.class.getResource("UbuntuProfile.groovy")),
    httpdSimpleScript("Httpd.groovy", CoreResources.class.getResource("HttpdSimple.groovy")),
    httpdUsersScript("Httpd.groovy", CoreResources.class.getResource("HttpdUsers.groovy")),
    httpdPortsScript("Httpd.groovy", CoreResources.class.getResource("HttpdPorts.groovy")),
    httpdInvalidPortsScript("Httpd.groovy", CoreResources.class.getResource("HttpdInvalidPorts.groovy")),
    // simple expected
    simpleRuncommandsLogExpected("/runcommands.log", CoreResources.class.getResource("simple_runcommands_expected.txt")),
    simpleApacheRestartOutExpected("/etc/init.d/apache2Restart.out", CoreResources.class.getResource("simple_apacherestart_out_expected.txt")),
    simpleApacheStopOutExpected("/etc/init.d/apache2Stop.out", CoreResources.class.getResource("simple_apachestop_out_expected.txt")),
    simpleDefaultConfExpected("/etc/apache2/sites-available/000-robobee-default.conf", CoreResources.class.getResource("simple_defaultconf_expected.txt")),
    simpleDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("simple_domainsconf_expected.txt")),
    simplePortsConfExpected("/etc/apache2/ports.conf", CoreResources.class.getResource("simple_portsconf_expected.txt")),
    simpleTest1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", CoreResources.class.getResource("simple_test1comconf_expected.txt")),
    simpleTest1comSslConfExpected("/etc/apache2/sites-available/100-robobee-test1.com-ssl.conf", CoreResources.class.getResource("simple_test1comsslconf_expected.txt")),
    simpleTest1comWeb("/var/www/test1.com/web", null),
    simpleTest1comCrtExpected("/var/www/test1.com/ssl/cert_crt.txt", CoreResources.class.getResource("simple_certcrt_expected.txt")),
    simpleTest1comKeyExpected("/var/www/test1.com/ssl/cert_key.txt", CoreResources.class.getResource("simple_certkey_expected.txt")),
    simpleTest2comCrtExpected("/var/www/test2.com/ssl/cert_crt.txt", CoreResources.class.getResource("simple_certcrt_expected.txt")),
    simpleTest2comKeyExpected("/var/www/test2.com/ssl/cert_key.txt", CoreResources.class.getResource("simple_certkey_expected.txt")),
    simpleTest2comCaExpected("/var/www/test2.com/ssl/cert_ca.txt", CoreResources.class.getResource("simple_certca_expected.txt")),
    simpleEnsiteOutExpected("/usr/sbin/a2ensite.out", CoreResources.class.getResource("simple_ensite_out_expected.txt")),
    simpleEnmodOutExpected("/usr/sbin/a2enmod.out", CoreResources.class.getResource("simple_enmod_out_expected.txt")),
    simpleUseraddOutExpected("/usr/sbin/useradd.out", CoreResources.class.getResource("simple_useradd_out_expected.txt")),
    simpleGroupaddOutExpected("/usr/sbin/groupadd.out", CoreResources.class.getResource("simple_groupadd_out_expected.txt")),
    simpleChownOutExpected("/bin/chown.out", CoreResources.class.getResource("simple_chown_out_expected.txt")),
    simpleChmodOutExpected("/bin/chmod.out", CoreResources.class.getResource("simple_chmod_out_expected.txt")),
    simplePsOutExpected("/bin/ps.out", CoreResources.class.getResource("simple_ps_out_expected.txt")),
    simpleKillOutExpected("/usr/bin/kill.out", CoreResources.class.getResource("simple_kill_out_expected.txt")),
    // ports expected
    portsApacheRestartOutExpected("/etc/init.d/apache2Restart.out", CoreResources.class.getResource("simple_apacherestart_out_expected.txt")),
    portsDefaultConfExpected("/etc/apache2/sites-available/000-robobee-default.conf", CoreResources.class.getResource("simple_defaultconf_expected.txt")),
    portsDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("ports_domainsconf_expected.txt")),
    portsPortsConfExpected("/etc/apache2/ports.conf", CoreResources.class.getResource("ports_portsconf_expected.txt")),
    // users expected
    usersApacheRestartOutExpected("/etc/init.d/apache2Restart.out", CoreResources.class.getResource("simple_apacherestart_out_expected.txt")),
    usersDefaultConfExpected("/etc/apache2/sites-available/000-robobee-default.conf", CoreResources.class.getResource("simple_defaultconf_expected.txt")),
    usersDomainsConfExpected("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("users_conf_expected.txt")),
    usersDomainsConfExpected2("/etc/apache2/conf.d/000-robobee-domains.conf", CoreResources.class.getResource("users_conf_expected2.txt")),
    usersEnsiteOutExpected("/usr/sbin/a2ensite.out", CoreResources.class.getResource("users_ensite_out_expected.txt")),
    usersEnmodOutExpected("/usr/sbin/a2enmod.out", CoreResources.class.getResource("users_enmod_out_expected.txt")),
    usersUseraddOutExpected("/usr/sbin/useradd.out", CoreResources.class.getResource("users_useradd_out_expected.txt")),
    usersGroupaddOutExpected("/usr/sbin/groupadd.out", CoreResources.class.getResource("users_groupadd_out_expected.txt")),
    usersChownOutExpected("/bin/chown.out", CoreResources.class.getResource("users_chown_out_expected.txt")),
    usersPsOutExpected("/bin/ps.out", CoreResources.class.getResource("simple_ps_out_expected.txt")),
    usersKillOutExpected("/usr/bin/kill.out", CoreResources.class.getResource("simple_kill_out_expected.txt")),
    // existing users expected
    usersExistingPsOutExpected("/bin/ps.out", CoreResources.class.getResource("usersexist_ps_out_expected.txt")),
    usersExistingKillOutExpected("/usr/bin/kill.out", CoreResources.class.getResource("usersexist_kill_out_expected.txt")),

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
