/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.ubuntu_12_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.database.mysql.mysql_5_1.Mysql51Script
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>MySQL Ubuntu 12.04</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuScript extends Mysql51Script {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Override
    void beforeConfiguration() {
        installPackages()
    }

    /**
     * Installs the <i>MySQL</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: packages,
                system: systemName,
                this, threads)()
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
