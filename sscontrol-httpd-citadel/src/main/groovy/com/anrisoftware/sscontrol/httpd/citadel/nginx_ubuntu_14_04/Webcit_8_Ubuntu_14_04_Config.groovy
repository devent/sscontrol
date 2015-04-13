/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.citadel.webcit_ubuntu.Webcit_8_Ubuntu_Config

/**
 * <i>Webcit 8 Ubuntu 14.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Webcit_8_Ubuntu_14_04_Config extends Webcit_8_Ubuntu_Config {

    @Inject
    Citadel_ubuntu_14_04_PropertyProvider citadelPropertyProvider

    ContextProperties getCitadelProperties() {
        citadelPropertyProvider.get()
    }

    String getServiceName() {
        Citadel_Nginx_Ubuntu_14_04_ConfigFactory.WEB_NAME
    }

    String getProfile() {
        Citadel_Nginx_Ubuntu_14_04_ConfigFactory.PROFILE_NAME
    }
}
