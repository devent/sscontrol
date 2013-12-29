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
        remoteScript.users.deployRemoteScript service
    }

    void setupParentScript() {
        remoteScript.each { key, RemoteScript value ->
            value.setScript this
        }
    }

    @Override
    RemoteService getService() {
        super.getService();
    }
}
