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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpmyadmin

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.PROFILE

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.FcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ServiceConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.phpmyadmin.BasePhpmyadminConfig
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminService
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures the phpmyadmin/service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PhpmyadminConfig extends BasePhpmyadminConfig implements ServiceConfig {

    public static final String NAME = "phpmyadmin"

    @Inject
    PhpmyadminConfigLogger log

    Templates phpmyadminTemplates

    TemplateResource phpmyadminConfigTemplate

    TemplateResource phpmyadminCommandsTemplate

    @Inject
    FcgiConfig fcgiConfig

    @Override
    String getProfile() {
        PROFILE
    }

    @Override
    String getServiceName() {
        NAME
    }

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
        phpmyadminTemplates = templatesFactory.create "Apache_2_2_Phpmyadmin"
        phpmyadminConfigTemplate = phpmyadminTemplates.getResource "config"
        phpmyadminCommandsTemplate = phpmyadminTemplates.getResource "commands"
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.deployConfig domain
        createDomainConfig domain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        fcgiConfig.script = script
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        installPackages phpmyadminPackages
        deployConfiguration service
        reconfigureService()
        changeOwnerConfiguration domain
        importTables service
        createDomainConfig domain, service, config
    }

    void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
        def config = phpmyadminConfigTemplate.getText(true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "fcgiProperties", fcgiConfig)
        serviceConfig << config
    }

    void importTables(PhpmyadminService service) {
        def worker = scriptCommandFactory.create(
                phpmyadminCommandsTemplate, "importTables",
                "zcatCommand", script.zcatCommand,
                "mysqlCommand", mysqlCommand,
                "admin", service.adminUser,
                "user", service.controlUser,
                "script", databaseScriptFile)()
        log.importTables script, worker
    }

    /**
     * Deploys the phpmyadmin configuration.
     */
    void deployConfiguration(PhpmyadminService service) {
        deployConfiguration configurationTokens(), phpmyadminConfiguration, phpmyadminConfigurations(service), configurationFile
    }

    /**
     * Returns the phpmyadmin configurations.
     */
    List phpmyadminConfigurations(PhpmyadminService service) {
        [
            configDbuser(service),
            configDbpassword(service),
            configDbserver(service),
            configDbport(service),
            configDbname(service),
            configDbadmin(service)
        ]
    }

    def configDbuser(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbuser_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbuser", "user", service.controlUser)
        new TokenTemplate(search, replace)
    }

    def configDbpassword(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbpassword_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbpassword", "user", service.controlUser)
        new TokenTemplate(search, replace)
    }

    def configDbserver(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbserver_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbserver", "server", service.server)
        new TokenTemplate(search, replace)
    }

    def configDbport(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbport_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbport", "server", service.server)
        new TokenTemplate(search, replace)
    }

    def configDbname(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbname_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbname", "user", service.controlUser)
        new TokenTemplate(search, replace)
    }

    def configDbadmin(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbadmin_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbadmin", "admin", service.adminUser)
        new TokenTemplate(search, replace)
    }

    def changeOwnerConfiguration(Domain domain) {
        def user = domain.domainUser
        changeOwner owner: "root", ownerGroup: user.group, files: [
            localBlowfishFile,
            localConfigFile,
            localDatabaseConfigFile
        ]
    }

    def reconfigureService() {
        def worker = scriptCommandFactory.create(
                phpmyadminCommandsTemplate, "reconfigure",
                "command", reconfigureCommand)()
        log.reconfigureService script, worker
    }
}
