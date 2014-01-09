/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.remote.resources.ResourcesUtils

/**
 * Remote Access/Ubuntu 12.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_12_04_Resources {

    profile("UbuntuProfile.groovy", Ubuntu_12_04_Resources.class.getResource("Ubuntu_10_04_Profile.groovy")),
    remoteService("Remote.groovy", Ubuntu_12_04_Resources.class.getResource("Remote.groovy")),
    sshkeygenCommand("/usr/bin/ssh-keygen", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    groupsFile("/etc/group", Ubuntu_12_04_Resources.class.getResource("group.txt")),
    passwdFile("/etc/passwd", Ubuntu_12_04_Resources.class.getResource("passwd.txt")),
    sshdconfigFile("/etc/ssh/sshd_config", Ubuntu_12_04_Resources.class.getResource("sshd_config.txt")),
    fooRemotePub("fooremote.pub", Ubuntu_12_04_Resources.class.getResource("fooremote_pub.txt")),
    barRemotePub("barremote.pub", Ubuntu_12_04_Resources.class.getResource("barremote_pub.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_12_04_Resources.class.getResource("aptitude_out_excepted.txt")),
    restartOutExpected("/sbin/restart.out", Ubuntu_12_04_Resources.class.getResource("restart_out_excepted.txt")),
    useraddOutExpected("/usr/sbin/useradd.out", Ubuntu_12_04_Resources.class.getResource("useradd_out_excepted.txt")),
    groupaddOutExpected("/usr/sbin/groupadd.out", Ubuntu_12_04_Resources.class.getResource("groupadd_out_excepted.txt")),
    chownOutExpected("/bin/chown.out", Ubuntu_12_04_Resources.class.getResource("chown_out_excepted.txt")),
    chmodOutExpected("/bin/chmod.out", Ubuntu_12_04_Resources.class.getResource("chmod_out_excepted.txt")),
    chpasswdOutExpected("/usr/sbin/chpasswd.out", Ubuntu_12_04_Resources.class.getResource("chpasswd_out_excepted.txt")),
    chpasswdInExpected("/usr/sbin/chpasswd.in", Ubuntu_12_04_Resources.class.getResource("chpasswd_in_excepted.txt")),
    sshkeygenOutExpected("/usr/bin/ssh-keygen.out", Ubuntu_12_04_Resources.class.getResource("sshkeygen_out_expected.txt")),
    sshdconfigExpected("/etc/ssh/sshd_config", Ubuntu_12_04_Resources.class.getResource("sshd_config_expected.txt")),
    deventAuthorizedkeysExpected("/tmp/home/devent/.ssh/authorized_keys", Ubuntu_12_04_Resources.class.getResource("devent_authorizedkeys_expected.txt")),
    fooAuthorizedkeysExpected("/home/foo/.ssh/authorized_keys", Ubuntu_12_04_Resources.class.getResource("foo_authorizedkeys_expected.txt")),
    foobarAuthorizedkeysExpected("/home/foobar/.ssh/authorized_keys", Ubuntu_12_04_Resources.class.getResource("foobar_authorizedkeys_expected.txt")),
    autoScreenExpected("/usr/local/bin/auto_screen.sh", Ubuntu_12_04_Resources.class.getResource("auto_screen_expected.txt")),
    autoScreenSessionExpected("/home/foo/.bashrc", Ubuntu_12_04_Resources.class.getResource("auto_screen_session_expected.txt")),
    screenrcExpected("/home/foo/.screenrc", Ubuntu_12_04_Resources.class.getResource("screenrc_expected.txt")),

    static void copyUbuntu_12_04_Files(File parent) {
        sshkeygenCommand.createCommand parent
        groupsFile.createFile parent
        passwdFile.createFile parent
        sshdconfigFile.createFile parent
    }

    ResourcesUtils resources

    Ubuntu_12_04_Resources(String path, URL resource) {
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
