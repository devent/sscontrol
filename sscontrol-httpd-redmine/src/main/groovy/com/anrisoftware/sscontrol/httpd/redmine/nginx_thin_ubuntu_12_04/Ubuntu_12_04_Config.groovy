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
package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_5_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Redmine</i> configuration for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_Config extends Redmine_2_5_Config implements ServiceConfig {

    @Inject
    RedminePropertiesProvider propertiesProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UbuntuRedmineFromArchive redmineFromArchive

    @Inject
    UbuntuThinConfig thinConfig

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        redmineFromArchive.deployDomain domain, refDomain, service, config
        thinConfig.deployDomain domain, refDomain, service, config
        super.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        installPackages()
        redmineFromArchive.deployService domain, service, config
        thinConfig.deployService domain, service, config
        super.deployService domain, service, config
    }

    /**
     * Installs the <i>Redmine</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: script.installCommand,
                packages: redminePackages,
                this, threads)()
    }

    @Override
    ContextProperties getRedmineProperties() {
        propertiesProvider.get()
    }

    @Override
    String getProfile() {
        RedmineConfigFactory.PROFILE_NAME
    }

    @Override
    public void setScript(LinuxScript script) {
        super.setScript(script)
        redmineFromArchive.setScript this
        thinConfig.setScript this
    }
}
