/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
    gititArchive("/tmp/gitit-0.10.4-archive.tar.gz", UbuntuResources.class.getResource("gitit-0.10.4.tar.gz")),
    gititServiceDir("/etc/init.d", null),
    gititServiceDefaultsDir("/etc/default", null),
    // test1.com
    test1comHsenvActivateCommand("/var/www/test1.com/gitit/.hsenv/bin/activate", UbuntuResources.class.getResource("echo_command.txt")),
    test1comHsenvCabalCommand("/var/www/test1.com/gitit/.hsenv/bin/cabal", UbuntuResources.class.getResource("echo_command.txt")),
    test1comHsenvGititCommand("/var/www/test1.com/gitit/.hsenv/cabal/bin/gitit", UbuntuResources.class.getResource("gitit_command.txt")),
    test1comHsenvDeactivateCommand("/var/www/test1.com/gitit/.hsenv/bin/deactivate_hsenv", UbuntuResources.class.getResource("echo_command.txt")),
    test1comGititDir("/var/www/test1.com/gitit", null),
    // test2.com
    test2comHsenvActivateCommand("/var/www/test2.com/gitit/.hsenv/bin/activate", UbuntuResources.class.getResource("echo_command.txt")),
    test2comHsenvCabalCommand("/var/www/test2.com/gitit/.hsenv/bin/cabal", UbuntuResources.class.getResource("echo_command.txt")),
    test2comHsenvGititCommand("/var/www/test2.com/gitit/.hsenv/cabal/bin/gitit", UbuntuResources.class.getResource("gitit_command.txt")),
    test2comHsenvDeactivateCommand("/var/www/test2.com/gitit/.hsenv/bin/deactivate_hsenv", UbuntuResources.class.getResource("echo_command.txt")),
    test2comGititDir("/var/www/test2.com/gitit", null),
    // expected
    test1comConfExpected("/etc/apache2/sites-available/100-robobee-test1.com.conf", GititResources.class.getResource("test1com_conf_expected.txt")),
    wwwtest1comConfExpected("/etc/apache2/sites-available/100-robobee-www.test1.com.conf", GititResources.class.getResource("wwwtest1com_conf_expected.txt")),
    test2comConfExpected("/etc/apache2/sites-available/100-robobee-test2.com.conf", GititResources.class.getResource("test2com_conf_expected.txt")),
    test1comGititConfExpected("/var/www/test1.com/gitit/gitit.conf", GititResources.class.getResource("test1com_gitit_conf_expected.txt")),
    test2comGititConfExpected("/var/www/test2.com/gitit/gitit.conf", GititResources.class.getResource("test2com_gitit_conf_expected.txt")),
    cabalOutExpected("/usr/bin/cabal.out", GititResources.class.getResource("cabal_out_expected.txt")),
    bashOutExpected("/bin/bash.out", GititResources.class.getResource("bash_out_expected.txt")),
    hsenvOutExpected("/root/.cabal/bin/hsenv.out", GititResources.class.getResource("hsenv_out_expected.txt")),
    tarOutExpected("/bin/tar.out", GititResources.class.getResource("tar_out_expected.txt")),
    test1comgititdServiceExpected("/etc/init.d/test1_com_gititd", GititResources.class.getResource("test1comgititd_service_expected.txt")),
    test1comgititdDefaultsExpected("/etc/default/test1_com_gititd", GititResources.class.getResource("test1comgititd_defaults_expected.txt")),
    test2comgititdServiceExpected("/etc/init.d/test2_com_gititd", GititResources.class.getResource("test2comgititd_service_expected.txt")),
    test2comgititdDefaultsExpected("/etc/default/test2_com_gititd", GititResources.class.getResource("test2comgititd_defaults_expected.txt")),
    chmodOutExpected("/bin/chmod.out", GititResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", GititResources.class.getResource("chown_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", GititResources.class.getResource("aptitude_out_expected.txt")),
    updateRcOutExpected("/usr/sbin/update-rc.d.out", GititResources.class.getResource("updaterc_out_expected.txt")),

    static copyGititFiles(File parent) {
        cabalCommand.createCommand parent
        hsenvCommand.createCommand parent
        gititArchive.createFile parent
        gititServiceDir.asFile parent mkdirs()
        gititServiceDefaultsDir.asFile parent mkdirs()
    }

    static copyTest1comFiles(File parent) {
        test1comGititDir.asFile(parent).mkdirs()
        test1comHsenvActivateCommand.createCommand parent
        test1comHsenvCabalCommand.createCommand parent
        test1comHsenvGititCommand.createCommand parent
        test1comHsenvDeactivateCommand.createCommand parent
    }

    static copyTest2comFiles(File parent) {
        test2comGititDir.asFile(parent).mkdirs()
        test2comHsenvActivateCommand.createCommand parent
        test2comHsenvCabalCommand.createCommand parent
        test2comHsenvGititCommand.createCommand parent
        test2comHsenvDeactivateCommand.createCommand parent
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
