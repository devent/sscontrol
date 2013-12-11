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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.FcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ServiceConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.roundcube.BaseRoundcube_0_9_Config
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminService
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures the roundcube service for Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RoundcubeConfig extends BaseRoundcube_0_9_Config implements ServiceConfig {

    @Inject
    private RoundcubeConfigLogger log

    @Inject
    private FcgiConfig fcgiConfig

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
    }

    @Override
    String getProfile() {
        PROFILE
    }

    @Override
    void deployService(Domain domain, WebService service, List serviceConfig) {
        fcgiConfig.script = script
        installPackages roundcubePackages
        downloadArchive()
        fcgiConfig.enableFcgi()
        fcgiConfig.deployConfig domain
        deployDatabaseConfig service
    }

    void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
        def config = roundcubeConfigTemplate.getText(true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "fcgiProperties", fcgiConfig)
        serviceConfig << config
    }

    void downloadArchive() {
        def name = new File(roundcubeArchive).name
        def dest = new File(tmpDirectory, "roundcube-$name")
        def type = archiveType file: dest
        if (!roundcubeDir.isDirectory()) {
            roundcubeDir.mkdirs()
        }
        copyURLToFile roundcubeArchive.toURL(), dest
        unpack file: dest, type: type, output: roundcubeDir, override: true
        log.downloadArchive script, roundcubeArchive
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
        def search = roundcubeConfigTemplate.getText(true, "configDbuser_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbuser", "user", service.controlUser)
        new TokenTemplate(search, replace)
    }

    def configDbpassword(PhpmyadminService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDbpassword_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbpassword", "user", service.controlUser)
        new TokenTemplate(search, replace)
    }

    def configDbserver(PhpmyadminService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDbserver_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbserver", "server", service.server)
        new TokenTemplate(search, replace)
    }

    def configDbport(PhpmyadminService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDbport_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbport", "server", service.server)
        new TokenTemplate(search, replace)
    }

    def configDbname(PhpmyadminService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDbname_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbname", "user", service.controlUser)
        new TokenTemplate(search, replace)
    }

    def configDbadmin(PhpmyadminService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDbadmin_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbadmin", "admin", service.adminUser)
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
                roundcubeCommandsTemplate, "reconfigure",
                "command", reconfigureCommand)()
        log.reconfigureService script, worker
    }
}
