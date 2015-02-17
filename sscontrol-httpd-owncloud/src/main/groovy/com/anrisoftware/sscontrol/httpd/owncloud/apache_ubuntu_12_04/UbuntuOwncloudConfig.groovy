/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.owncloud.core.Owncloud_7_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu 12.04 ownCloud</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuOwncloudConfig extends Owncloud_7_Config implements ServiceConfig {

    @Inject
    OwncloudPropertiesProvider owncloudPropertiesProvider

    @Inject
    UbuntuOwncloudFromArchive owncloudFromArchive

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UbuntuApacheOwncloudConfig apacheConfig

    @Inject
    OwncloudBackup owncloudBackup

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        apacheConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        installPackages()
        owncloudBackup.backupService domain, service
        apacheConfig.deployService domain, service, config
        owncloudFromArchive.deployService domain, service
        deployConfig domain, service
        setupPermissions domain, service
    }

    /**
     * Installs the <i>ownCloud</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: owncloudPackages,
                system: systemName,
                this, threads)()
    }

    @Override
    ContextProperties getOwncloudProperties() {
        owncloudPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        ApacheOwncloudConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        apacheConfig.setScript this
        owncloudFromArchive.setScript this
        owncloudBackup.setScript this
    }
}
