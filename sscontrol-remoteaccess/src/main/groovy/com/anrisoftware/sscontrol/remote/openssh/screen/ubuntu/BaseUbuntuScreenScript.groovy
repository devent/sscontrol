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
package com.anrisoftware.sscontrol.remote.openssh.screen.ubuntu

import com.anrisoftware.sscontrol.remote.openssh.screen.linux.ScreenScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.User

/**
 * Screen script for local users for Ubuntu.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseUbuntuScreenScript extends ScreenScript {

    @Override
    void deployScreenScript(RemoteService service) {
        installPackages screenPackages
        super.deployScreenScript service
        service.users.each { User user ->
            appendScreenSession "bashrc", bashConfigFile(user)
        }
    }

    /**
     * Returns bash-rc file, for example {@code ".bashrc".}
     *
     * <ul>
     * <li>profile property {@code "bash_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File bashConfigFile(User user) {
        def file = profileProperty "bash_configuration_file", defaultProperties
        new File(homeDir(user), file)
    }

    /**
     * Returns the screen packages, for example {@code "screen".}
     *
     * <ul>
     * <li>profile property {@code "screen_packages"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getScreenPackages() {
        profileListProperty "screen_packages", defaultProperties
    }

    /**
     * Returns auto-screen script file, for example {@code "auto_script.sh".}
     * If the file path is not absolute it is assumed under the local scripts
     * directory.
     *
     * <ul>
     * <li>profile property {@code "auto_screen_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getLocalBinDirectory()
     */
    File getAutoScreenFile() {
        profileFileProperty "auto_screen_file", localBinDirectory, defaultProperties
    }

    /**
     * Returns screen-rc file for the specified user, for
     * example {@code ".screenrc".}
     *
     * <ul>
     * <li>profile property {@code "screen_configuration_file"}</li>
     * </ul>
     *
     * @see #homeDir(User)
     * @see #getDefaultProperties()
     */
    File screenConfigFile(User user) {
        def file = profileProperty "screen_configuration_file", defaultProperties
        new File(homeDir(user), file)
    }
}
