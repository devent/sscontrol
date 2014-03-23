/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.httpd.resources.ResourcesUtils

/**
 * <i>Ubuntu</i> 12.04 <i>gitit</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum GititResources {

    profile("UbuntuProfile.groovy", GititResources.class.getResource("UbuntuProfile.groovy")),
    httpdScript("Httpd.groovy", GititResources.class.getResource("HttpdGitit.groovy")),
    // gitit
    cabalCommand("/usr/bin/cabal", UbuntuResources.class.getResource("echo_command.txt")),
    hsenvCommand("/root/.cabal/bin/hsenv", UbuntuResources.class.getResource("echo_command.txt")),
    hsenvActivateCommand("/var/www/test1.com/gitit/.hsenv/bin/activate", UbuntuResources.class.getResource("echo_command.txt")),
    hsenvCabalCommand("/var/www/test1.com/gitit/.hsenv/bin/cabal", UbuntuResources.class.getResource("echo_command.txt")),
    hsenvGititCommand("/var/www/test1.com/gitit/.hsenv/cabal/bin/gitit", UbuntuResources.class.getResource("gitit_command.txt")),
    hsenvDeactivateCommand("/var/www/test1.com/gitit/.hsenv/bin/deactivate_hsenv", UbuntuResources.class.getResource("echo_command.txt")),
    gititDir("/var/www/test1.com/gitit", null),
    gititArchive("/tmp/gitit-0.10.3.1-archive.tar.gz", UbuntuResources.class.getResource("gitit-0.10.3.1.tar.gz")),
    gititServiceDir("/etc/init.d", null),
    gititServiceDefaultsDir("/etc/default", null),
    // expected
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", GititResources.class.getResource("test1com_conf_expected.txt")),
    wwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", GititResources.class.getResource("wwwtest1com_conf_expected.txt")),
    test2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", GititResources.class.getResource("test2com_conf_expected.txt")),
    test1comGititConfExpected("/var/www/test1.com/gitit/gitit.conf", GititResources.class.getResource("test1com_gitit_conf_expected.txt")),
    cabalOutExpected("/usr/bin/cabal.out", GititResources.class.getResource("cabal_out_expected.txt")),
    hsenvCabalOutExpected("/var/www/test1.com/gitit/.hsenv/bin/cabal.out", GititResources.class.getResource("hsenvcabal_out_expected.txt")),
    hsenvOutExpected("/root/.cabal/bin/hsenv.out", GititResources.class.getResource("hsenv_out_expected.txt")),
    tarOutExpected("/bin/tar.out", GititResources.class.getResource("tar_out_expected.txt")),
    test1comgititdServiceExpected("/etc/init.d/test1_com_gititd", GititResources.class.getResource("test1comgititd_service_expected.txt")),
    test1comgititdDefaultsExpected("/etc/default/test1_com_gititd", GititResources.class.getResource("test1comgititd_defaults_expected.txt")),
    test2comgititdServiceExpected("/etc/init.d/test2_com_gititd", GititResources.class.getResource("test2comgititd_service_expected.txt")),
    test2comgititdDefaultsExpected("/etc/default/test2_com_gititd", GititResources.class.getResource("test2comgititd_defaults_expected.txt")),
    chmodOutExpected("/bin/chmod.out", GititResources.class.getResource("chmod_out_expected.txt")),

    static copyGititFiles(File parent) {
        gititDir.asFile(parent).mkdirs()
        cabalCommand.createCommand parent
        hsenvCommand.createCommand parent
        hsenvActivateCommand.createCommand parent
        hsenvCabalCommand.createCommand parent
        hsenvGititCommand.createCommand parent
        hsenvDeactivateCommand.createCommand parent
        gititArchive.createFile parent
        gititServiceDir.asFile parent mkdirs()
        gititServiceDefaultsDir.asFile parent mkdirs()
    }

    ResourcesUtils resources

    GititResources(String path, URL resource) {
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
