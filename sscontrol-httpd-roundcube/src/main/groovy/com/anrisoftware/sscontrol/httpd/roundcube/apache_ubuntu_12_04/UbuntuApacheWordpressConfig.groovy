/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.apache_ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.ubuntu_12_04.Ubuntu_12_04_Config

/**
 * <i>Piwik</i> configuration for <i>Apache Ubuntu 12.04</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuApacheWordpressConfig extends Ubuntu_12_04_Config implements ServiceConfig {

    @Inject
    UbuntuFcgiWordpressConfig fcgiConfig

    @Inject
    UbuntuWordpressBackup wordpressBackup

    @Inject
    UbuntuWordpressFromArchiveConfig fromArchiveConfig

    @Inject
    TemplatesFactory templatesFactory

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultForce service
        setupDefaultMultisite service
        fcgiConfig.deployService domain, service, config
        wordpressBackup.backupService domain, service
        installPackages()
        enableMods wordpressMods
        fromArchiveConfig.downloadArchive domain, service
        deployMainConfig domain, service
        deployDatabaseConfig domain, service
        deployKeysConfig domain, service
        deployLanguageConfig domain, service
        deploySecureLoginConfig domain, service
        deployDebugConfig domain, service
        deployMultisiteConfig domain, service
        deployCacheConfig domain, service
        deployMainConfigEnding domain, service
        fromArchiveConfig.createDirectories domain, service
        deployThemes domain, service
        deployPlugins domain, service
        deployCache domain, service
        fromArchiveConfig.setupPermissions domain, service
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
        wordpressBackup.setScript this
        fromArchiveConfig.setScript this
    }
}
