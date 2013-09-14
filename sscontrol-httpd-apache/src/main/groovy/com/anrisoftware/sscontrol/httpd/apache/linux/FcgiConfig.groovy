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
package com.anrisoftware.sscontrol.httpd.apache.linux

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Configures php-fcgi for the domain.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FcgiConfig {

	Templates fcgiTemplates

	TemplateResource fcgiConfigTemplate

	ApacheScript script

	void setScript(ApacheScript script) {
		this.script = script
		fcgiTemplates = templatesFactory.create "Apache_2_2_Fcgi"
		fcgiConfigTemplate = fcgiTemplates.getResource "config"
	}

	def enableFcgi() {
		enableMods "fcgid"
	}

	def deployConfig(Domain domain) {
		createScriptDirectory domain
		deployStarterScript domain
	}

	private createScriptDirectory(Domain domain) {
		def user = domain.domainUser
		def scripts = scriptDir domain
		scripts.mkdirs()
		changeOwner owner: user.name, ownerGroup: user.group, files: scripts, recursive: true
	}

	private deployStarterScript(Domain domain) {
		def user = domain.domainUser
		def string = fcgiConfigTemplate.getText true, "fcgiStarter", "properties", this
		def file = scriptStarterFile(domain)
		FileUtils.write file, string
		changeOwner owner: user.name, ownerGroup: user.group, files: file
		changeMod mod: "755", files: file
	}

	/**
	 * Returns the maximum requests for php/fcgi.
	 *
	 * <ul>
	 * <li>profile property {@code "php_fcgi_max_requests"}</li>
	 * </ul>
	 */
	int getMaxRequests() {
		profileNumberProperty("php_fcgi_max_requests", defaultProperties)
	}

	/**
	 * Returns the sub-directory to save the php/fcgi scripts.
	 * For example {@code "php-fcgi-scripts"}.
	 *
	 * <ul>
	 * <li>profile property {@code "php_fcgi_scripts_subdirectory"}</li>
	 * </ul>
	 */
	String getScriptsSubdirectory() {
		profileProperty("php_fcgi_scripts_subdirectory", defaultProperties)
	}

	/**
	 * Returns the name of the fcgi/starter script.
	 * For example {@code "php-fcgi-starter"}.
	 *
	 * <ul>
	 * <li>profile property {@code "php_fcgi_scripts_file"}</li>
	 * </ul>
	 */
	String getScriptStarterFileName() {
		profileProperty("php_fcgi_scripts_file", defaultProperties)
	}

	/**
	 * Returns the scripts directory for the specified domain.
	 */
	File scriptDir(Domain domain) {
		new File(sitesDirectory, "$scriptsSubdirectory/$domain.name")
	}

	/**
	 * Returns the scripts directory for the specified domain.
	 */
	File scriptStarterFile(Domain domain) {
		new File(scriptDir(domain), scriptStarterFileName)
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
