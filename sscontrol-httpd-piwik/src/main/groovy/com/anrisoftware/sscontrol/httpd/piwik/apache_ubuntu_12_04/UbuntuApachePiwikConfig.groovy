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
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.core.Piwik_2_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Piwik</i> configuration for <i>Apache Ubuntu 12.04</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuApachePiwikConfig extends Piwik_2_Config implements ServiceConfig {

    @Inject
    PiwikPropertiesProvider piwikPropertiesProvider

    @Inject
    UbuntuPiwikFromArchive ubuntuPiwikFromArchive

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UbuntuApacheFcgiPiwikConfig apacheFcgiPiwikConfig

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        apacheFcgiPiwikConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        installPackages()
        apacheFcgiPiwikConfig.deployService domain, service, config
        ubuntuPiwikFromArchive.deployService domain, service
        deployConfig domain, service
    }

    /**
     * Installs the <i>Piwik</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: piwikPackages,
                system: systemName,
                this, threads)()
    }

    @Override
    ContextProperties getPiwikProperties() {
        piwikPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApachePiwikConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        apacheFcgiPiwikConfig.setScript script
        ubuntuPiwikFromArchive.setScript this
    }
}
