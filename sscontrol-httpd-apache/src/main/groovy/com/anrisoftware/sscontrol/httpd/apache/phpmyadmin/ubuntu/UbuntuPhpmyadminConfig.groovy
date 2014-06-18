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
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheFcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.apache_2_2.FcgiPhpmyadminConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminService
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * Ubuntu fcgi phpMyAdmin.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UbuntuPhpmyadminConfig extends FcgiPhpmyadminConfig {

    @Inject
    UbuntuPhpmyadminConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    Templates phpmyadminTemplates

    TemplateResource phpmyadminConfigTemplate

    TemplateResource phpmyadminCommandsTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        super.deployDomain domain, refDomain, service, config
        createDomainConfig domain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        super.deployService domain, service, config
        installPackages()
        fcgiConfig.linkPhpconf domain
        deployConfiguration service
        reconfigureService()
        changeOwnerConfiguration domain
        importTables service
        createDomainConfig domain, service, config
    }

    /**
     * Installs the <i>phpLDAPAdmin</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: script.installCommand, packages: phpmyadminPackages,
                this, threads)()
    }

    /**
     * Returns the list of needed packages for phpMyAdmin.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_packages"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    List getPhpmyadminPackages() {
        profileListProperty "phpmyadmin_packages", myadminProperties
    }

    void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
        def config = phpmyadminConfigTemplate.getText(true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "fcgiProperties", fcgiConfig)
        logg.domainConfigCreated script, domain, config
        serviceConfig << config
    }

    /**
     * Imports the <i>phpMyAdmin</i> tables.
     */
    void importTables(PhpmyadminService service) {
        def task = scriptExecFactory.create(
                log: log,
                zcatCommand: script.zcatCommand,
                mysqlCommand: mysqlCommand,
                admin: service.adminUser,
                user: service.controlUser,
                script: databaseScriptFile,
                this, threads, phpmyadminCommandsTemplate, "importTables")()
        logg.importTables script, task, databaseScriptFile
    }

    /**
     * phpMyAdmin database script file, for
     * example {@code "/usr/share/doc/phpmyadmin/examples/create_tables.sql.gz"}.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_database_script_file"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    File getDatabaseScriptFile() {
        profileDirProperty "phpmyadmin_database_script_file", myadminProperties
    }

    /**
     * Returns the MySQL client command, for example {@code /usr/bin/mysql}.
     *
     * <ul>
     * <li>profile property {@code "mysql_command"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    String getMysqlCommand() {
        profileProperty "mysql_command", myadminProperties
    }

    /**
     * Deploys the phpMyAdmin configuration.
     */
    void deployConfiguration(PhpmyadminService service) {
        def current = currentConfiguration configurationFile
        deployConfiguration configurationTokens(), current, phpmyadminConfigurations(service), configurationFile
    }

    /**
     * phpMyAdmin configuration file, for
     * example {@code "/etc/dbconfig-common/phpmyadmin.conf"}.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_configuration_file"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    File getConfigurationFile() {
        profileDirProperty "phpmyadmin_configuration_file", myadminProperties
    }

    /**
     * Returns the phpMyAdmin configurations.
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
        changeFileOwnerFactory.create(
                log: log,
                owner: "root",
                ownerGroup: user.group,
                files: [
                    localBlowfishFile,
                    localConfigFile,
                    localDatabaseConfigFile
                ],
                command: script.chownCommand,
                this, threads)()
    }

    /**
     * phpMyAdmin local configuration file, for
     * example {@code "/var/lib/phpmyadmin/config.inc.php"}.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_local_config_file"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    File getLocalConfigFile() {
        profileDirProperty "phpmyadmin_local_config_file", myadminProperties
    }

    /**
     * phpMyAdmin local blowfish secret file, for
     * example {@code "/var/lib/phpmyadmin/blowfish_secret.inc.php"}.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_local_blowfish_secret_file"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    File getLocalBlowfishFile() {
        profileDirProperty "phpmyadmin_local_blowfish_secret_file", myadminProperties
    }

    /**
     * phpMyAdmin local database configuration file, for
     * example {@code "/etc/phpmyadmin/config-db.php"}.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_local_database_config_file"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    File getLocalDatabaseConfigFile() {
        profileDirProperty "phpmyadmin_local_database_config_file", myadminProperties
    }

    /**
     * Reconfigure <i>phpMyAdmin</i>.
     */
    void reconfigureService() {
        def task = scriptExecFactory.create(
                log: log,
                command: reconfigureCommand,
                this, threads, phpmyadminCommandsTemplate, "reconfigure")()
        logg.reconfigureService script, task
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript script
        this.phpmyadminTemplates = templatesFactory.create "UbuntuPhpmyadmin"
        this.phpmyadminConfigTemplate = phpmyadminTemplates.getResource "config"
        this.phpmyadminCommandsTemplate = phpmyadminTemplates.getResource "commands"
    }
}
