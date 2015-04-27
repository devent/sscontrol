/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.apache_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.frontaccounting.core.Frontaccounting_2_3_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu 12.04 Apache FrontAccounting</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_Apache_FrontaccountingConfig extends Frontaccounting_2_3_Config implements ServiceConfig {

    @Inject
    FrontaccountingPropertiesProvider frontaccountingPropertiesProvider

    @Inject
    Ubuntu_12_04_FrontaccountingFromArchive frontaccountingFromArchive

    @Inject
    Ubuntu_12_04_Apache_Fcgi_FrontaccountingConfig frontaccountingFcgiConfig

    @Inject
    Ubuntu_12_04_FrontaccountingBackup frontaccountingBackup

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        frontaccountingFcgiConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        installPackages()
        frontaccountingBackup.backupService domain, service
        frontaccountingFcgiConfig.deployService domain, service, config
        frontaccountingFromArchive.deployService domain, service
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
                packages: frontaccountingPackages,
                system: systemName,
                this, threads)()
    }

    @Override
    ContextProperties getFrontaccountingProperties() {
        frontaccountingPropertiesProvider.get()
    }

    @Override
    String getServiceName() {
        Ubuntu_12_04_Apache_FrontaccountingConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_Apache_FrontaccountingConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        frontaccountingFcgiConfig.setScript script
        frontaccountingFromArchive.setScript this
        frontaccountingBackup.setScript this
    }
}
