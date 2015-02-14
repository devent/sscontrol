/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04;

import java.net.URL;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides <i>Ubuntu 12.04 Nginx Thin Redmine 2.6</i> from archive properties
 * from {@code /redmine_2_6_fromarchive_ubuntu_12_04.properties}
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class RedmineFromArchivePropertiesProvider extends
        AbstractContextPropertiesProvider {

    private static final URL URL = RedmineFromArchivePropertiesProvider.class
            .getResource("/redmine_2_6_fromarchive_ubuntu_12_04.properties");

    RedmineFromArchivePropertiesProvider() {
        super(RedmineFromArchivePropertiesProvider.class, URL);
    }
}
