/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.roundcube_1_apache_ubuntu_12_04;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * <i>Ubuntu 12.04 Roundcube</i> archive properties provider from
 * {@code "/roundcube_1_fromarchive_ubuntu_12_04.properties".}
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
@Singleton
class RoundcubeArchivePropertiesProvider extends AbstractContextPropertiesProvider {

    private static final URL URL = RoundcubeArchivePropertiesProvider.class
            .getResource("/roundcube_1_fromarchive_ubuntu_12_04.properties");

    RoundcubeArchivePropertiesProvider() {
        super(RoundcubeArchivePropertiesProvider.class, URL);
    }
}
