/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_Config
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu 12.04 Wordpress</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuWordpressConfig extends Wordpress_3_Config implements ServiceConfig {

    @Inject
    WordpressPropertiesProvider wordpressPropertiesProvider

    @Inject
    UbuntuWordpressFromArchive wordpressFromArchive

    @Inject
    UbuntuApacheWordpressConfig apacheConfig

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    WordpressBackup wordpressBackup

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultForce service
        setupDefaultMultisite service
        setupDefaultDatabase service
        setupDefaultDebug service
        apacheConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultForce service
        setupDefaultMultisite service
        setupDefaultDatabase service
        setupDefaultDebug service
        wordpressBackup.backupService domain, service
        apacheConfig.deployService domain, service, config
        installPackages()
        wordpressFromArchive.deployService domain, service
        deployMainConfig domain, service
        deployDatabaseConfig domain, service
        deployKeysConfig domain, service
        deployLanguageConfig domain, service
        deploySecureLoginConfig domain, service
        deployDebugConfig domain, service
        deployMultisiteConfig domain, service
        deployCacheConfig domain, service
        deployMainConfigEnding domain, service
        createDirectories domain, service
        deployThemes domain, service
        deployPlugins domain, service
        deployCache domain, service
        setupPermissions domain, service
    }

    /**
     * Installs the <i>Wordpress</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: wordpressPackages,
                system: systemName,
                this, threads)()
    }

    @Override
    ContextProperties getWordpressProperties() {
        wordpressPropertiesProvider.get()
    }

    @Override
    String getServiceName() {
        Ubuntu_12_04_ApacheWordpressConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ApacheWordpressConfigFactory.PROFILE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script)
        apacheConfig.setScript this
        wordpressFromArchive.setScript this
        wordpressBackup.setScript this
    }
}
