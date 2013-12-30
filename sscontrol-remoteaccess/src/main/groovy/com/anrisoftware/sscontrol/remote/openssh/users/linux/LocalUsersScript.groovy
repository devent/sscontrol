/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.users.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.Group
import com.anrisoftware.sscontrol.remote.user.GroupFactory
import com.anrisoftware.sscontrol.remote.user.Require
import com.anrisoftware.sscontrol.remote.user.User
import com.anrisoftware.sscontrol.remote.user.UserFactory

/**
 * Local users script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class LocalUsersScript implements RemoteScript {

    @Inject
    UserFactory userFactory

    @Inject
    GroupFactory groupFactory

    Templates localUsersTemplates

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
            Group found = groups.find { Group it -> it.name == user.group.name }
            if (found) {
                groupHaveId found, id, { id++ }
            }
            if (!found) {
                updateGroupId user.group, id, { id++ }
                def group = user.group.name
                addGroup "groupName": group, "groupId": user.group.gid
            }
        }
    }

    void groupHaveId(Group group, int id, def callback) {
        if (group.gid == id) {
            callback()
        }
    }

    void updateGroupId(Group group, int id, def callback) {
        if (group.gid == null) {
            group.gid = id
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
            User found = users.find { User it -> it.name == user.name }
            if (found) {
                user.home = found.home
                userHaveId found, id, { id++ }
                updateUserPassword user
            }
            if (!found) {
                updateUserId user, id, { id++ }
                user.home = homeDir user
                def shell = user.login == null ? defaultLoginShell : user.login
                def group = user.group.name
                addUser "userName": user.name, "homeDir": user.home, "shell": shell, "groupName": group, "userId": user.uid
            }
        }
    }

    void userHaveId(User user, int id, def callback) {
        if (user.uid == id) {
            callback()
        }
    }

    void updateUserId(User user, int id, def callback) {
        if (user.uid == null) {
            user.uid = id
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
        changePassword "userName": user.name, "password": user.password
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
     * @see #getUsersFile()
     */
    List loadUsers() {
        def users = []
        def file = usersFile
        lineIterator(file, charset.name()).inject([]) { acc, it ->
            String[] str = StringUtils.splitPreserveAllTokens it, ":"
            String name = str[0]
            int uid = Integer.valueOf str[2]
            int gid = Integer.valueOf str[3]
            String comment = str[4]
            String home = str[5]
            String login = str[6]
            def user = userFactory.create service, ["name": name, "password": "", "uid": uid, "gid": gid]
            user.comment = comment
            user.home = new File(home)
            user.login = login
            acc << user
        }
    }

    /**
     * Returns the current local user groups.
     *
     * @see #getGroupsFile()
     */
    List loadGroups() {
        def groups = []
        def file = groupsFile
        lineIterator(file, charset.name()).inject([]) { acc, it ->
            String[] str = StringUtils.splitPreserveAllTokens it, ":"
            String name = str[0]
            int gid = Integer.valueOf str[2]
            def group = groupFactory.create service, ["name": name, "gid": gid]
            acc << group
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