/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.remote.resources.ResourcesUtils

/**
 * Remote Access/Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    profile("UbuntuProfile.groovy", UbuntuResources.class.getResource("Ubuntu_10_04_Profile.groovy")),
    remoteService("Remote.groovy", UbuntuResources.class.getResource("Remote.groovy")),
    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    restartCommand("/sbin/restart", UbuntuResources.class.getResource("echo_command.txt")),
    useraddCommand("/usr/sbin/useradd", UbuntuResources.class.getResource("echo_command.txt")),
    groupaddCommand("/usr/sbin/groupadd", UbuntuResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", UbuntuResources.class.getResource("echo_command.txt")),
    chmodCommand("/bin/chmod", UbuntuResources.class.getResource("echo_command.txt")),
    chpasswdCommand("/usr/sbin/chpasswd", UbuntuResources.class.getResource("echo_command.txt")),
    sshkeygenCommand("/usr/bin/ssh-keygen", UbuntuResources.class.getResource("echo_command.txt")),
    localBinDirectory("/usr/local/bin", null),
    groupsFile("/etc/group", UbuntuResources.class.getResource("group.txt")),
    passwdFile("/etc/passwd", UbuntuResources.class.getResource("passwd.txt")),
    sshdconfigFile("/etc/ssh/sshd_config", UbuntuResources.class.getResource("sshd_config.txt")),
    fooRemotePub("fooremote.pub", UbuntuResources.class.getResource("fooremote_pub.txt")),
    barRemotePub("barremote.pub", UbuntuResources.class.getResource("barremote_pub.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", UbuntuResources.class.getResource("aptitude_out_excepted.txt")),
    restartOutExpected("/sbin/restart.out", UbuntuResources.class.getResource("restart_out_excepted.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", UbuntuResources.class.getResource("useradd_out_excepted.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", UbuntuResources.class.getResource("groupadd_out_excepted.txt")),
    chownOutExpected("/bin/chown.out", UbuntuResources.class.getResource("chown_out_excepted.txt")),
    chmodOutExpected("/bin/chmod.out", UbuntuResources.class.getResource("chmod_out_excepted.txt")),
    chpasswdOutExpected("/usr/sbin/chpasswd.out", UbuntuResources.class.getResource("chpasswd_out_excepted.txt")),
    chpasswdInExpected("/usr/sbin/chpasswd.in", UbuntuResources.class.getResource("chpasswd_in_excepted.txt")),
    sshkeygenOutExpected("/usr/bin/ssh-keygen.out", UbuntuResources.class.getResource("sshkeygen_out_expected.txt")),
    sshdconfigExpected("/etc/ssh/sshd_config", UbuntuResources.class.getResource("sshd_config_expected.txt")),
    deventAuthorizedkeysExpected("/tmp/home/devent/.ssh/authorized_keys", UbuntuResources.class.getResource("devent_authorizedkeys_expected.txt")),
    fooAuthorizedkeysExpected("/home/foo/.ssh/authorized_keys", UbuntuResources.class.getResource("foo_authorizedkeys_expected.txt")),
    foobarAuthorizedkeysExpected("/home/foobar/.ssh/authorized_keys", UbuntuResources.class.getResource("foobar_authorizedkeys_expected.txt")),
    autoScreenExpected("/usr/local/bin/auto_screen.sh", UbuntuResources.class.getResource("auto_screen_expected.txt")),
    autoScreenSessionExpected("/home/foo/.bashrc", UbuntuResources.class.getResource("auto_screen_session_expected.txt")),
    screenrcExpected("/home/foo/.screenrc", UbuntuResources.class.getResource("screenrc_expected.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        useraddCommand.createCommand parent
        groupaddCommand.createCommand parent
        chownCommand.createCommand parent
        chmodCommand.createCommand parent
        chpasswdCommand.createCommand parent
        sshkeygenCommand.createCommand parent
        groupsFile.createFile parent
        passwdFile.createFile parent
        sshdconfigFile.createFile parent
    }

    ResourcesUtils resources

    UbuntuResources(String path, URL resource) {
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
