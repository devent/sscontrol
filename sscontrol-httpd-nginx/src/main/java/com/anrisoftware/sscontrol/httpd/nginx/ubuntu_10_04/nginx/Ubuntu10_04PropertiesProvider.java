/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the default Nginx Ubuntu 10.04 properties from
 * {@code /nginx_ubuntu_10_04.properties}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Ubuntu10_04PropertiesProvider extends
        AbstractContextPropertiesProvider {

    private static final URL RESOURCE = Ubuntu10_04PropertiesProvider.class
            .getResource("/nginx_ubuntu_10_04.properties");

    Ubuntu10_04PropertiesProvider() {
        super(Ubuntu10_04PropertiesProvider.class, RESOURCE);
    }

}
