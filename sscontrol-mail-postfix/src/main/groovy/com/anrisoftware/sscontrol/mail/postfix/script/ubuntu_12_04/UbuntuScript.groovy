/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BasePostfixScript

/**
 * Postfix/Ubuntu 12.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScript extends BasePostfixScript {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Override
    def run() {
        super.run()
        restartServices()
    }

    @Override
    def runDistributionSpecific() {
        installPackages()
    }

    @Override
    String getProfileName() {
        Ubuntu_12_04_ScriptFactory.PROFILE_NAME
    }

    /**
     * @see #ubuntuProperties
     */
    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
