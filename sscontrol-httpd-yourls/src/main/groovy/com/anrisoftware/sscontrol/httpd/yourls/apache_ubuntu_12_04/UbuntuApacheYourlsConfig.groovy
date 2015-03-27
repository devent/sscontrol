/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.apache_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.yourls.core.Yourls_1_7_Config
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Apache Ubuntu 12.04 Yourls</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuApacheYourlsConfig extends Yourls_1_7_Config implements ServiceConfig {

    @Inject
    YourlsPropertiesProvider yourlsPropertiesProvider

    @Inject
    UbuntuYourlsFromArchive yourlsFromArchive

    @Inject
    UbuntuApacheFcgiYourlsConfig yourlsFcgiConfig

    @Inject
    Ubuntu_12_04_YourlsBackup yourlsBackup

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        yourlsFcgiConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        installPackages()
        yourlsBackup.backupService domain, service
        yourlsFcgiConfig.deployService domain, service, config
        yourlsFromArchive.deployService domain, service
        deployConfig domain, service
        setupPermissions domain, service
    }

    /**
     * Installs the <i>Yourls</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: yourlsPackages,
                system: systemName,
                this, threads)()
    }

    @Override
    ContextProperties getYourlsProperties() {
        yourlsPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApacheYourlsConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        yourlsFcgiConfig.setScript script
        yourlsFromArchive.setScript this
        yourlsBackup.setScript this
    }
}
