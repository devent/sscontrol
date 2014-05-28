/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.authfile.ubuntu_10_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default auth/file Ubuntu 10.04 properties from
 * {@code "/apache_authfile_ubuntu_10_04.properties"}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class UbuntuPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = UbuntuPropertiesProvider.class
            .getResource("/apache_authfile_ubuntu_10_04.properties");

    UbuntuPropertiesProvider() {
        super(UbuntuPropertiesProvider.class, RESOURCE);
    }

}
