

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

import static org.apache.commons.io.FileUtils.*

import java.util.regex.Pattern

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
	}

	/**
	 * Sets the configuration.
	 */
	void deployConfig() {
		def configuration = []
		configuration << new TokenTemplate(moduleListSearchTemplate, moduleListTemplate, Pattern.MULTILINE)
		configuration << new TokenTemplate(debugLoggingSearchTemplate, debugLoggingTemplate, Pattern.MULTILINE)
		deployConfiguration configurationTokens(), currentAuthdaemonConfiguration, configuration, authdaemonFile
		log.configurationDeployed this, authdaemonFile
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
