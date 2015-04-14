/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.repo.ubuntu.RepoScript
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Repo Ubuntu 12.04</i> configuration script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScript extends RepoScript {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    UbuntuAptConfig aptConfig

    @Override
    def run() {
        installPackages()
        aptConfig.signRepositories service
        def sources = aptConfig.readSources service
        aptConfig.deploySources service, sources
        aptConfig.deployProxy service
        updatePackages()
    }

    @Inject
    void setUbuntuAptConfig(UbuntuAptConfig config) {
        config.setScript this
        this.aptConfig = config
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
