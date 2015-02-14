/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.linux

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.hosts.utils.HostFormatFactory

/**
 * Deploys the hosts on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class HostsScript extends LinuxScript {

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    HostFormatFactory hostsFormat

    Templates hostsTemplates

    TemplateResource hostsConfiguration

    @Override
    def run() {
        hostsTemplates = templatesFactory.create "Hosts"
        hostsConfiguration = hostsTemplates.getResource("hosts_configuration")
        setupDefaultHosts()
        deployHostsConfiguration()
    }

    /**
     * Sets the default hosts.
     */
    void setupDefaultHosts() {
        service.addHostsHead defaultHosts
    }

    /**
     * Deploys the <i>hosts</i> configuration.
     */
    void deployHostsConfiguration() {
        deployConfiguration configurationTokens(), currentHostsConfiguration, hostsConfigurations, hostsFile
    }

    /**
     * Returns the configuration for each host.
     */
    List getHostsConfigurations() {
        service.hosts.inject([]) { list, host ->
            list << new TokenTemplate("${host.address}.*", hostConfiguration(host))
        }
    }

    String hostConfiguration(def host) {
        hostsConfiguration.getText(true, "host", "host", host)
    }

    /**
     * Returns the current <i>hosts</i> configuration.
     */
    String getCurrentHostsConfiguration() {
        currentConfiguration hostsFile
    }

    /**
     * Returns the hosts file.
     */
    File getHostsFile() {
        new File(configurationDir, configurationFile)
    }

    /**
     * Returns the default hosts.
     * <p>
     * <ul>
     * <li>property {@code "default_hosts"}</li>
     * </ul>
     */
    List getDefaultHosts() {
        profileTypedListProperty "default_hosts", hostsFormat.create(), defaultProperties
    }

    /**
     * Returns the <i>hosts</i> configuration file.
     * <p>
     * <ul>
     * <li>property {@code "configuration_file"}</li>
     * </ul>
     */
    abstract String getConfigurationFile()
}
