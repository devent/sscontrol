/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheFcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.apache_2_2.FcgiPhpldapadminConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.phpldapadmin.PhpldapadminService
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu 10.04 phpLDAPAdmin.</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuConfig extends FcgiPhpldapadminConfig implements ServiceConfig {

    public static final String NAME = "phpldapadmin"

    @Inject
    UbuntuConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UbuntuPropertiesProvider phpldapadminPropertiesProvider

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
        installPackages()
        super.deployService domain, service, config
        phpldapadminConfig.deployService domain, service, config
        createDomainConfig domain, service, config
    }

    /**
     * Installs the <i>phpLDAPAdmin</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: script.installCommand, packages: adminPackages,
                this, threads)()
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
     * Returns the list of needed <i>phpLDAPAdmin</i> packages.
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
        phpldapadminPropertiesProvider.get()
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script);
        this.phpldapadminConfig.script = script
        this.phpldapadminTemplates = templatesFactory.create "Ubuntu_10_04_Phpldapadmin"
        this.phpldapadminConfigTemplate = phpldapadminTemplates.getResource "config"
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
