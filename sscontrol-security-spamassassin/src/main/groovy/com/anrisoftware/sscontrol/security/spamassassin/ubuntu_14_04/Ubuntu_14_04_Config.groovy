/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.security.service.SecService
import com.anrisoftware.sscontrol.security.service.ServiceConfig
import com.anrisoftware.sscontrol.security.spamassassin.ubuntu.UbuntuConfig

/**
 * <i>Spamassassin Ubuntu 14.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_14_04_Config extends UbuntuConfig implements ServiceConfig {

    @Inject
    Spamassassin_Ubuntu_14_04_Config spamassassinConfig

    @Inject
    UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Override
    void deployService(SecService service) {
        spamassassinConfig.setScript this
        spamassassinConfig.installPackages()
        spamassassinConfig.setupDefaultDebug service
        spamassassinConfig.setupDefaults service
        spamassassinConfig.deploySpamassassinDefaultConfig service
        spamassassinConfig.deploySpamassassinConfig service
        spamassassinConfig.restartSpamassassin service
    }

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
