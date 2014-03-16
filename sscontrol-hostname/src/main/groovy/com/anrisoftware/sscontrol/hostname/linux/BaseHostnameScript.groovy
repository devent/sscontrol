/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname.linux

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Base hostname script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseHostnameScript extends LinuxScript {

    Templates hostnameTemplates

    @Override
    def run() {
        super.run()
        hostnameTemplates = templatesFactory.create "Hostname"
        distributionSpecificConfiguration()
        deployHostnameConfiguration()
        restartServices()
    }

    /**
     * Do some distribution specific configuration.
     */
    abstract void distributionSpecificConfiguration()

    /**
     * Deploys the hostname configuration.
     */
    void deployHostnameConfiguration() {
        deployConfiguration configurationTokens(), currentHostnameConfiguration, hostnameConfiguration, hostnameFile
    }

    /**
     * Returns the current hostname configuration.
     */
    String getCurrentHostnameConfiguration() {
        currentConfiguration hostnameFile
    }

    /**
     * Returns the hostname configuration.
     */
    List getHostnameConfiguration() {
        [
            new TokenTemplate(".*", hostnameTemplates.getResource("hostname_configuration").getText(true, "hostname", service.hostname))
        ]
    }

    /**
     * Returns the hostname configuration file, for
     * example {@code "hostname".} If the file path is not absolute then the
     * file is assumed under the configuration directory.
     *
     * <ul>
     * <li>property key {@code configuration_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getHostnameFile() {
        profileFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the hostname configuration file, for
     * example {@code "/etc".}
     *
     * <ul>
     * <li>property key {@code configuration_file}</li>
     * </ul>
     */
    File getConfigurationFile() {
        profileDirProperty "configuration_directory", defaultProperties
    }
}
