/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.mail.postfix.linux.DeliveryConfig
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BaseDelivery
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Courier/Mysql delivery.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class CourierMysqlDeliveryConfig extends BaseDelivery implements DeliveryConfig {

	public static final String NAME = "courier.mysql"

	@Inject
	CourierMysqlDeliveryConfigLogger log

	@Inject
	DebugLoggingLevelRenderer debugLoggingLevelRenderer

	/**
	 * The {@link Templates} for the script.
	 */
	Templates courierTemplates

	TemplateResource authTemplate

	@Override
	String getDeliveryName() {
		NAME
	}

	@Override
	void deployDelivery() {
		courierTemplates = templatesFactory.create "CourierMysqlDeliveryConfig", templatesAttributes
		authTemplate = courierTemplates.getResource "auth_configuration"
		installPackages courierPackages
		deployConfig()
		restartServices restartCommand: courierRestartCommand
	}

	/**
	 * Sets the configuration.
	 */
	void deployConfig() {
		def configuration = []
		configuration << new TokenTemplate(moduleListSearchTemplate, moduleListTemplate, MULTILINE)
		configuration << new TokenTemplate(debugLoggingSearchTemplate, debugLoggingTemplate, MULTILINE)
		deployConfiguration configurationTokens(), currentAuthdaemonConfiguration, configuration, authdaemonFile
		log.configurationDeployed this, authdaemonFile
		configuration = []
		configuration << new TokenTemplate(mysqlServerSearchTemplate, mysqlServerTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlPortSearchTemplate, mysqlPortTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlDatabaseSearchTemplate, mysqlDatabaseTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlUsernameSearchTemplate, mysqlUsernameTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlPasswordSearchTemplate, mysqlPasswordTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlUserTableSearchTemplate, mysqlUserTableTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlCryptPwfieldSearchTemplate, mysqlCryptPwfieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlClearPwfieldSearchTemplate, mysqlClearPwfieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlUidFieldSearchTemplate, mysqlUidFieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlGidFieldSearchTemplate, mysqlGidFieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlLoginFieldSearchTemplate, mysqlLoginFieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlHomeFieldSearchTemplate, mysqlHomeFieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlNameFieldSearchTemplate, mysqlNameFieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlMaildirFieldSearchTemplate, mysqlMaildirFieldTemplate, MULTILINE)
		configuration << new TokenTemplate(mysqlWhereClauseSearchTemplate, mysqlWhereClauseTemplate, MULTILINE)
		deployConfiguration configurationTokens(), currentAuthmysqlConfiguration, configuration, authmysqlFile
		log.configurationDeployed this, authmysqlFile
	}

	/**
	 * Returns additional template attributes.
	 */
	Map getTemplatesAttributes() {
		["renderers": [
				debugLoggingLevelRenderer
			]]
	}

	String getModuleListSearchTemplate() {
		authTemplate.getText(true, "moduleListSearch")
	}

	String getModuleListTemplate() {
		authTemplate.getText(true, "moduleList", "modules", authModules)
	}

	String getDebugLoggingSearchTemplate() {
		authTemplate.getText(true, "debugLoggingSearch")
	}

	String getDebugLoggingTemplate() {
		authTemplate.getText(true, "debugLogging", "level", service.debugLogging)
	}

	String getMysqlServerSearchTemplate() {
		authTemplate.getText(true, "mysqlServerSearch")
	}

	String getMysqlServerTemplate() {
		authTemplate.getText(true, "mysqlServer", "server", service.database.server)
	}

	String getMysqlPortSearchTemplate() {
		authTemplate.getText(true, "mysqlPortSearch")
	}

	String getMysqlPortTemplate() {
		authTemplate.getText(true, "mysqlPort", "port", service.database.port)
	}

	String getMysqlDatabaseSearchTemplate() {
		authTemplate.getText(true, "mysqlDatabaseSearch")
	}

	String getMysqlDatabaseTemplate() {
		authTemplate.getText(true, "mysqlDatabase", "name", service.database.database)
	}

	String getMysqlUsernameSearchTemplate() {
		authTemplate.getText(true, "mysqlUsernameSearch")
	}

	String getMysqlUsernameTemplate() {
		authTemplate.getText(true, "mysqlUsername", "name", service.database.user)
	}

	String getMysqlPasswordSearchTemplate() {
		authTemplate.getText(true, "mysqlPasswordSearch")
	}

	String getMysqlPasswordTemplate() {
		authTemplate.getText(true, "mysqlPassword", "password", service.database.password)
	}

	String getMysqlUserTableSearchTemplate() {
		authTemplate.getText(true, "mysqlUserTableSearch")
	}

	String getMysqlUserTableTemplate() {
		authTemplate.getText(true, "mysqlUserTable", "table", usersTable)
	}

	String getMysqlCryptPwfieldSearchTemplate() {
		authTemplate.getText(true, "mysqlCryptPwfieldSearch")
	}

	String getMysqlCryptPwfieldTemplate() {
		authTemplate.getText(true, "mysqlCryptPwfield", "field", cryptField)
	}

	String getMysqlClearPwfieldSearchTemplate() {
		authTemplate.getText(true, "mysqlClearPwfieldSearch")
	}

	String getMysqlClearPwfieldTemplate() {
		authTemplate.getText(true, "mysqlClearPwfield")
	}

	String getMysqlUidFieldSearchTemplate() {
		authTemplate.getText(true, "mysqlUidFieldSearch")
	}

	String getMysqlUidFieldTemplate() {
		authTemplate.getText(true, "mysqlUidField", "field", uidField)
	}

	String getMysqlGidFieldSearchTemplate() {
		authTemplate.getText(true, "mysqlGidFieldSearch")
	}

	String getMysqlGidFieldTemplate() {
		authTemplate.getText(true, "mysqlGidField", "field", gidField)
	}

	String getMysqlLoginFieldSearchTemplate() {
		authTemplate.getText(true, "mysqlLoginFieldSearch")
	}

	String getMysqlLoginFieldTemplate() {
		authTemplate.getText(true, "mysqlLoginField", "field", loginField)
	}

	String getMysqlHomeFieldSearchTemplate() {
		authTemplate.getText(true, "mysqlHomeFieldSearch")
	}

	String getMysqlHomeFieldTemplate() {
		authTemplate.getText(true, "mysqlHomeField", "field", homeField)
	}

	String getMysqlNameFieldSearchTemplate() {
		authTemplate.getText(true, "mysqlNameFieldSearch")
	}

	String getMysqlNameFieldTemplate() {
		authTemplate.getText(true, "mysqlNameField", "field", nameField)
	}

	String getMysqlMaildirFieldSearchTemplate() {
		authTemplate.getText(true, "mysqlMaildirFieldSearch")
	}

	String getMysqlMaildirFieldTemplate() {
		authTemplate.getText(true, "mysqlMaildirField", "field", "concat(${homeField},'/',${maildirField})")
	}

	String getMysqlWhereClauseSearchTemplate() {
		authTemplate.getText(true, "mysqlWhereClauseSearch")
	}

	String getMysqlWhereClauseTemplate() {
		authTemplate.getText(true, "mysqlWhereClause", "clause", "${enabledField}=1")
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
	 * Returns the current {@code authdaemonrc} configuration. This is usually
	 * the configuration file {@code /etc/courier/authdaemonrc}.
	 *
	 * @see #getMainFile()
	 */
	String getCurrentAuthdaemonConfiguration() {
		currentConfiguration authdaemonFile
	}

	/**
	 * Returns the current {@code authmysqlrc} configuration. This is usually
	 * the configuration file {@code /etc/courier/authmysqlrc}.
	 *
	 * @see #getMainFile()
	 */
	String getCurrentAuthmysqlConfiguration() {
		currentConfiguration authmysqlFile
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
	 * Returns the restart command.
	 *
	 * <ul>
	 * <li>property {@code "courier_restart_command"}</li>
	 * </ul>
	 *
	 * @see #getDeliveryProperties()
	 */
	String getCourierRestartCommand() {
		profileProperty "courier_restart_command", deliveryProperties
	}

	/**
	 * Returns the services to restart.
	 *
	 * <ul>
	 * <li>property {@code "courier_restart_services"}</li>
	 * </ul>
	 *
	 * @see #getDeliveryProperties()
	 */
	List getCourierServices() {
		profileListProperty "courier_restart_services", deliveryProperties
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
		script.propertyFile "courier_authdaemon_file", deliveryProperties, courierConfigurationDir
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
		propertyFile "courier_authmysql_file", deliveryProperties, courierConfigurationDir
	}
}
