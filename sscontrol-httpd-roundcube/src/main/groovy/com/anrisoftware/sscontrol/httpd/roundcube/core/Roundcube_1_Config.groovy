/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.core

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.scripts.importdb.ImportDatabaseFactory

/**
 * <i>Roundcube 1.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Roundcube_1_Config extends RoundcubeConfig {

    @Inject
    private Roundcube_1_ConfigLogger log

    @Inject
    Roundcube_1_Permissions roundcubePermissions

    @Inject
    ImportDatabaseFactory importDatabaseFactory

    TemplateResource imapConfigTemplate

    TemplateResource databaseConfigTemplate

    TemplateResource smtpConfigTemplate

    TemplateResource debugConfigTemplate

    TemplateResource miscConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Roundcube_1"
        this.imapConfigTemplate = templates.getResource "imapconfig"
        this.databaseConfigTemplate = templates.getResource "databaseconfig"
        this.smtpConfigTemplate = templates.getResource "smtpconfig"
        this.debugConfigTemplate = templates.getResource "debugconfig"
        this.miscConfigTemplate = templates.getResource "miscconfig"
    }

    /**
     * Sets the owner and permissions of the <i>Roundcube</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupPermissions(Domain domain, RoundcubeService service) {
        roundcubePermissions.setupPermissions domain, service
    }

    /**
     * Deploys the misc configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void deployMiscConfig(Domain domain, RoundcubeService service) {
        def sampleConfig = roundcubeSampleConfigFile domain, service
        def config = roundcubeConfigFile domain, service
        config.isFile() == false ? FileUtils.copyFile(sampleConfig, config) : false
        def current = currentConfiguration config
        def configs = [
            configPlugins(service),
            configDesKey(service),
        ]
        deployConfiguration configurationTokens(), current, configs, config
    }

    def configPlugins(RoundcubeService service) {
        def search = miscConfigTemplate.getText(true, "configPlugins_search")
        def replace = miscConfigTemplate.getText(true, "configPlugins", "plugins", service.plugins)
        new TokenTemplate(search, replace)
    }

    def configDesKey(RoundcubeService service) {
        def search = miscConfigTemplate.getText(true, "configDesKey_search")
        def replace = miscConfigTemplate.getText(true, "configDesKey", "key", redmineDesKey)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the IMAP servers configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void deployImapConfig(Domain domain, RoundcubeService service) {
        def sampleConfig = roundcubeSampleConfigFile domain, service
        def config = roundcubeConfigFile domain, service
        config.isFile() == false ? FileUtils.copyFile(sampleConfig, config) : false
        def current = currentConfiguration config
        def configs = [
            configDefaultImapHost(service),
            configDefaultImapPort(service),
            configImapHosts(service),
            configImapAuthType(service),
            configImapDelimiter(service),
            configImapNsPersonal(service),
            configImapNsOther(service),
            configImapNsShared(service),
        ]
        deployConfiguration configurationTokens(), current, configs, config
    }

    def configDefaultImapHost(RoundcubeService service) {
        if (service.imapServer == null) {
            return []
        }
        def search = imapConfigTemplate.getText(true, "configDefaultImapHost_search")
        def replace = imapConfigTemplate.getText(true, "configDefaultImapHost", "host", service.imapServer)
        new TokenTemplate(search, replace)
    }

    def configDefaultImapPort(RoundcubeService service) {
        if (service.imapPort == null) {
            return []
        }
        def search = imapConfigTemplate.getText(true, "configDefaultImapPort_search")
        def replace = imapConfigTemplate.getText(true, "configDefaultImapPort", "port", service.imapPort)
        new TokenTemplate(search, replace)
    }

    def configImapAuthType(RoundcubeService service) {
        def search = imapConfigTemplate.getText(true, "configImapAuthType_search")
        def replace = imapConfigTemplate.getText(true, "configImapAuthType", "type", roundcubeImapAuthType)
        new TokenTemplate(search, replace)
    }

    def configImapHosts(RoundcubeService service) {
        if (service.imapServer != null) {
            return []
        }
        def search = imapConfigTemplate.getText(true, "configImapHosts_search")
        def replace = imapConfigTemplate.getText(true, "configImapHosts", "hosts", service.imapServers)
        new TokenTemplate(search, replace)
    }

    def configImapDelimiter(RoundcubeService service) {
        def search = imapConfigTemplate.getText(true, "configImapDelimiter_search")
        def replace = imapConfigTemplate.getText(true, "configImapDelimiter", "delimiter", roundcubeImapDelimiter)
        new TokenTemplate(search, replace)
    }

    def configImapNsPersonal(RoundcubeService service) {
        def search = imapConfigTemplate.getText(true, "configImapNsPersonal_search")
        def replace = imapConfigTemplate.getText(true, "configImapNsPersonal", "ns", roundcubeImapNsPersonal)
        new TokenTemplate(search, replace)
    }

    def configImapNsOther(RoundcubeService service) {
        def search = imapConfigTemplate.getText(true, "configImapNsOther_search")
        def replace = imapConfigTemplate.getText(true, "configImapNsOther", "ns", roundcubeImapNsOther)
        new TokenTemplate(search, replace)
    }

    def configImapNsShared(RoundcubeService service) {
        def search = imapConfigTemplate.getText(true, "configImapNsShared_search")
        def replace = imapConfigTemplate.getText(true, "configImapNsShared", "ns", roundcubeImapNsShared)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the database configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void deployDatabaseConfig(Domain domain, RoundcubeService service) {
        def sampleConfig = roundcubeSampleConfigFile domain, service
        def config = roundcubeConfigFile domain, service
        config.isFile() == false ? FileUtils.copyFile(sampleConfig, config) : false
        def current = currentConfiguration config
        def configs = [
            configDatabase(service),
            configDatabasePrefix(service),
        ]
        deployConfiguration configurationTokens(), current, configs, config
    }

    def configDatabase(RoundcubeService service) {
        def search = databaseConfigTemplate.getText(true, "configDatabase_search")
        def replace = databaseConfigTemplate.getText(true, "configDatabase", "database", service.database)
        new TokenTemplate(search, replace)
    }

    def configDatabasePrefix(RoundcubeService service) {
        def search = databaseConfigTemplate.getText(true, "configDatabasePrefix_search")
        def replace = databaseConfigTemplate.getText(true, "configDatabasePrefix", "prefix", roundcubeDatabaseTablePrefix)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the SMTP configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void deploySmtpConfig(Domain domain, RoundcubeService service) {
        def sampleConfig = roundcubeSampleConfigFile domain, service
        def config = roundcubeConfigFile domain, service
        config.isFile() == false ? FileUtils.copyFile(sampleConfig, config) : false
        def current = currentConfiguration config
        def configs = [
            configSmtpServer(service),
            configSmtpPort(service),
            configSmtpUser(service),
            configSmtpPass(service),
            configSmtpAuthType(service),
        ]
        deployConfiguration configurationTokens(), current, configs, config
    }

    def configSmtpServer(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpServer_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpServer", "server", service.mailServer.mail)
        new TokenTemplate(search, replace)
    }

    def configSmtpPort(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpPort_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpPort", "port", smtpServerPort)
        new TokenTemplate(search, replace)
    }

    def configSmtpUser(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpUser_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpUser", "user", service.mailServer.user)
        new TokenTemplate(search, replace)
    }

    def configSmtpPass(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpPass_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpPass", "pass", service.mailServer.password)
        new TokenTemplate(search, replace)
    }

    def configSmtpAuthType(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpAuthType_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpAuthType", "type", smtpServerAuthType)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the debug configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void deployDebugConfig(Domain domain, RoundcubeService service) {
        def sampleConfig = roundcubeSampleConfigFile domain, service
        def config = roundcubeConfigFile domain, service
        config.isFile() == false ? FileUtils.copyFile(sampleConfig, config) : false
        def current = currentConfiguration config
        def configs = [
            configDebugLevel(service),
            configSmtpLog(service),
            configLogLogins(service),
            configLogSession(service),
            configSqlDebug(service),
            configImapDebug(service),
            configLdapDebug(service),
            configSmtpDebug(service),
            configLogDriver(service),
            configLogDateFormat(service),
            configSyslogId(service),
            configSyslogFacility(service),
            configPerUserLogging(service),
        ]
        deployConfiguration configurationTokens(), current, configs, config
    }

    def configDebugLevel(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configDebugLevel_search")
        def replace = debugConfigTemplate.getText(true, "configDebugLevel", "level", service.debugLogging("level").roundcube)
        new TokenTemplate(search, replace)
    }

    def configSmtpLog(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configSmtpLog_search")
        def replace = debugConfigTemplate.getText(true, "configSmtpLog", "enabled", service.debugLogging("level").smtplog > 0)
        new TokenTemplate(search, replace)
    }

    def configLogLogins(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configLogLogins_search")
        def replace = debugConfigTemplate.getText(true, "configLogLogins", "enabled", service.debugLogging("level").logins > 0)
        new TokenTemplate(search, replace)
    }

    def configLogSession(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configLogSession_search")
        def replace = debugConfigTemplate.getText(true, "configLogSession", "enabled", service.debugLogging("level").session > 0)
        new TokenTemplate(search, replace)
    }

    def configSqlDebug(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configSqlDebug_search")
        def replace = debugConfigTemplate.getText(true, "configSqlDebug", "enabled", service.debugLogging("level").sql > 0)
        new TokenTemplate(search, replace)
    }

    def configImapDebug(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configImapDebug_search")
        def replace = debugConfigTemplate.getText(true, "configImapDebug", "enabled", service.debugLogging("level").imap > 0)
        new TokenTemplate(search, replace)
    }

    def configLdapDebug(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configLdapDebug_search")
        def replace = debugConfigTemplate.getText(true, "configLdapDebug", "enabled", service.debugLogging("level").ldap > 0)
        new TokenTemplate(search, replace)
    }

    def configSmtpDebug(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configSmtpDebug_search")
        def replace = debugConfigTemplate.getText(true, "configSmtpDebug", "enabled", service.debugLogging("level").smtp > 0)
        new TokenTemplate(search, replace)
    }

    def configLogDriver(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configLogDriver_search")
        def replace = debugConfigTemplate.getText(true, "configLogDriver", "driver", debugDriver)
        new TokenTemplate(search, replace)
    }

    def configLogDateFormat(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configLogDateFormat_search")
        def replace = debugConfigTemplate.getText(true, "configLogDateFormat", "format", debugDateFormat)
        new TokenTemplate(search, replace)
    }

    def configSyslogId(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configSyslogId_search")
        def replace = debugConfigTemplate.getText(true, "configSyslogId", "id", debugSyslogId)
        new TokenTemplate(search, replace)
    }

    def configSyslogFacility(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configSyslogFacility_search")
        def replace = debugConfigTemplate.getText(true, "configSyslogFacility", "facility", debugSyslogFacility)
        new TokenTemplate(search, replace)
    }

    def configPerUserLogging(RoundcubeService service) {
        def search = debugConfigTemplate.getText(true, "configPerUserLogging_search")
        def replace = debugConfigTemplate.getText(true, "configPerUserLogging", "enabled", debugPerUserLogging)
        new TokenTemplate(search, replace)
    }

    /**
     * Setups the databases tables.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDatabase(Domain domain, RoundcubeService service) {
        def config = roundcubeConfigFile domain, service
        if (config.isFile()) {
            return
        }
        def driver = service.database.driver
        def command = roundcubeDatabaseCommand driver
        def script = roundcubeDatabaseInitialFile driver, domain, service
        importDatabaseFactory.create(
                log: log.log,
                runCommands: runCommands,
                driver: driver,
                command: command,
                user: service.database.user,
                password: service.database.password,
                database: service.database.database,
                script: script,
                this, threads)()
    }

    /**
     * Returns <i>Roundcube</i> configuration file, for
     * example {@code "config/config.inc.php"}. If the path is not absolute
     * then it is assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_config_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeProperties()
     * @see #roundcubeDir(Domain, RoundcubeService)
     */
    File roundcubeConfigFile(Domain domain, RoundcubeService service) {
        def dir = roundcubeDir domain, service
        profileFileProperty "roundcube_config_file", dir, roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> sample configuration file, for
     * example {@code "config/config.inc.php.sample"}. If the path is
     * not absolute then it is assumed to be under the service installation
     * directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_sample_config_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeProperties()
     * @see #roundcubeDir(Domain, RoundcubeService)
     */
    File roundcubeSampleConfigFile(Domain domain, RoundcubeService service) {
        def dir = roundcubeDir domain, service
        def file = profileFileProperty "roundcube_sample_config_file", dir, roundcubeProperties
        log.checkRoundcubeSampleConfigFile this, file
        return file
    }

    /**
     * Returns <i>Roundcube</i> database initial script file, for
     * example {@code "SQL/mysql.initial.sql"}. If the path is
     * not absolute then it is assumed to be under the service installation
     * directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_${driver}_initial_file"}</li>
     * </ul>
     *
     * @param driver
     *            the database {@link String} driver.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeProperties()
     * @see #roundcubeDir(Domain, RoundcubeService)
     */
    File roundcubeDatabaseInitialFile(String driver, Domain domain, RoundcubeService service) {
        def dir = roundcubeDir domain, service
        profileFileProperty "roundcube_${driver}_initial_file", dir, roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> temp directory, for
     * example {@code "temp"}. If the path is
     * not absolute then it is assumed to be under the service installation
     * directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_temp_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeProperties()
     * @see #roundcubeDir(Domain, RoundcubeService)
     */
    File roundcubeTempDirectory(Domain domain, RoundcubeService service) {
        def dir = roundcubeDir domain, service
        profileFileProperty "roundcube_temp_directory", dir, roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> logging directory, for
     * example {@code "logs"}. If the path is
     * not absolute then it is assumed to be under the service installation
     * directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_logs_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeProperties()
     * @see #roundcubeDir(Domain, RoundcubeService)
     */
    File roundcubeLogsDirectory(Domain domain, RoundcubeService service) {
        def dir = roundcubeDir domain, service
        profileFileProperty "roundcube_logs_directory", dir, roundcubeProperties
    }

    /**
     * Returns the IMAP auth type, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_auth_type"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeImapAuthType() {
        def p = profileProperty "roundcube_imap_auth_type", roundcubeProperties
        p.length() == 0 ? null : p
    }

    /**
     * Returns the IMAP delimiter, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_delimiter"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeImapDelimiter() {
        def p = profileProperty "roundcube_imap_delimiter", roundcubeProperties
        p.length() == 0 ? null : p
    }

    /**
     * Returns the IMAP name space, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_ns_personal"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeImapNsPersonal() {
        def p = profileProperty "roundcube_imap_ns_personal", roundcubeProperties
        p.length() == 0 ? null : p
    }

    /**
     * Returns the IMAP name space, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_ns_other"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeImapNsOther() {
        def p = profileProperty "roundcube_imap_ns_other", roundcubeProperties
        p.length() == 0 ? null : p
    }

    /**
     * Returns the IMAP name space, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_ns_shared"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeImapNsShared() {
        def p = profileProperty "roundcube_imap_ns_shared", roundcubeProperties
        p.length() == 0 ? null : p
    }

    @Override
    void setScript(LinuxScript script) {
        super.setScript(script);
        roundcubePermissions.setScript this
    }
}
