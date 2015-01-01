/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.remote.resources.ResourcesUtils

/**
 * <i>Remote Access Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_14_04_Resources {

    profile("UbuntuProfile.groovy", Ubuntu_14_04_Resources.class.getResource("UbuntuProfile.groovy")),
    remoteService("Remote.groovy", Ubuntu_14_04_Resources.class.getResource("Remote.groovy")),
    remoteRequireService("Remote.groovy", Ubuntu_14_04_Resources.class.getResource("RemoteRequire.groovy")),
    // commands
    sshkeygenCommand("/usr/bin/ssh-keygen", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    // files
    groupsFile("/etc/group", Ubuntu_14_04_Resources.class.getResource("group.txt")),
    passwdFile("/etc/passwd", Ubuntu_14_04_Resources.class.getResource("passwd.txt")),
    sshdconfigFile("/etc/ssh/sshd_config", Ubuntu_14_04_Resources.class.getResource("sshd_config.txt")),
    fooPub("foo.pub", Ubuntu_14_04_Resources.class.getResource("foo_pub.txt")),
    barPub("bar.pub", Ubuntu_14_04_Resources.class.getResource("bar_pub.txt")),
    bazPub("baz.pub", Ubuntu_14_04_Resources.class.getResource("baz_pub.txt")),
    // remote expected
    remoteAptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_14_04_Resources.class.getResource("remote_aptitude_out_excepted.txt")),
    remoteRestartOutExpected("/sbin/restart.out", Ubuntu_14_04_Resources.class.getResource("remote_restart_out_excepted.txt")),
    remoteUseraddOutExpected("/usr/sbin/useradd.out", Ubuntu_14_04_Resources.class.getResource("remote_useradd_out_excepted.txt")),
    remoteGroupaddOutExpected("/usr/sbin/groupadd.out", Ubuntu_14_04_Resources.class.getResource("remote_groupadd_out_excepted.txt")),
    remoteChownOutExpected("/bin/chown.out", Ubuntu_14_04_Resources.class.getResource("remote_chown_out_excepted.txt")),
    remoteChmodOutExpected("/bin/chmod.out", Ubuntu_14_04_Resources.class.getResource("remote_chmod_out_excepted.txt")),
    remoteSshkeygenOutExpected("/usr/bin/ssh-keygen.out", Ubuntu_14_04_Resources.class.getResource("remote_sshkeygen_out_expected.txt")),
    remoteSshdconfigExpected("/etc/ssh/sshd_config", Ubuntu_14_04_Resources.class.getResource("remote_sshd_config_expected.txt")),
    remoteFooAuthorizedkeysExpected("/home/foo/.ssh/authorized_keys", Ubuntu_14_04_Resources.class.getResource("remote_foo_authorizedkeys_expected.txt")),
    remoteAutoScreenExpected("/usr/local/bin/auto_screen.sh", Ubuntu_14_04_Resources.class.getResource("remote_auto_screen_expected.txt")),
    remoteAutoScreenSessionExpected("/home/foo/.bashrc", Ubuntu_14_04_Resources.class.getResource("remote_auto_screen_session_expected.txt")),
    remoteScreenrcExpected("/home/foo/.screenrc", Ubuntu_14_04_Resources.class.getResource("remote_screenrc_expected.txt")),
    // remote required expected
    remoteRequireGroupsFile("/etc/group", Ubuntu_14_04_Resources.class.getResource("remoterequire_group.txt")),
    remoteRequirePasswdFile("/etc/passwd", Ubuntu_14_04_Resources.class.getResource("remoterequire_passwd.txt")),
    remoteRequireGroupaddOutExpected("/usr/sbin/groupadd.out", Ubuntu_14_04_Resources.class.getResource("remoterequire_groupadd_out_excepted.txt")),
    remoteRequireUseraddOutExpected("/usr/sbin/useradd.out", Ubuntu_14_04_Resources.class.getResource("remoterequire_useradd_out_excepted.txt")),
    remoteRequireChownOutExpected("/bin/chown.out", Ubuntu_14_04_Resources.class.getResource("remoterequire_chown_out_excepted.txt")),
    remoteRequireUsermodOutExpected("/usr/sbin/usermod.out", Ubuntu_14_04_Resources.class.getResource("remoterequire_usermod_out_excepted.txt")),
    remoteRequireChpasswdOutExpected("/usr/sbin/chpasswd.out", Ubuntu_14_04_Resources.class.getResource("remoterequire_chpasswd_out_excepted.txt")),
    remoteRequireSshkeygenOutExpected("/usr/bin/ssh-keygen.out", Ubuntu_14_04_Resources.class.getResource("remoterequire_sshkeygen_out_expected.txt")),
    remoteRequireFooAuthorizedkeysExpected("/home/foo/.ssh/authorized_keys", Ubuntu_14_04_Resources.class.getResource("remoterequire_foo_authorizedkeys_expected.txt")),
    foobarAuthorizedkeysExpected("/home/foobar/.ssh/authorized_keys", Ubuntu_14_04_Resources.class.getResource("foobar_authorizedkeys_expected.txt")),

    static void copyUbuntu_14_04_Files(File parent) {
        sshkeygenCommand.createCommand parent
        groupsFile.createFile parent
        passwdFile.createFile parent
        sshdconfigFile.createFile parent
    }

    ResourcesUtils resources

    Ubuntu_14_04_Resources(String path, URL resource) {
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
