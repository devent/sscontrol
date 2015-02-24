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
package com.anrisoftware.sscontrol.httpd.apache.authdb.apache_2_2

import static org.apache.commons.io.FileUtils.writeLines
import static org.apache.commons.lang3.StringUtils.split

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.BasicAuth
import com.anrisoftware.sscontrol.httpd.apache.authdb.core.DbDriverConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Auth-Database</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AuthDbConfig extends BasicAuth {

    /**
     * Authentication service name.
     */
    public static final String SERVICE_NAME = "auth-db"

    @Inject
    private AuthDbConfigLogger log;

    @Inject
    private Map<String, DbDriverConfig> authDrivers

    @Override
    void deployService(Domain domain, WebService service, List config) {
        def authDriver = authDrivers["${getProfile()}.${service.database.driver}"]
        log.checkAuthDriver this, authDriver, service.database.driver
        authDriver.deployService domain, service, config
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        def authDriver = authDrivers["${getProfile()}.${service.database.driver}"]
        log.checkAuthDriver this, authDriver, service.database.driver
        authDriver.deployDomain domain, refDomain, service, config
    }

    /**
     * Returns authentication service name.
     *
     * @return the service {@link String} name.
     */
    String getServiceName() {
        SERVICE_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script);
        authDrivers.each { key, value ->
            value.setScript this
        }
    }
}
