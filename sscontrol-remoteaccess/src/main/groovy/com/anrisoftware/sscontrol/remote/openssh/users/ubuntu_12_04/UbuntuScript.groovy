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
package com.anrisoftware.sscontrol.remote.openssh.users.ubuntu_12_04

import com.anrisoftware.sscontrol.remote.openssh.users.linux.LocalUsersScript
import com.anrisoftware.sscontrol.remote.service.RemoteService

/**
 * Local users script for Ubuntu 12.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScript extends LocalUsersScript {

    @Override
    void deployRemoteScript(RemoteService service) {
        super.deployRemoteScript service
    }
}
