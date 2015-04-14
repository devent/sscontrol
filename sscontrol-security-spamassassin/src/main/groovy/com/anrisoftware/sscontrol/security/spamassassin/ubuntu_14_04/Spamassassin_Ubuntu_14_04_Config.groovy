/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin.ubuntu_14_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.security.spamassassin.ubuntu.Spamassassin_3_Ubuntu_Config

/**
 * <i>Spamassassin Ubuntu 14.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Spamassassin_Ubuntu_14_04_Config extends Spamassassin_3_Ubuntu_Config {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    ContextProperties getSpamassassinProperties() {
        ubuntuPropertiesProvider.get()
    }

    String getServiceName() {
        Spamassassin_Ubuntu_14_04_ConfigFactory.SERVICE_NAME
    }

    String getProfile() {
        Spamassassin_Ubuntu_14_04_ConfigFactory.PROFILE_NAME
    }
}
