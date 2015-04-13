/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.localuser

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.scripts.resources.ResourcesUtils

/**
 * Test resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Resources {

    // local change password
    chpasswdCommand("/usr/sbin/chpasswd", Resources.class.getResource("echo_command.txt")),
    ubuntuChpasswdCommandOutExpected("/usr/sbin/chpasswd.out", Resources.class.getResource("ubuntu_chpasswd_out_excepted.txt")),
    redhatChpasswdCommandOutExpected("/usr/sbin/chpasswd.out", Resources.class.getResource("redhat_chpasswd_out_excepted.txt")),
    // local change user
    usermodCommand("/usr/sbin/usermod", Resources.class.getResource("echo_command.txt")),
    groupsUsermodOutExpected("/usr/sbin/usermod.out", Resources.class.getResource("groups_usermod_out_excepted.txt")),
    groupsAppendUsermodOutExpected("/usr/sbin/usermod.out", Resources.class.getResource("groups_append_usermod_out_excepted.txt")),
    shellUsermodOutExpected("/usr/sbin/usermod.out", Resources.class.getResource("shell_usermod_out_excepted.txt")),
    // local group add
    groupAddCommand("/usr/sbin/groupadd", Resources.class.getResource("echo_command.txt")),
    groupsFile("/etc/group", Resources.class.getResource("group.txt")),
    groupAddOutExpected("/usr/sbin/groupadd.out", Resources.class.getResource("group_groupadd_out_excepted.txt")),
    groupsFileWithGroup("/etc/group", Resources.class.getResource("group_withgroup.txt")),
    withGroupGroupAddOutExpected("/usr/sbin/groupadd.out", Resources.class.getResource("group_withgroup_groupadd_out_excepted.txt")),
    groupSystemAddOutExpected("/usr/sbin/groupadd.out", Resources.class.getResource("group_system_groupadd_out_excepted.txt")),
    // local user add
    userAddCommand("/usr/sbin/useradd", Resources.class.getResource("echo_command.txt")),
    passwdFile("/etc/passwd", Resources.class.getResource("passwd.txt")),
    userAddOutExpected("/usr/sbin/useradd.out", Resources.class.getResource("user_useradd_out_excepted.txt")),
    userSystemAddOutExpected("/usr/sbin/useradd.out", Resources.class.getResource("user_system_useradd_out_excepted.txt")),
    homeDirUserAddOutExpected("/usr/sbin/useradd.out", Resources.class.getResource("user_homedir_useradd_out_excepted.txt")),
    shellUserAddOutExpected("/usr/sbin/useradd.out", Resources.class.getResource("user_shell_useradd_out_excepted.txt")),
    passwdFileWithUser("/etc/passwd", Resources.class.getResource("passwd_withuser.txt")),
    withUserUserAddOutExpected("/usr/sbin/useradd.out", Resources.class.getResource("user_withuser_useradd_out_excepted.txt")),
    // local user info
    idCommand("/usr/bin/id", Resources.class.getResource("id_command.txt")),
    idNoUserCommand("/usr/bin/id", Resources.class.getResource("id_nouser_command.txt")),

    static void copyTestFiles(File parent) {
        chpasswdCommand.createCommand parent
        usermodCommand.createCommand parent
        groupAddCommand.createCommand parent
        userAddCommand.createCommand parent
    }

    ResourcesUtils resources

    Resources(String path, URL resource) {
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
