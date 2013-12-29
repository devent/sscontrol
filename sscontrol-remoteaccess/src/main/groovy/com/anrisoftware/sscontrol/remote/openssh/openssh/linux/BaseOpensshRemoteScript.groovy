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
package com.anrisoftware.sscontrol.remote.openssh.openssh.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService

/**
 * Base OpenSSH remote script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseOpensshRemoteScript extends LinuxScript {

    @Inject
    Map<String, RemoteScript> remoteScript

    @Override
    def run() {
        setupParentScript()
        super.run()
        beforeConfiguration()
        remoteScript.users.deployRemoteScript service
        remoteScript.userkey.deployRemoteScript service
    }

    void setupParentScript() {
        remoteScript.each { key, RemoteScript value ->
            value.setScript this
        }
    }

    /**
     * Deploys the configuration before the script configuration.
     */
    void beforeConfiguration() {
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
     * Returns the user SSH/key pattern, for
     * example {@code "/home/%1$s/.ssh/id_rsa".}
     *
     * <ul>
     * <li>profile property {@code "ssh_key_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSshkeyPattern() {
        profileProperty "ssh_key_pattern", defaultProperties
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
     * Returns SSH/key generation command, for
     * example {@code "/usr/bin/ssh-keygen".}
     *
     * <ul>
     * <li>profile property {@code "key_gen_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getKeyGenCommand() {
        profileProperty "key_gen_command", defaultProperties
    }

    @Override
    RemoteService getService() {
        super.getService();
    }
}
