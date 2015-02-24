/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authdb.ubuntu_12_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * <i>Auth-Database Mysql Ubuntu 12.04</i> properties provider from
 * {@code "/apache_authdb_mysql_ubuntu_12_04.properties"}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class UbuntuAuthMysqlPropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL RESOURCE = UbuntuAuthMysqlPropertiesProvider.class
            .getResource("/apache_authdb_mysql_ubuntu_12_04.properties");

    UbuntuAuthMysqlPropertiesProvider() {
        super(UbuntuAuthMysqlPropertiesProvider.class, RESOURCE);
    }

}
