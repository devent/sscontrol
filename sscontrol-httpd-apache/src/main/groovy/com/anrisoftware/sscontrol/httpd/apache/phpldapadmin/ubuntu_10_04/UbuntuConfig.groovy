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
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.ubuntu_10_04

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.apache_2_2.FcgiPhpldapadminConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.phpldapadmin.PhpldapadminService;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Ubuntu 10.04 phpLDAPAdmin.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends FcgiPhpldapadminConfig implements ServiceConfig {

    public static final String NAME = "phpldapadmin"

    @Inject
    UbuntuConfigLogger log

    @Inject
    UbuntuPropertiesProvider phpldapadminProperties

    @Inject
    UbuntuFromArchiveConfig phpldapadminConfig

    Templates phpldapadminTemplates

    TemplateResource phpldapadminConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        super.deployDomain domain, refDomain, service, config
        phpldapadminConfig.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        installPackages adminPackages
        super.deployService domain, service, config
        phpldapadminConfig.deployService domain, service, config
        createDomainConfig domain, service, config
    }

    /**
     * Creates the Apache/domain configuration.
     */
    void createDomainConfig(Domain domain, PhpldapadminService service, List serviceConfig) {
        def serviceDir = phpldapadminConfig.adminLinkedConfigurationDir(domain)
        def config = phpldapadminConfigTemplate.getText(true, "domainConfig",
                "domain", domain,
                "service", service,
                "serviceDir", serviceDir,
                "properties", script,
                "fcgiProperties", fcgiConfig)
        serviceConfig << config
    }

    /**
     * Returns the list of needed phpldapadmin/packages.
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_packages"}</li>
     * </ul>
     *
     * @see #getPhpldapadminProperties()
     */
    List getAdminPackages() {
        profileListProperty "phpldapadmin_packages", phpldapadminProperties
    }

    @Override
    ContextProperties getPhpldapadminProperties() {
        phpldapadminProperties.get()
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script);
        phpldapadminConfig.script = script
        phpldapadminTemplates = templatesFactory.create "Ubuntu_10_04_Phpldapadmin"
        phpldapadminConfigTemplate = phpldapadminTemplates.getResource "config"
    }

    @Override
    String getProfile() {
        PROFILE
    }

    @Override
    String getServiceName() {
        NAME
    }
}
