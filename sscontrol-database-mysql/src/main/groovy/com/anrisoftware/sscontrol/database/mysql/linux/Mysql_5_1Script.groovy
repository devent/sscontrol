

package com.anrisoftware.sscontrol.database.mysql.linux

import static org.apache.commons.io.FileUtils.*

import java.util.regex.Pattern

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Script to configure MySQL 5.1.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Mysql_5_1Script extends LinuxScript {

	Templates mysqlTemplates

	TemplateResource mysqlConfiguration

	TemplateResource zoneConfiguration

	@Override
	def run() {
		super.run()
		mysqlTemplates = templatesFactory.create("Mysql_5_1")
		mysqlConfiguration = mysqlTemplates.getResource("configuration")
		deployMysqldConfiguration()
		restartService restartCommand
	}

	/**
	 * Deploys the mysqld configuration.
	 */
	void deployMysqldConfiguration() {
		deployConfiguration configurationTokens(), currentMysqldConfiguration, mysqldConfiguration, mysqldFile
	}

	/**
	 * Returns the mysqld configuration.
	 */
	List getMysqldConfiguration() {
		def replace = mysqlConfiguration.getText(true, "mysqld", "service", service)
		[
			new TokenTemplate(".*", replace, Pattern.DOTALL)
		]
	}

	/**
	 * Returns path of the MySQL configuration directory.
	 * <p>
	 * Example: {@code /etc/mysql/conf.d}
	 *
	 * <ul>
	 * <li>profile property key {@code configuration_directory}</li>
	 * </ul>
	 */
	abstract File getConfigurationDir()

	/**
	 * Returns the file of the {@code mysqld} configuration file.
	 * <p>
	 * Example: {@code sscontrol_mysqld.cnf}
	 *
	 * <ul>
	 * <li>profile property key {@code mysqld_configuration_file}</li>
	 * </ul>
	 */
	abstract File getMysqldFile()

	/**
	 * Returns the current {@code mysqld} configuration.
	 */
	String getCurrentMysqldConfiguration() {
		currentConfiguration mysqldFile
	}

	/**
	 * Returns the restart command for the MySQL server.
	 * <p>
	 * Example: {@code /etc/init.d/mysql restart}
	 *
	 * <ul>
	 * <li>profile property key {@code restart_command}</li>
	 * </ul>
	 */
	abstract String getRestartCommand()
}
