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
package com.anrisoftware.sscontrol.httpd.roundcube.ubuntu_12_04

import static com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04.Ubuntu_12_04_ApacheWordpressConfigFactory.PROFILE_NAME
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04.UbuntuPropertiesProvider
import com.anrisoftware.sscontrol.httpd.wordpress.core.Wordpress_3_Config
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu 12.04</i> <i>Wordpress</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_12_04_Config extends Wordpress_3_Config {

    @Inject
    private Ubuntu_12_04_ConfigLogger logg

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    /**
     * Installs the <i>Wordpress</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: script.installCommand, packages: wordpressPackages,
                this, threads)()
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript script
    }

    @Override
    String getProfile() {
        PROFILE_NAME
    }

    @Override
    ContextProperties getWordpressProperties() {
        ubuntuPropertiesProvider.get()
    }
}
