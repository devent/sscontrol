/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.apache_2_2

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fcgi.FcgiConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>php-fcgi phpMyAdmin</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class FcgiPhpmyadminConfig {

    private LinuxScript script

    @Inject
    FcgiConfig fcgiConfig

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, java.util.List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.deployConfig domain
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, java.util.List)
     */
    void deployService(Domain domain, WebService service, List config) {
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        fcgiConfig.deployService domain, service, config
    }

    /**
     * Links PHP configurations to the domain directory.
     */
    void linkPhpconf(Domain domain) {
        fcgiConfig.linkPhpconf domain
    }

    /**
     * Returns the default <i>phpMyAdmin</i> properties.
     */
    abstract ContextProperties getMyadminProperties()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
        fcgiConfig.script = script
    }

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
