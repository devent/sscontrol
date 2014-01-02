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
package com.anrisoftware.sscontrol.remote.openssh.fail2ban.ubuntu_10_04

import com.anrisoftware.sscontrol.remote.openssh.fail2ban.linux.BaseFail2BanScript
import com.anrisoftware.sscontrol.remote.service.RemoteService

/**
 * fail2ban script for Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScript extends BaseFail2BanScript {

    @Override
    void beforeFail2banConfiguration(RemoteService service) {
        installPackages fail2banPackages
    }

    @Override
    void deployFail2banScript(RemoteService service) {
        super.deployFail2banScript service
        restartServices restartCommand: fail2banRestartCommand, services: fail2banRestartServices
    }
}
