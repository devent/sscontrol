/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.apache_2_2

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fcgi.FcgiConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>php-fcgi ownCloud</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FcgiOwncloudConfig {

    @Inject
    FcgiConfig fcgiConfig

    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.deployConfig domain
    }

    void deployService(Domain domain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        fcgiConfig.deployService domain, service, config
    }

    /**
     * @see FcgiConfig#getScriptsSubdirectory()
     */
    String getScriptsSubdirectory() {
        fcgiConfig.getScriptsSubdirectory()
    }

    /**
     * @see FcgiConfig#getScriptStarterFileName()
     */
    String getScriptStarterFileName() {
        fcgiConfig.getScriptStarterFileName()
    }

    /**
     * Links PHP configurations to the domain directory.
     */
    void linkPhpconf(Domain domain) {
        fcgiConfig.linkPhpconf domain
    }
}
