/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.roundcube_1_apache_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeServiceImpl
import com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Roundcube 1.x Apache Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuRoundcubeConfig extends Roundcube_1_Config implements ServiceConfig {

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UbuntuApacheRoundcubeConfig apacheConfig

    @Inject
    UbuntuRoundcubeFromArchiveConfig fromArchiveConfig

    @Inject
    RoundcubeBackup roundcubeBackup

    @Inject
    UbuntuRoundcubePhp_5_Config roundcubePhpConfig

    @Inject
    RoundcubePropertiesProvider ubuntuPropertiesProvider

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults service
        apacheConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults service
        apacheConfig.deployService domain, service, config
        roundcubePhpConfig.deployPhpini domain
        roundcubeBackup.backupService domain, service
        installPackages()
        installDatabasePackages service
        fromArchiveConfig.deployService domain, service
        setupDatabase domain, service
        deployImapConfig domain, service
        deployDatabaseConfig domain, service
        deploySmtpConfig domain, service
        deployDebugConfig domain, service
        setupPermissions domain, service
    }

    /**
     * Installs the <i>Roundcube</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: roundcubePackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Installs the database packages.
     *
     * @param service
     *            the {@link RoundcubeServiceImpl} service.
     */
    void installDatabasePackages(RoundcubeServiceImpl service) {
        def packages = roundcubeDatabasePackages service.database.driver
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: packages,
                system: systemName,
                this, threads)()
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        apacheConfig.setScript this
        fromArchiveConfig.setScript this
        roundcubeBackup.setScript this
        roundcubePhpConfig.setScript this
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApacheRoundcubeConfigFactory.PROFILE_NAME
    }

    @Override
    ContextProperties getRoundcubeProperties() {
        ubuntuPropertiesProvider.get()
    }
}
