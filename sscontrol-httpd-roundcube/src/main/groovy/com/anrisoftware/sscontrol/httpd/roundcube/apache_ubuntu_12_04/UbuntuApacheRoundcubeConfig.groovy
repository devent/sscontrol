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
package com.anrisoftware.sscontrol.httpd.roundcube.apache_ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.ubuntu_12_04.Ubuntu_12_04_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Roundcube Apache Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuApacheRoundcubeConfig extends Ubuntu_12_04_Config implements ServiceConfig {

    @Inject
    UbuntuFcgiRoundcubeConfig fcgiConfig

    @Inject
    UbuntuRoundcubeFromArchiveConfig fromArchiveConfig

    @Inject
    UbuntuRoundcubeBackup roundcubeBackup

    @Inject
    UbuntuRoundcubePhp_5_Config roundcubePhpConfig

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultDebugLevels service
        setupDefaultDatabase service
        setupDefaultSmtp service
        setupDefaultImap service
        fcgiConfig.deployService domain, service, config
        roundcubePhpConfig.deployPhpini domain
        roundcubeBackup.backupService domain, service
        installPackages()
        installDatabasePackages service
        enableMods roundcubeMods
        fromArchiveConfig.downloadArchive domain, service
        fromArchiveConfig.setupPermissions domain, service
        setupDatabase domain, service
        deployImapConfig domain, service
        deployDatabaseConfig domain, service
        deploySmtpConfig domain, service
        deployDebugConfig domain, service
    }

    /**
     * @see FcgiConfig#getScriptsSubdirectory()
     */
    String getScriptsSubdirectory() {
        fcgiConfig.scriptsSubdirectory
    }

    /**
     * @see FcgiConfig#getScriptStarterFileName()
     */
    String getScriptStarterFileName() {
        fcgiConfig.scriptStarterFileName
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        fcgiConfig.setScript this
        fromArchiveConfig.setScript this
        roundcubeBackup.setScript this
        roundcubePhpConfig.setScript this
    }
}
