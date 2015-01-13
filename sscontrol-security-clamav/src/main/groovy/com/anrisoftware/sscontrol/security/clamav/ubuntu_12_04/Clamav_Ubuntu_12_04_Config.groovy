/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav.ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.security.clamav.ubuntu.Clamav_0_98_Ubuntu_Config

/**
 * <i>ClamAV Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Clamav_Ubuntu_12_04_Config extends Clamav_0_98_Ubuntu_Config {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    ContextProperties getClamavProperties() {
        ubuntuPropertiesProvider.get()
    }

    String getServiceName() {
        Clamav_Ubuntu_12_04_ConfigFactory.SERVICE_NAME
    }

    String getProfile() {
        Clamav_Ubuntu_12_04_ConfigFactory.PROFILE_NAME
    }
}
