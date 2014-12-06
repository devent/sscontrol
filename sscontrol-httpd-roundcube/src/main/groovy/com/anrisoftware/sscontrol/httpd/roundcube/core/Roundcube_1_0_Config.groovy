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
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * <i>Roundcube 1.0.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Roundcube_1_0_Config extends RoundcubeConfig {

    @Inject
    UnpackFactory unpackFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    Templates roundcubeTemplates

    TemplateResource imapConfigTemplate

    TemplateResource databaseConfigTemplate

    TemplateResource smtpConfigTemplate

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
        def sampleConfig = sampleConfigurationFile domain, service
        def config = configurationFile domain, service
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
        def sampleConfig = sampleConfigurationFile domain, service
        def config = configurationFile domain, service
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
        def sampleConfig = sampleConfigurationFile domain, service
        def config = configurationFile domain, service
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
        def replace = smtpConfigTemplate.getText(true, "configSmtpServer", "server", service.smtpServer.server)
        new TokenTemplate(search, replace)
    }

    def configSmtpPort(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpPort_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpPort", "port", smtpServerPort)
        new TokenTemplate(search, replace)
    }

    def configSmtpUser(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpUser_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpUser", "user", service.smtpServer.user)
        new TokenTemplate(search, replace)
    }

    def configSmtpPass(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpPass_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpPass", "pass", service.smtpServer.password)
        new TokenTemplate(search, replace)
    }

    def configSmtpAuthType(RoundcubeService service) {
        def search = smtpConfigTemplate.getText(true, "configSmtpAuthType_search")
        def replace = smtpConfigTemplate.getText(true, "configSmtpAuthType", "type", smtpServerAuthType)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the <i>Roundcube</i> configuration file.
     * The placeholder variable are replaced:
     * <ul>
     * <li>"&lt;domainDir>" with the directory of the domain;</li>
     * <li>"&lt;prefix>" with the directory of the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #getRoundcubeConfigFile()
     * @see #domainDir(Domain)
     * @see RoundcubeService#getPrefix()
     */
    File configurationFile(Domain domain, RoundcubeService service) {
        def name = new ST(roundcubeConfigFile).
                add("domainDir", domainDir(domain)).
                add("prefix", service.prefix).
                render()
        new File(name)
    }

    /**
     * Returns the <i>Roundcube</i> sample configuration file.
     * The placeholder variable are replaced:
     * <ul>
     * <li>"&lt;domainDir>" with the directory of the domain;</li>
     * <li>"&lt;prefix>" with the directory of the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #getRoundcubeSampleConfigFile()
     * @see #domainDir(Domain)
     * @see RoundcubeService#getPrefix()
     */
    File sampleConfigurationFile(Domain domain, RoundcubeService service) {
        def name = new ST(roundcubeSampleConfigFile).
                add("domainDir", domainDir(domain)).
                add("prefix", service.prefix).
                render()
        new File(name)
    }

    /**
     * Returns the <i>Roundcube</i> temp directory.
     * The placeholder variable are replaced:
     * <ul>
     * <li>"&lt;domainDir>" with the directory of the domain;</li>
     * <li>"&lt;prefix>" with the directory of the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #getRoundcubeConfigFile()
     * @see #domainDir(Domain)
     * @see RoundcubeService#getPrefix()
     */
    File roundcubeTempDirectory(Domain domain, RoundcubeService service) {
        def name = new ST(roundcubeTempDirectory).
                add("domainDir", domainDir(domain)).
                add("prefix", service.prefix).
                render()
        new File(name)
    }

    /**
     * Returns the <i>Roundcube</i> logging directory.
     * The placeholder variable are replaced:
     * <ul>
     * <li>"&lt;domainDir>" with the directory of the domain;</li>
     * <li>"&lt;prefix>" with the directory of the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #getRoundcubeConfigFile()
     * @see #domainDir(Domain)
     * @see RoundcubeService#getPrefix()
     */
    File roundcubeLogDirectory(Domain domain, RoundcubeService service) {
        def name = new ST(roundcubeLogsDirectory).
                add("domainDir", domainDir(domain)).
                add("prefix", service.prefix).
                render()
        new File(name)
    }

    /**
     * Returns <i>Roundcube</i> configuration file, for
     * example {@code "<domainDir>/<prefix>/config/config.inc.php"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_config_file"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeConfigFile() {
        profileProperty "roundcube_config_file", roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> sample configuration file, for
     * example {@code "<domainDir>/<prefix>/config/config.inc.php.sample"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_sample_config_file"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeSampleConfigFile() {
        profileProperty "roundcube_sample_config_file", roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> temp directory, for
     * example {@code "<domainDir>/<prefix>/temp"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_temp_directory"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeTempDirectory() {
        profileProperty "roundcube_temp_directory", roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> logging directory, for
     * example {@code "<domainDir>/<prefix>/logs"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_logs_directory"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeLogsDirectory() {
        profileProperty "roundcube_logs_directory", roundcubeProperties
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

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.roundcubeTemplates = factory.create "Roundcube_1_0"
        this.imapConfigTemplate = roundcubeTemplates.getResource "imapconfig"
        this.databaseConfigTemplate = roundcubeTemplates.getResource "databaseconfig"
        this.smtpConfigTemplate = roundcubeTemplates.getResource "smtpconfig"
    }
}