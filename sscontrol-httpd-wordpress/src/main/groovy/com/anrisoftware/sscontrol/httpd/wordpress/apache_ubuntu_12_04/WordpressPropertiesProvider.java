/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * <i>Wordpress 4 Ubuntu 12.04</i> properties provider from
 * {@code "/apache_wordpress_4_ubuntu_12_04.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
@Singleton
class WordpressPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = WordpressPropertiesProvider.class
            .getResource("/apache_wordpress_4_ubuntu_12_04.properties");

    WordpressPropertiesProvider() {
        super(WordpressPropertiesProvider.class, RESOURCE);
    }

}
