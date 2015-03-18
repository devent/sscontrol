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
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_Config
import com.anrisoftware.sscontrol.source.service.SourceServiceConfig
import com.anrisoftware.sscontrol.source.service.SourceSetupService

/**
 * <i>Gitolite Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuGitoliteConfig extends Gitolite_3_Config implements SourceServiceConfig {

    @Inject
    private GitolitePropertiesProvider ubuntuPropertiesProvider

    @Inject
    private UbuntuGitoliteFromArchive fromArchive

    @Inject
    private Ubuntu_12_04_Config ubuntuConfig

    @Override
    void deployService(SourceSetupService service) {
        setupDefaults service
        ubuntuConfig.installPackages()
        ubuntuConfig.createGitoliteUser service
        fromArchive.deployService service
        installGitolite service
        deployAdminKey service
        deployGitolitercConfig service
        updateRepositoriesPermissions service
    }

    ContextProperties getGitoliteProperties() {
        ubuntuPropertiesProvider.get()
    }

    String getServiceName() {
        Gitolite_Ubuntu_12_04_ConfigFactory.SERVICE_NAME
    }

    String getProfile() {
        Gitolite_Ubuntu_12_04_ConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        fromArchive.setScript this
        ubuntuConfig.setScript this
    }
}
