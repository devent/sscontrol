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
package com.anrisoftware.sscontrol.remote.openssh.users.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.Require
import com.anrisoftware.sscontrol.remote.user.User
import com.anrisoftware.sscontrol.remote.user.UserFactory
import com.anrisoftware.sscontrol.scripts.localchangepassword.LocalChangePasswordFactory
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddFactory

/**
 * Local users script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class LocalUsersScript implements RemoteScript {

    @Inject
    UserFactory userFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalChangePasswordFactory localChangePasswordFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    LinuxScript script

    @Override
    void deployRemoteScript(RemoteService service) {
        createLocalGroups()
        createLocalUsers()
    }

    /**
     * Create local user group.
     */
    void createLocalGroups() {
        def service = getService()
        def groups = loadGroups()
        int id = minimumGid
        service.users.each { User user ->
            if (user.group == null) {
                user.group user.name
            }
            def found = groups.find { it.name == user.group }
            if (found) {
                groupHaveId found, id, { id++ }
                user.groupId = found.gid
            }
            if (!found) {
                updateGroupId user, id, { id++ }
                localGroupAddFactory.create(
                        log: log,
                        command: script.groupAddCommand,
                        groupsFile: script.groupsFile,
                        groupId: user.groupId,
                        groupName: user.group,
                        this, threads)()
            }
        }
    }

    void groupHaveId(Map group, int id, def callback) {
        if (group.gid == id) {
            callback()
        }
    }

    void updateGroupId(User user, int id, def callback) {
        if (user.groupId == null) {
            user.group user.group, gid: id
            callback()
        }
    }

    /**
     * Create local users.
     */
    void createLocalUsers() {
        def service = getService()
        def users = loadUsers()
        int id = minimumUid
        service.users.each { User user ->
            Map found = users.find { it.name == user.name }
            if (found) {
                user.home = found.home
                userHaveId found, id, { id++ }
                user.uid = found.uid
                updateUserPassword user
            }
            if (!found) {
                updateUserId user, id, { id++ }
                user.home homeDir(user)
                if (user.login == null) {
                    user.login defaultLoginShell
                }
                localUserAddFactory.create(
                        log: log,
                        command: script.userAddCommand,
                        usersFile: script.usersFile,
                        userName: user.name,
                        homeDir: user.home,
                        shell: user.login,
                        groupName: user.group,
                        userId: user.uid,
                        this, threads)()
            }
        }
    }

    void userHaveId(Map user, int id, def callback) {
        if (user.uid == id) {
            callback()
        }
    }

    void updateUserId(User user, int id, def callback) {
        if (user.uid == null) {
            user.user uid: id
            callback()
        }
    }

    /**
     * Updates the user password.
     *
     * @param user
     *            the {@link User}.
     */
    void updateUserPassword(User user) {
        if (!user.requires.contains(Require.password)) {
            return
        }
        localChangePasswordFactory.create(
                log: log,
                command: changePasswordCommand,
                name: distributionName,
                userName: user.name,
                password: user.password,
                this, threads)()
    }

    /**
     * Returns distribution name, for example {@code "debian", "ubuntu", "redhat".}
     *
     * <ul>
     * <li>profile property {@code "distribution_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDistributionName() {
        profileProperty "distribution_name", defaultProperties
    }

    /**
     * Returns minimal user ID, for example {@code "1000".}
     *
     * <ul>
     * <li>profile property {@code "minimum_uid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumUid() {
        profileNumberProperty "minimum_uid", defaultProperties
    }

    /**
     * Returns minimal group ID, for example {@code "1000".}
     *
     * <ul>
     * <li>profile property {@code "minimum_gid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumGid() {
        profileNumberProperty "minimum_gid", defaultProperties
    }

    /**
     * Returns the user home directory pattern, for
     * example {@code "/home/%1$s".}
     *
     * <ul>
     * <li>profile property {@code "home_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getHomePattern() {
        profileProperty "home_pattern", defaultProperties
    }

    /**
     * Returns the default log-in shell, for
     * example {@code "/bin/bash".}
     *
     * <ul>
     * <li>profile property {@code "default_login_shell"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultLoginShell() {
        profileProperty "default_login_shell", defaultProperties
    }

    /**
     * Returns the current local users.
     *
     * @returns {@link List} of
     * the {@link Map} {@code name:=<name>, password:=<password>, uid:=<uid>, gid:=<gid>, comment:=<comment>, home:=<home>, login:=<login>}
     *
     * @see #getUsersFile()
     */
    List loadUsers() {
        def file = usersFile
        lineIterator(file, charset.name()).inject([]) { acc, it ->
            String[] str = StringUtils.splitPreserveAllTokens it, ":"
            String name = str[0]
            int uid = Integer.valueOf str[2]
            int gid = Integer.valueOf str[3]
            String comment = str[4]
            String home = str[5]
            String login = str[6]
            def user = [name: name, password: "", uid: uid, gid: gid]
            user.comment = comment
            user.home = home
            user.login = login
            acc << user
        }
    }

    /**
     * Returns the current local user groups.
     *
     * @returns {@link List} of
     * the {@link Map} {@code name:=<name>, gid:=<gid>}
     *
     * @see #getGroupsFile()
     */
    List loadGroups() {
        def file = groupsFile
        lineIterator(file, charset.name()).inject([]) { acc, it ->
            String[] str = StringUtils.splitPreserveAllTokens it, ":"
            String name = str[0]
            int gid = Integer.valueOf str[2]
            acc << [name: name, gid: gid]
        }
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
    }

    @Override
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
