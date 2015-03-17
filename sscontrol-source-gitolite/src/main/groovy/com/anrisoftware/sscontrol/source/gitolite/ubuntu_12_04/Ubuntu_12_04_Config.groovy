/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.source.gitolite.ubuntu.UbuntuConfig

/**
 * <i>Gitolite Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_Config extends UbuntuConfig {

    @Inject
    private GitolitePropertiesProvider ubuntuPropertiesProvider

    ContextProperties getGitoliteProperties() {
        ubuntuPropertiesProvider.get()
    }

    String getServiceName() {
        Gitolite_Ubuntu_12_04_ConfigFactory.SERVICE_NAME
    }

    String getProfile() {
        Gitolite_Ubuntu_12_04_ConfigFactory.PROFILE_NAME
    }
}
