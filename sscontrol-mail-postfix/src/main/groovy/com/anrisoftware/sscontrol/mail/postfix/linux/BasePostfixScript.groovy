/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.mail.statements.User
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Sets the basic postfix configuration.
 *
 * <ul>
 * <li>bind address,
 * <li>default destinations,
 * <li>mail name,
 * <li>etc.
 * </ul>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BasePostfixScript extends LinuxScript {

	/**
	 * The {@link Templates} for the script.
	 */
	Templates postfixTemplates

	/**
	 * Postfix main configuration file templates.
	 */
	TemplateResource mainTemplate

	/**
	 * Renderer for {@code inet_interfaces}.
	 */
	@Inject
	BindAddressesRenderer bindAddressesRenderer

	/**
	 * Postfix default properties.
	 */
	@Inject
	PostfixPropertiesProvider postfixProperties

	/**
	 * Delegate postfix service script.
	 */
	LinuxScript postfixScript

	@Override
	def run() {
		postfixTemplates = templatesFactory.create "PostfixScript", templatesAttributes
		mainTemplate = postfixTemplates.getResource "main_configuration"
		appendDefaultDestinations()
		deployMailname()
		deployMain()
	}

	/**
	 * Returns additional template attributes.
	 */
	Map getTemplatesAttributes() {
		["renderers": [bindAddressesRenderer]]
	}

	/**
	 * Sets the Postfix service script.
	 */
	void setPostfixScript(LinuxScript script) {
		this.postfixScript = script
	}

	/**
	 * @see #postfixScript
	 */
	@Override
	def getDefaultProperties() {
		postfixScript.defaultProperties
	}

	/**
	 * Append the default destinations.
	 */
	void appendDefaultDestinations() {
		service.destinations defaultDestinations
	}

	/**
	 * Returns a list of the default destination domains.
	 *
	 * <ul>
	 * <li>profile property {@code "default_destinations"}</li>
	 * </ul>
	 */
	List getDefaultDestinations() {
		profileListProperty "default_destinations", postfixProperties.get()
	}

	/**
	 * Sets the domain name of the mail server. This is done usually in
	 * the {@code /etc/mailname} file.
	 *
	 * @see #getMailnameFile()
	 * @see #getCurrentMailnameConfiguration()
	 * @see #getMailnameConfiguration()
	 */
	void deployMailname() {
		deployConfiguration configurationTokens(), currentMailnameConfiguration, mailnameConfiguration, mailnameFile
	}

	/**
	 * Returns the current mail name configuration. This is usually the
	 * configuration file {@code /etc/mailname}.
	 *
	 * @see #getMailnameFile()
	 */
	String getCurrentMailnameConfiguration() {
		currentConfiguration mailnameFile
	}

	/**
	 * Returns the mail name configuration.
	 */
	List getMailnameConfiguration() {
		[
			new TokenTemplate(".*",
			postfixTemplates.getResource("mailname_configuration").
			getText(true, "name", service.domainName))
		]
	}

	/**
	 * Returns the absolute path of the mail name configuration file.
	 *
	 * <ul>
	 * <li>property {@code "mailname_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getMailnameFile() {
		def file = profileProperty("mailname_file", defaultProperties) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * Sets the main configuration of the mail server. This is done usually in
	 * the {@code /etc/postfix/main.cf} file.
	 *
	 * @see #getMainFile()
	 * @see #getAliasMapsFile()
	 */
	void deployMain() {
		def configuration = [
			new TokenTemplate("(?m)^\\#?myhostname.*", mainTemplate.getText(true, "hostname", "mail", service)),
			new TokenTemplate("(?m)^\\#?myorigin.*", mainTemplate.getText(true, "origin", "mail", service)),
			new TokenTemplate("(?m)^\\#?smtpd_banner.*", mainTemplate.getText(true, "banner", "banner", banner)),
			new TokenTemplate("(?m)^\\#?relayhost.*", mainTemplate.getText(true, "relayhost", "mail", service)),
			new TokenTemplate("(?m)^\\#?inet_interfaces.*", mainTemplate.getText(true, "interfaces", "mail", service)),
			new TokenTemplate("(?m)^\\#?mydestination.*", mainTemplate.getText(true, "destinations", "mail", service)),
			new TokenTemplate("(?m)^\\#?masquerade_domains.*", mainTemplate.getText(true, "masqueradeDomains", "mail", service)),
		]
		def currentConfiguration = currentConfiguration mainFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, mainFile
	}

	/**
	 * Returns the banner text that follows the 220 status code in the SMTP
	 * greeting banner.
	 *
	 * <ul>
	 * <li>profile property {@code "banner"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getBanner() {
		profileProperty "banner", postfixProperties.get()
	}

	/**
	 * Returns the {@code main.cf} file.
	 *
	 * <ul>
	 * <li>property {@code "main_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getMainFile() {
		def file = profileProperty("main_file", defaultProperties) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * Returns the path of the configuration directory.
	 *
	 * <ul>
	 * <li>property {@code "configuration_directory"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	File getConfigurationDir() {
		profileProperty("configuration_directory", defaultProperties) as File
	}

	/**
	 * Execute the {@code postmap} command on the specified file.
	 *
	 * @see #getPostmapCommand()
	 */
	void rehashFile(File file) {
		def template = mysqlTemplates.getResource("postmap_command")
		def worker = scriptCommandFactory.create(template, "postmapCommand", postmapCommand, "file", file)()
		log.rehashFileDone this, file, worker
	}

	/**
	 * Replace the variables of the mailbox path pattern.
	 */
	String createMailboxPath(User user) {
		String pattern = mailboxPattern.replace('${domain}', user.domain.name)
		pattern = pattern.replace('${user}', user.name)
	}

	/**
	 * Returns the absolute path of the alias domains hash table file.
	 * Default is the file {@code "alias_domains"} in the configuration
	 * directory.
	 *
	 * <ul>
	 * <li>profile property {@code "alias_domains_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #postfixProperties
	 */
	File getAliasDomainsFile() {
		def file = profileProperty("alias_domains_file", postfixProperties.get()) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * Returns the absolute path of the alias maps hash table file.
	 * Default is the file {@code "alias_maps"} in the configuration
	 * directory.
	 *
	 * <ul>
	 * <li>profile property {@code "alias_maps_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #postfixProperties
	 */
	File getAliasMapsFile() {
		def file = profileProperty("alias_maps_file", postfixProperties.get()) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * Returns the absolute path of the virtual mailbox maps hash table file.
	 * Default is the file {@code "mailbox_maps"} in the configuration
	 * directory.
	 *
	 * <ul>
	 * <li>profile property {@code "mailbox_maps_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #postfixProperties
	 */
	File getMailboxMapsFile() {
		def file = profileProperty("mailbox_maps_file", postfixProperties.get()) as File
		file.absolute ? file : new File(configurationDir, file.name)
	}

	/**
	 * Returns the command for the {@code postmap} command. Can be a full
	 * path or just the command name that can be found in the current
	 * search path.
	 *
	 * <ul>
	 * <li>profile property {@code "postmap_command"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getPostmapCommand() {
		profileProperty("postmap_command", defaultProperties)
	}

	/**
	 * Returns the path of the base directory for virtual users.
	 *
	 * <ul>
	 * <li>profile property {@code "mailbox_base_directory"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	File getMailboxBaseDir() {
		profileProperty("mailbox_base_directory", defaultProperties) as File
	}

	/**
	 * Returns the pattern for the directories that are created for each virtual
	 * domain and virtual user under the mailbox base directory.
	 *
	 * <ul>
	 * <li>profile property {@code "mailbox_pattern"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getMailboxPattern() {
		profileProperty("mailbox_pattern", postfixProperties.get()) as File
	}

	/**
	 * Returns the minimum identification number for virtual users.
	 *
	 * <ul>
	 * <li>profile property {@code "minimum_uid"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Number getMinimumUid() {
		profileProperty("minimum_uid", defaultProperties) as Integer
	}

	/**
	 * Returns the user identification number for virtual users.
	 *
	 * <ul>
	 * <li>profile property {@code "virtual_uid"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Number getVirtualUid() {
		profileProperty("virtual_uid", defaultProperties) as Integer
	}

	/**
	 * Returns the group identification number for virtual users.
	 *
	 * <ul>
	 * <li>profile property {@code "virtual_gid"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Number getVirtualGid() {
		profileProperty("virtual_gid", defaultProperties) as Integer
	}
}
