/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.courierdelivery.linux

import static java.util.regex.Pattern.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.mail.api.MailService
import com.anrisoftware.sscontrol.mail.postfix.linux.DeliveryConfig
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BaseDelivery
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * Courier/Mysql delivery.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class CourierMysqlDeliveryConfig extends BaseDelivery implements DeliveryConfig {

    public static final String NAME = "courier.mysql"

    @Inject
    private CourierMysqlDeliveryConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    InstallPackagesFactory installPackagesFactory

    /**
     * The {@link Templates} for the script.
     */
    Templates courierTemplates

    TemplateResource authConfigTemplate

    TemplateResource imapdConfigTemplate

    TemplateResource imapdSslConfigTemplate

    @Override
    String getDeliveryName() {
        NAME
    }

    @Override
    void deployDelivery() {
        courierTemplates = templatesFactory.create "CourierMysqlDeliveryConfig"
        authConfigTemplate = courierTemplates.getResource "authconfig"
        imapdConfigTemplate = courierTemplates.getResource "imapdconfig"
        imapdSslConfigTemplate = courierTemplates.getResource "imapdsslconfig"
        installPackages()
        deployAuthdaemonConfig()
        deployAuthmysqlConfig()
        deployImapdConfig()
        deployImapdSslConfig()
    }

    /**
     * Installs the packages for Courier/Mysql delivery.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: script.installCommand,
                packages: courierPackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Deploys the Courier {@code "authdaemonrc"} configuration.
     */
    void deployAuthdaemonConfig() {
        def config = []
        config << moduleListConfig()
        config << debugLoggingConfig(service)
        def current = currentConfiguration authdaemonFile
        deployConfiguration configurationTokens(), current, config, authdaemonFile
        logg.configurationDeployed this, authdaemonFile
    }

    def moduleListConfig() {
        def search = authConfigTemplate.getText(true, "moduleListSearch")
        def replace = authConfigTemplate.getText(true, "moduleList", "modules", authModules)
        new TokenTemplate(search, replace, MULTILINE)
    }

    def debugLoggingConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "debugLoggingSearch")
        def replace = authConfigTemplate.getText(true, "debugLogging", "level", service.debug.level)
        new TokenTemplate(search, replace, MULTILINE)
    }

    /**
     * Deploys the Courier {@code "authmysqlrc"} configuration.
     */
    void deployAuthmysqlConfig() {
        def conf = []
        conf << mysqlServerConfig(service)
        conf << mysqlPortConfig(service)
        conf << mysqlDatabaseConfig(service)
        conf << mysqlUsernameConfig(service)
        conf << mysqlPasswordConfig(service)
        conf << mysqlUserTableConfig(service)
        conf << mysqlCryptPwfieldConfig(service)
        conf << mysqlClearPwfieldConfig()
        conf << mysqlUidFieldConfig()
        conf << mysqlGidFieldConfig()
        conf << mysqlLoginFieldConfig()
        conf << mysqlHomeFieldConfig()
        conf << mysqlNameFieldConfig()
        conf << mysqlMaildirFieldConfig()
        conf << mysqlWhereClauseConfig()
        def current = currentConfiguration authmysqlFile
        deployConfiguration configurationTokens(), current, conf, authmysqlFile
        logg.configurationDeployed this, authmysqlFile
    }

    def mysqlServerConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlServerSearch")
        def config = authConfigTemplate.getText(true, "mysqlServer", "service", service)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlPortConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlPortSearch")
        def config = authConfigTemplate.getText(true, "mysqlPort", "service", service)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlDatabaseConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlDatabaseSearch")
        def config = authConfigTemplate.getText(true, "mysqlDatabase", "service", service)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlUsernameConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlUsernameSearch")
        def config = authConfigTemplate.getText(true, "mysqlUsername", "service", service)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlPasswordConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlPasswordSearch")
        def config = authConfigTemplate.getText(true, "mysqlPassword", "service", service)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlUserTableConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlUserTableSearch")
        def config = authConfigTemplate.getText(true, "mysqlUserTable", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlCryptPwfieldConfig(MailService service) {
        def search = authConfigTemplate.getText(true, "mysqlCryptPwfieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlCryptPwfield", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlClearPwfieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlClearPwfieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlClearPwfield", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlUidFieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlUidFieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlUidField", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlGidFieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlGidFieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlGidField", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlLoginFieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlLoginFieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlLoginField", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlHomeFieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlHomeFieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlHomeField", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlNameFieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlNameFieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlNameField", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlMaildirFieldConfig() {
        def search = authConfigTemplate.getText(true, "mysqlMaildirFieldSearch")
        def config = authConfigTemplate.getText(true, "mysqlMaildirField", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    def mysqlWhereClauseConfig() {
        def search = authConfigTemplate.getText(true, "mysqlWhereClauseSearch")
        def config = authConfigTemplate.getText(true, "mysqlWhereClause", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    /**
     * Deploys the Courier {@code "imapd"} configuration.
     */
    void deployImapdConfig() {
        def conf = []
        conf << imapCapabilityConfig()
        def current = currentConfiguration imapdFile
        deployConfiguration configurationTokens(), current, conf, imapdFile
        logg.configurationDeployed this, imapdFile
    }

    def imapCapabilityConfig() {
        def search = imapdConfigTemplate.getText(true, "imapCapabilitySearch")
        def config = imapdConfigTemplate.getText(true, "imapCapability", "properties", this)
        new TokenTemplate(search, config, MULTILINE)
    }

    /**
     * Returns the list of Courier IMAP capabilities.
     *
     * <ul>
     * <li>property {@code "courier_imap_capabilities"}</li>
     * </ul>
     *
     * @see #getDeliveryProperties()
     */
    List getImapCapabilities() {
        profileListProperty "courier_imap_capabilities", deliveryProperties
    }

    /**
     * Deploys the Courier {@code "imapd-ssl"} configuration.
     */
    void deployImapdSslConfig() {
        def conf = []
        conf << certFileConfig()
        conf << trustCertsConfig()
        conf << tlsRequiredConfig()
        def current = currentConfiguration imapdSslFile
        deployConfiguration configurationTokens(), current, conf, imapdSslFile
        logg.configurationDeployed this, imapdSslFile
    }

    def certFileConfig() {
        def file = deployCertPemFile(service)
        if (!file) {
            return []
        }
        def search = imapdSslConfigTemplate.getText(true, "certFileSearch")
        def config = imapdSslConfigTemplate.getText(true, "certFile", "file", file)
        new TokenTemplate(search, config, MULTILINE)
    }

    def trustCertsConfig() {
        def search = imapdSslConfigTemplate.getText(true, "trustCertsSearch")
        def config = imapdSslConfigTemplate.getText(true, "trustCerts", "dir", certsDir)
        new TokenTemplate(search, config, MULTILINE)
    }

    def tlsRequiredConfig() {
        def search = imapdSslConfigTemplate.getText(true, "tlsRequiredSearch")
        def config = imapdSslConfigTemplate.getText(true, "tlsRequired", "enabled", tlsRequiredEnabled)
        new TokenTemplate(search, config, MULTILINE)
    }

    /**
     * Returns to require TLS.
     *
     * <ul>
     * <li>property {@code "courier_tls_required_enabled"}</li>
     * </ul>
     *
     * @see #getDeliveryProperties()
     */
    boolean getTlsRequiredEnabled() {
        profileBooleanProperty "courier_tls_required_enabled", deliveryProperties
    }

    String getUsersTable() {
        script.profileProperty "users_table", deliveryProperties
    }

    String getEnabledField() {
        script.profileProperty "enabled_field", deliveryProperties
    }

    String getLoginField() {
        script.profileProperty "login_field", deliveryProperties
    }

    String getNameField() {
        script.profileProperty "name_field", deliveryProperties
    }

    String getUidField() {
        script.profileProperty "uid_field", deliveryProperties
    }

    String getGidField() {
        script.profileProperty "gid_field", deliveryProperties
    }

    String getHomeField() {
        script.profileProperty "home_field", deliveryProperties
    }

    String getMaildirField() {
        script.profileProperty "maildir_field", deliveryProperties
    }

    String getClearField() {
        script.profileProperty "clear_field", deliveryProperties
    }

    String getCryptField() {
        script.profileProperty "crypt_field", deliveryProperties
    }

    /**
     * Returns the list of authentication modules.
     *
     * <ul>
     * <li>property {@code "courier_auth_modules"}</li>
     * </ul>
     *
     * @see #getMainFile()
     */
    List getAuthModules() {
        profileListProperty "courier_auth_modules", deliveryProperties
    }

    /**
     * Returns the list of Courier/packages.
     *
     * <ul>
     * <li>property {@code "courier_packages"}</li>
     * </ul>
     *
     * @see #getDeliveryProperties()
     */
    List getCourierPackages() {
        profileListProperty "courier_packages", deliveryProperties
    }

    /**
     * Returns the path of the configuration directory.
     *
     * <ul>
     * <li>property {@code "courier_configuration_directory"}</li>
     * </ul>
     *
     * @see #getDeliveryProperties()
     */
    File getCourierConfigurationDir() {
        script.profileProperty("courier_configuration_directory", deliveryProperties) as File
    }

    /**
     * Returns the {@code authdaemonrc} file. If the path is not absolute
     * then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>property {@code "courier_authdaemon_file"}</li>
     * </ul>
     *
     * @see #getCourierConfigurationDir()
     * @see #getDeliveryProperties()
     */
    File getAuthdaemonFile() {
        script.profileFileProperty "courier_authdaemon_file", courierConfigurationDir, deliveryProperties
    }

    /**
     * Returns the {@code authmysqlrc} file. If the path is not absolute
     * then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>property {@code "courier_authmysql_file"}</li>
     * </ul>
     *
     * @see #getCourierConfigurationDir()
     * @see #getDeliveryProperties()
     */
    File getAuthmysqlFile() {
        profileFileProperty "courier_authmysql_file", courierConfigurationDir, deliveryProperties
    }

    /**
     * Returns the {@code imapd} file. If the path is not absolute
     * then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>property {@code "courier_imapd_file"}</li>
     * </ul>
     *
     * @see #getCourierConfigurationDir()
     * @see #getDeliveryProperties()
     */
    File getImapdFile() {
        profileFileProperty "courier_imapd_file", courierConfigurationDir, deliveryProperties
    }

    /**
     * Returns the {@code imapd-ssl} file. If the path is not absolute
     * then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>property {@code "courier_imapd_ssl_file"}</li>
     * </ul>
     *
     * @see #getCourierConfigurationDir()
     * @see #getDeliveryProperties()
     */
    File getImapdSslFile() {
        profileFileProperty "courier_imapd_ssl_file", courierConfigurationDir, deliveryProperties
    }
}
