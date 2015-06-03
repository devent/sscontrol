/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService
import com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_ArchiveInstallConfig
import com.anrisoftware.sscontrol.httpd.fudforum.core.Fudforum_3_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.locale.ubuntu_12_04.Ubuntu_12_04_InstallLocaleFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Apache Ubuntu 12.04 FUDForum</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuApacheFudforumConfig extends Fudforum_3_Config implements ServiceConfig {

    @Inject
    FudforumPropertiesProvider fudforumPropertiesProvider

    @Inject
    UbuntuFudforumFromArchive fudforumFromArchive

    @Inject
    UbuntuApacheFcgiFudforumConfig fudforumFcgiConfig

    @Inject
    Ubuntu_12_04_FudforumBackup fudforumBackup

    @Inject
    Fudforum_3_ArchiveInstallConfig fudforumInstallConfig

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    Ubuntu_12_04_InstallLocaleFactory installLocaleFactory

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        checkService domain, service
        fudforumFcgiConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        checkService domain, service
        installPackages service
        fudforumBackup.backupService domain, service
        fudforumFcgiConfig.deployService domain, service, config
        deployServiceFromArchive domain, service
        fudforumInstallConfig.removeServiceFiles domain, service
        setupPermissions domain, service
    }

    void deployServiceFromArchive(Domain domain, WebService service) {
        fudforumFromArchive.deployService domain, service
        if (!fudforumFromArchive.isServiceInstalled(domain, service)) {
            deployInstall domain, service
            fudforumInstallConfig.installService domain, service
        } else if (fudforumFromArchive.isServiceDeployed(domain, service)) {
            fudforumInstallConfig.upgradeService domain, service
        }
    }

    /**
     * Installs the <i>FUDForum</i> packages.
     */
    void installPackages(FudforumService service) {
        def databasePackages = fudforumDatabasePackages(service.database["type"])
        def packages = []
        packages.addAll fudforumPackages
        packages.addAll databasePackages
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: packages,
                system: systemName,
                this, threads)()
    }

    /**
     * Installs specified locales.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param service
     *            the {@link FudforumService}.
     *
     */
    void installLocales(Domain domain, FudforumService service) {
        if (service.locales == null || service.locales.size() == 0) {
            return
        }
        installLocaleFactory.create(
                log: log,
                runCommands: runCommands,
                locales: service.locales,
                installCommand: installCommand,
                dpkgReconfigureCommand: reconfigureCommand,
                localesDirectory: localesDirectory,
                system: systemName,
                charset: charset,
                this, threads)()
    }

    @Override
    ContextProperties getFudforumProperties() {
        fudforumPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApacheFudforumConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        fudforumFcgiConfig.setScript script
        fudforumFromArchive.setScript this
        fudforumBackup.setScript this
        fudforumInstallConfig.setScript this
    }
}
