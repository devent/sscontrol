/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.deadwood.deadwood_3_2

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.api.Service
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * <i>MaraDNS-Deadwood 3.2.x</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Deadwood_3_2_Script {

    /**
     * The {@link Service} of the script.
     */
    Service service

    Templates deadwoodTemplates

    TemplateResource deadwoodConfiguration

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.deadwoodTemplates = factory.create("Deadwood_3_2")
        this.deadwoodConfiguration = deadwoodTemplates.getResource("configuration")
    }

    /**
     * Deploys the <i>Deadwood</i> configuration.
     */
    void deployMaraDnsConfiguration() {
        deployConfiguration configurationTokens(), currentDeadwoodConfiguration, deadwoodConfigurations, deadwoodrcFile
    }

    /**
     * Returns the <i>Deadwood</i> configurations.
     */
    List getDeadwoodConfigurations() {
        [
            bindAddressConfiguration,
            ipv4BindAddressConfiguration,
            rootServersConfiguration,
            recursiveConfiguration,
        ]
    }

    def getBindAddressConfiguration() {
        def search = deadwoodConfiguration.getText(true, "bind_address_search")
        def replace = deadwoodConfiguration.getText(true, "bind_address", "service", service)
        new TokenTemplate(search, replace)
    }

    def getIpv4BindAddressConfiguration() {
        def search = deadwoodConfiguration.getText(true, "ip4_bind_address_search")
        def replace = deadwoodConfiguration.getText(true, "ip4_bind_address", "service", service, "properties", this)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the current <i>Deadwood</i> configuration.
     */
    String getCurrentDeadwoodConfiguration() {
        currentConfiguration deadwoodrcFile
    }

    /**
     * Returns the file of the Deadwood configuration file.
     *
     * <ul>
     * <li>profile property key {@code configuration_file}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getDefaultProperties()
     */
    File getDeadwoodrcFile() {
        profileFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the user name under which <i>Deadwood</i> is run.
     *
     * <ul>
     * <li>profile property key {@code deadwood_user}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDeadwoodUser() {
        profileProperty "deadwood_user", defaultProperties
    }

    /**
     * Returns the user name under which <i>Deadwood</i> is run.
     *
     * <ul>
     * <li>profile property key {@code deadwood_group}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDeadwoodGroup() {
        profileProperty "deadwood_group", defaultProperties
    }

    /**
     * Returns the default binding addresses, for example {@code "127.0.0.1"}.
     *
     * <ul>
     * <li>profile property key {@code default_binding_addresses}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBindingAddresses() {
        profileListProperty "default_binding_addresses", defaultProperties
    }

    /**
     * Returns the default maximum requests, for example {@code 8}.
     *
     * <ul>
     * <li>profile property key {@code default_max_requests}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultMaxRequests() {
        profileNumberProperty "default_max_requests", defaultProperties
    }

    /**
     * Returns default handle overload enabled, for example {@code true}.
     *
     * <ul>
     * <li>profile property key {@code default_handle_overload}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getDefaultHandleOverload() {
        profileBooleanProperty "default_handle_overload", defaultProperties
    }

    /**
     * Returns the default maximum cache, for example {@code 60000}.
     *
     * <ul>
     * <li>profile property key {@code default_max_cache}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    long getDefaultMaxCache() {
        profileNumberProperty "default_max_cache", defaultProperties
    }

    /**
     * Returns the default cache file name.
     *
     * <ul>
     * <li>profile property key {@code default_cache_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultCacheFile() {
        profileProperty "default_cache_file", defaultProperties
    }

    /**
     * Returns default fetch expired records enabled, for example {@code true}.
     *
     * <ul>
     * <li>profile property key {@code default_resurrections}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getDefaultResurrection() {
        profileBooleanProperty "default_resurrections", defaultProperties
    }

    /**
     * Sets the parent script.
     *
     * @param script
     *            the {@link LinuxScript} parent script.
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
