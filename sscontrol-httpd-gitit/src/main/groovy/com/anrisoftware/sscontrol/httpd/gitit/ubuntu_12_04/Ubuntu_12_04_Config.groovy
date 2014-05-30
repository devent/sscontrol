/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_Config
import com.anrisoftware.sscontrol.httpd.gitit.core.RepositoryTypeRenderer
import com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04.GititConfigFactory
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Gitit</i> configuration for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_Config extends Gitit_0_10_Config implements ServiceConfig {

    @Inject
    private RepositoryTypeRenderer repositoryTypeRenderer

    @Inject
    GititPropertiesProvider gititProperties

    @Inject
    UbuntuHsenvFromSourceConfig ubuntuHsenvFromSourceConfig

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    TemplatesFactory templatesFactory

    Templates gititTemplates

    TemplateResource gititCommandTemplate

    TemplateResource gititConfigTemplate

    TemplateResource gititServiceConfigTemplate

    TemplateResource gititServiceDefaultsConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        ubuntuHsenvFromSourceConfig.deployDomain domain, refDomain, service, config
        super.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        installPackages()
        ubuntuHsenvFromSourceConfig.deployService domain, service, config
        super.deployService domain, service, config
    }

    /**
     * Installs the <i>Gitit</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: script.installCommand, packages: gititPackages,
                this, threads)()
    }

    @Override
    String gititCommand(Domain domain, GititService service) {
        ubuntuHsenvFromSourceConfig.hsenvGititCommand domain, service
    }

    @Override
    TemplateResource getGititConfigTemplate() {
        gititConfigTemplate
    }

    @Override
    TemplateResource getGititServiceTemplate() {
        gititServiceConfigTemplate
    }

    @Override
    TemplateResource getGititServiceDefaultsTemplate() {
        gititServiceDefaultsConfigTemplate
    }

    @Override
    ContextProperties getGititProperties() {
        gititProperties.get()
    }

    @Override
    String getProfile() {
        GititConfigFactory.PROFILE_NAME
    }

    @Override
    public void setScript(LinuxScript script) {
        super.setScript(script)
        ubuntuHsenvFromSourceConfig.setScript this
        gititTemplates = templatesFactory.create "Gitit_Ubuntu_12_04", ["renderers": [repositoryTypeRenderer]]
        gititCommandTemplate = gititTemplates.getResource "gititcommands"
        gititConfigTemplate = gititTemplates.getResource "gititconfig"
        gititServiceConfigTemplate = gititTemplates.getResource "gititserviceconfig"
        gititServiceDefaultsConfigTemplate = gititTemplates.getResource "gititservicedefaultsconfig"
    }
}
