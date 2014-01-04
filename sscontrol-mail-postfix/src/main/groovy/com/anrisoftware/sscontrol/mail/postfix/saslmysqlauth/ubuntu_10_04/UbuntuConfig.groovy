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
package com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.ubuntu_10_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux.BaseSaslMysqlAuth
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.Ubuntu_10_04_ScriptFactory

/**
 * SASL/authentication Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends BaseSaslMysqlAuth {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Override
    public void deployAuth() {
        installPackages saslPackages
        super.deployAuth()
    }

    /**
     * Returns the list of packages to install, for
     * example {@code "libsasl2-modules, libsasl2-modules-sql, libgsasl7, libauthen-sasl-cyrus-perl, sasl2-bin, libpam-mysql".}
     *
     * <ul>
     * <li>profile property {@code "sasl_packages"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getSaslPackages() {
        profileListProperty "sasl_packages", authProperties
    }

    @Override
    String getProfile() {
        Ubuntu_10_04_ScriptFactory.PROFILE_NAME
    }

    @Override
    ContextProperties getAuthProperties() {
        ubuntuProperties.get()
    }
}