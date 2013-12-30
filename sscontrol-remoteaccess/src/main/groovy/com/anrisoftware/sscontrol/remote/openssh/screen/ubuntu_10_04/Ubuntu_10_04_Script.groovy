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
package com.anrisoftware.sscontrol.remote.openssh.screen.ubuntu_10_04

import com.anrisoftware.sscontrol.remote.openssh.screen.linux.ScreenScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.User

/**
 * Screen script for local users for Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04_Script extends ScreenScript {

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
}
