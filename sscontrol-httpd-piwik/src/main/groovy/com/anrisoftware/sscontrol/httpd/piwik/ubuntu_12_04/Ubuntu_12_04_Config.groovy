/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.httpd.piwik.core.Piwik_2_3_Config
import com.anrisoftware.sscontrol.httpd.piwik.nginx_ubuntu_12_04.PiwikConfigFactory
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Piwik</i> configuration for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_Config extends Piwik_2_3_Config implements ServiceConfig {

    @Inject
    PiwikPropertiesProvider piwikPropertiesProvider

    @Inject
    UbuntuPiwikFromArchive ubuntuPiwikFromArchive

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    TemplatesFactory templatesFactory

    Templates piwikTemplates

    TemplateResource piwikConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        ubuntuPiwikFromArchive.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        installPackages()
        ubuntuPiwikFromArchive.deployService domain, service, config
        deployConfig domain, service
    }

    /**
     * Installs the <i>Piwik</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: script.installCommand, packages: piwikPackages,
                this, threads)()
    }

    @Override
    TemplateResource getPiwikConfigTemplate() {
        piwikConfigTemplate
    }

    @Override
    ContextProperties getPiwikProperties() {
        piwikPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        PiwikConfigFactory.PROFILE_NAME
    }

    @Override
    public void setScript(LinuxScript script) {
        super.setScript(script)
        ubuntuPiwikFromArchive.setScript this
        piwikTemplates = templatesFactory.create "Piwik_Ubuntu_12_04", ["renderers": [debugLevelRenderer]]
        piwikConfigTemplate = piwikTemplates.getResource "config"
    }
}
