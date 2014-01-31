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
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.apache_2_2

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux.PhpldapadminConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Fcgi phpLDAPAdmin.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class FcgiPhpldapadminConfig extends PhpldapadminConfig {

    @Inject
    FcgiConfig fcgiConfig

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.deployConfig domain
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        fcgiConfig.deployService domain, service, config
    }
}
