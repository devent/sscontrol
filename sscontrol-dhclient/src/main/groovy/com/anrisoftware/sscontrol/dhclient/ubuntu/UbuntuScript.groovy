/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.ubuntu

import static java.util.regex.Pattern.*

import javax.inject.Inject

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.dhclient.service.DhclientService
import com.anrisoftware.sscontrol.dhclient.statements.DeclarationFactory
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclarationFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * Dhclient/Ubuntu.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuScript extends LinuxScript {

    Templates dhclientTemplates

    TemplateResource dhclientConfiguration

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    DeclarationFactory declarationFactory

    @Inject
    OptionDeclarationFactory optionDeclarationFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    @Override
    def run() {
        dhclientTemplates = templatesFactory.create "DhclientUbuntu"
        dhclientConfiguration = dhclientTemplates.getResource "configuration"
        setupDefaultOption service
        setupDefaultSends service
        setupDefaultRequests service
        distributionSpecificConfiguration()
        deployConfiguration()
        restartService()
    }

    /**
     * Setups default option.
     */
    void setupDefaultOption(DhclientService service) {
        if (service.getOption() != null) {
            return
        }
        service.setOption declarationFactory.create(defaultOption)
    }

    /**
     * Setups default sends.
     */
    void setupDefaultSends(DhclientService service) {
        defaultSends.each {
            String[] str = StringUtils.split it, " "
            service.addSend optionDeclarationFactory.create(str[0], str[1])
        }
    }

    /**
     * Setups default requests.
     */
    void setupDefaultRequests(DhclientService service) {
        defaultRequests.each {
            service.addRequest declarationFactory.create(it)
        }
    }

    /**
     * Do the distribution specific configuration.
     */
    abstract void distributionSpecificConfiguration()

    /**
     * Deploys the <i>dhclient</i> configuration.
     */
    void deployConfiguration() {
        deployConfiguration configurationTokens(), currentConfiguration, configurations, configurationFile
    }

    /**
     * Restarts the <i>dhclient</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log, command: restartCommand, services: restartServices, this, threads)()
    }

    /**
     * Returns the dhclient/configurations.
     */
    List getConfigurations() {
        [
            openConfig(service),
            sendsConfig(service),
            requestConfig(service),
            prependsConfig(service),
        ]
    }

    def openConfig(DhclientService service) {
        def search = dhclientConfiguration.getText true, "optionSearch"
        def conf = dhclientConfiguration.getText true, "option", "declaration", service.option
        new TokenTemplate(search, conf)
    }

    def sendsConfig(DhclientService service) {
        def search = dhclientConfiguration.getText true, "sendSearch"
        service.sends.inject([]) { list, send ->
            def conf = dhclientConfiguration.getText true, "send", "declaration", send
            list << new TokenTemplate(search, conf)
        }
    }

    def requestConfig(DhclientService service) {
        def search = dhclientConfiguration.getText true, "requestSearch"
        def conf = dhclientConfiguration.getText true, "request", "requests", service.requests
        new TokenTemplate(search, conf, DOTALL)
    }

    def prependsConfig(DhclientService service) {
        def search = dhclientConfiguration.getText true, "prependSearch"
        service.prepends.inject([]) { list, prepend ->
            def conf = dhclientConfiguration.getText true, "prepend", "declaration", prepend
            list << new TokenTemplate(search, conf)
        }
    }

    /**
     * Returns the dhclient/configuration file {@code dhclient.conf}.
     *
     * <ul>
     * <li>profile property key {@code configuration_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getConfigurationFile() {
        profileFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the default dhclient option.
     *
     * <ul>
     * <li>profile property key {@code default_option}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultOption() {
        profileProperty "default_option", defaultProperties
    }

    /**
     * Returns the default dhclient sends.
     *
     * <ul>
     * <li>profile property key {@code default_sends}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultSends() {
        profileListProperty "default_sends", ",", defaultProperties
    }

    /**
     * Returns the default dhclient requests.
     *
     * <ul>
     * <li>profile property key {@code default_requests}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultRequests() {
        profileListProperty "default_requests", defaultProperties
    }

    /**
     * Returns the current dhclient/configuration.
     */
    String getCurrentConfiguration() {
        currentConfiguration configurationFile
    }
}
