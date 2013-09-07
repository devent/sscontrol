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
package com.anrisoftware.sscontrol.ldap.openldap.linux

import javax.inject.Inject

import org.apache.commons.io.FileUtils

/**
 * Deploys database, base and administrator, systems ACL's and LDAP ACL's.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Openldap_2_4Config {

	@Inject
	Openldap_2_4ConfigLogger log

	Openldap_2_4Script script

	void deployConfig() {
		deployDatabase()
		deployBase()
		deploySystem()
		deployLdap()
	}

	private deployDatabase() {
		def string = ldapConfigTemplate.getText true, "databaseConfig",
				"properties", script,
				"service", service
		FileUtils.write databaseConfigFile, string
		log.databaseConfigDeployed script, databaseConfigFile
	}

	private deployBase() {
		def string = ldapConfigTemplate.getText true, "baseConfig",
				"properties", script,
				"service", service
		FileUtils.write baseConfigFile, string
		log.baseConfigDeployed script, baseConfigFile
	}

	private deploySystem() {
		def string = ldapConfigTemplate.getText true, "systemConfig",
				"properties", script,
				"service", service
		FileUtils.write systemACLConfigFile, string
		log.systemConfigDeployed script, systemACLConfigFile
	}

	private deployLdap() {
		def string = ldapConfigTemplate.getText true, "ldapConfig",
				"properties", script,
				"service", service
		FileUtils.write ldapACLConfigFile, string
		log.ldapConfigDeployed script, ldapACLConfigFile
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
