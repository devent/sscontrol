/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-thin.
 *
 * sscontrol-httpd-thin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-thin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-thin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.thin.ubuntu_12_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Returns <i>Thin</i> properties for <i>Ubuntu 12.04</i> from
 * {@code /thin_ubuntu_12_04.properties}
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class Ubuntu_12_04_ThinPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL URL = Ubuntu_12_04_ThinPropertiesProvider.class
            .getResource("/thin_ubuntu_12_04.properties");

    Ubuntu_12_04_ThinPropertiesProvider() {
        super(Ubuntu_12_04_ThinPropertiesProvider.class, URL);
    }
}
