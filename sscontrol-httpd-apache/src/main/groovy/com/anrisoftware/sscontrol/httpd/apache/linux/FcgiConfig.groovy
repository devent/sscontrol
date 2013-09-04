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

	def deployConfig(Domain domain, Map domainUser) {
		createScriptDirectory domain, domainUser
		deployStarterScript domain, domainUser
	}

	private createScriptDirectory(Domain domain, Map domainUser) {
		def scripts = scriptDir domain
		scripts.mkdirs()
		changeOwner user: domainUser.user, group: domainUser.group, files: scripts, recursive: true
	}

	private deployStarterScript(Domain domain, Map domainUser) {
		def string = fcgiConfigTemplate.getText true, "fcgiStarter", "properties", this
		def file = scriptStarterFile(domain)
		FileUtils.write file, string
		changeOwner user: domainUser.user, group: domainUser.group, files: file
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
