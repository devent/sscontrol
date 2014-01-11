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
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_10_04

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.api.ServiceConfig
import com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu.UbuntuWordpressConfig
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService

/**
 * Ubuntu 10.04 Wordpress.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends UbuntuWordpressConfig implements ServiceConfig {

    @Inject
    private UbuntuConfigLogger log

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Inject
    private UbuntuFcgiWordpressConfig fcgiConfig

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.deployDomain domain, refDomain, service, config
        super.deployDomain domain, refDomain, service, config
        createDomainConfig domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        fcgiConfig.deployService domain, service, config
        super.deployService domain, service, config
        createDomainConfig domain, null, service, config
    }

    void createDomainConfig(Domain domain, Domain refDomain, WebService service, List config) {
        def serviceAliasDir = serviceAliasDir service, domain, refDomain
        def serviceDir = serviceDir domain, refDomain
        def configStr = wordpressConfigTemplate.getText(
                true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "config", this,
                "serviceAliasDir", serviceAliasDir,
                "serviceDir", serviceDir)
        config << configStr
    }
    /**
     * @see FcgiConfig#getScriptsSubdirectory()
     */
    String getScriptsSubdirectory() {
        fcgiConfig.scriptsSubdirectory
    }

    /**
     * @see FcgiConfig#getScriptStarterFileName()
     */
    String getScriptStarterFileName() {
        fcgiConfig.scriptStarterFileName
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript script
        fcgiConfig.setScript script
    }

    @Override
    String getProfile() {
        PROFILE
    }

    @Override
    ContextProperties getWordpressProperties() {
        ubuntuPropertiesProvider.get()
    }
}