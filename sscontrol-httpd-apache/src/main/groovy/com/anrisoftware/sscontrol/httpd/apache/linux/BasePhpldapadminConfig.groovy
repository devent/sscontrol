package com.anrisoftware.sscontrol.httpd.apache.linux


/**
 * Returns phpmyadmin/properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BasePhpldapadminConfig {

	ApacheScript script

	void setScript(ApacheScript script) {
		this.script = script
	}

	/**
	 * Returns the current {@code phpmyadmin.conf} configuration.
	 */
	String getPhpmyadminConfiguration() {
		currentConfiguration configurationFile
	}

	/**
	 * Returns the mysql client command, for example {@code /usr/bin/mysql}.
	 *
	 * <ul>
	 * <li>profile property {@code "mysql_command"}</li>
	 * </ul>
	 */
	String getMysqlCommand() {
		profileProperty "mysql_command", defaultProperties
	}

	/**
	 * Returns the list of needed phpldapadmin/packages.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_packages"}</li>
	 * </ul>
	 */
	List getAdminPackages() {
		script.profileListProperty "phpldapadmin_packages"
	}

	/**
	 * Phpldapadmin/configuration directory, for
	 * example {@code "/etc/phpldapadmin"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_directory"}</li>
	 * </ul>
	 */
	File getAdminConfigurationDir() {
		profileProperty("phpldapadmin_configuration_directory") as File
	}

	/**
	 * Phpldapadmin/configuration file, for
	 * example {@code "config/config.php"}. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_file"}</li>
	 * </ul>
	 */
	File getAdminConfigFile() {
		propertyFile("phpldapadmin_configuration_file", defaultProperties, phpmyadminConfigurationDir) as File
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
