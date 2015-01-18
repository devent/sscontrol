/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.linux.PhpmyadminConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory;
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Ubuntu phpMyAdmin</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UbuntuPhpmyadminConfig extends PhpmyadminConfig {

    @Inject
    UbuntuPhpmyadminConfigLogger logg

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    TemplateResource phpmyadminConfigTemplate

    TemplateResource phpmyadminCommandsTemplate

    @Inject
    final void TemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "UbuntuPhpmyadmin"
        this.phpmyadminConfigTemplate = templates.getResource "config"
        this.phpmyadminCommandsTemplate = templates.getResource "commands"
    }

    /**
     * Setups the default properties of the service.
     *
     * @param service
     *            the {@link PhpmyadminService} service.
     */
    void setupDefaults(PhpmyadminService service) {
        if (service.serverPort == null) {
            service.server service.server, port: defaultServerPort
        }
    }

    /**
     * Installs the <i>phpMyAdmin</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: phpmyadminPackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Imports the <i>phpMyAdmin</i> tables.
     *
     * @param service
     *            the {@link PhpmyadminService} service.
     */
    void importTables(PhpmyadminService service) {
        def task = scriptExecFactory.create(
                log: log,
                zcatCommand: script.zcatCommand,
                mysqlCommand: mysqlCommand,
                controlUser: service.controlUser,
                controlPassword: service.controlPassword,
                controlDatabase: service.controlDatabase,
                script: databaseScriptFile,
                this, threads,
                phpmyadminCommandsTemplate, "importTables")()
        logg.importTables script, task, databaseScriptFile
    }

    /**
     * Reconfigure <i>phpMyAdmin</i>.
     */
    void reconfigureService() {
        def task = scriptExecFactory.create(
                log: log,
                command: reconfigureCommand,
                this, threads,
                phpmyadminCommandsTemplate, "reconfigure")()
        logg.reconfigureService script, task
    }

    /**
     * Deploys the <i>phpMyAdmin</i> configuration.
     *
     * @param service
     *            the {@link PhpmyadminService} service.
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
        def replace = phpmyadminConfigTemplate.getText(true, "configDbpassword", "password", service.controlPassword)
        new TokenTemplate(search, replace)
    }

    def configDbserver(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbserver_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbserver", "server", service.server)
        new TokenTemplate(search, replace)
    }

    def configDbport(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbport_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbport", "port", service.serverPort)
        new TokenTemplate(search, replace)
    }

    def configDbname(PhpmyadminService service) {
        def search = phpmyadminConfigTemplate.getText(true, "configDbname_search")
        def replace = phpmyadminConfigTemplate.getText(true, "configDbname", "database", service.controlDatabase)
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
     * Default server port, for example {@code 3306}.
     *
     * <ul>
     * <li>profile property {@code "phpmyadmin_default_server_port"}</li>
     * </ul>
     *
     * @see #getMyadminProperties()
     */
    int getDefaultServerPort() {
        profileNumberProperty "phpmyadmin_default_server_port", myadminProperties
    }
}
