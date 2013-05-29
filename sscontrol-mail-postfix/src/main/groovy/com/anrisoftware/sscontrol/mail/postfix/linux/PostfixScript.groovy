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
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Uses the postfix service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class PostfixScript extends LinuxScript {

	@Inject
	PostfixScriptLogger log

	@Inject
	@Named("postfix-properties")
	ContextProperties postfixProperties

	@Inject
	BindAddressesRenderer bindAddressesRenderer

	/**
	 * The {@link Templates} for the script.
	 */
	Templates postfixTemplates

	TemplateResource mainTemplate

	TemplateResource postmapsTemplate

	@Override
	def run() {
		super.run()
		postfixTemplates = templatesFactory.create "Postfix", templatesAttributes
		mainTemplate = postfixTemplates.getResource("main_configuration")
		postmapsTemplate = postfixTemplates.getResource("postmaps_configuration")
		appendDefaultDestinations()
		deployMailname()
		deployMain()
		if (service.domains.size() > 0) {
			deployVirtualDomains()
		}
		distributionSpecificConfiguration()
	}

	/**
	 * Returns additional template attributes.
	 */
	Map getTemplatesAttributes() {
		["renderers": [bindAddressesRenderer]]
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
		profileListProperty "default_destinations", postfixProperties
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
	 * Sets the main configuration of the mail server. This is done usually in 
	 * the {@code /etc/postfix/main.cf} file.
	 * 	
	 * @see #getMainFile()
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
			{
				if (service.domains.size() > 0) {
					new TokenTemplate("(?m)^\\#?virtual_alias_domains.*", mainTemplate.getText(true, "aliasDomains", "file", aliasDomainsFile))
				} else {
					[]
				}
			}()
		]
		def currentConfiguration = currentConfiguration mainFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, mainFile
	}

	/**
	 * Deploys the list of virtual domains.
	 * 
	 * @see #getAliasDomainsFile()
	 */
	void deployVirtualDomains() {
		def configuration = service.domains.inject([]) { list, it ->
			list << new TokenTemplate("(?m)^\\#?${it.name}.*", postmapsTemplate.getText(true, "domain", "domain", it))
		}
		def currentConfiguration = currentConfiguration aliasDomainsFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, aliasDomainsFile
		rehashFile aliasDomainsFile
	}

	/**
	 * Execute the {@code postmap} command on the specified file.
	 * 
	 * @see #getPostmapCommand()
	 */
	void rehashFile(File file) {
		def template = postfixTemplates.getResource("postmap_command")
		def worker = scriptCommandFactory.create(template, "postmapCommand", postmapCommand, "file", file)()
		log.rehashFileDone this, file, worker
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
		profileProperty("banner", postfixProperties)
	}

	/**
	 * Returns the absolute path of the alias domains hash table file.
	 * Default is the file {@code alias_domains} in the configuration
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
		def file = profileProperty("alias_domains_file", postfixProperties) as File
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
	 * Returns the absolute path of the mail name configuration file.
	 * 
	 * <ul>
	 * <li>profile property {@code "mailname_file"}</li>
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
	 * Returns the {@code main.cf} file.
	 * 
	 * <ul>
	 * <li>profile property {@code "main_file"}</li>
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
	 * <li>profile property {@code "configuration_directory"}</li>
	 * </ul>
	 * 
	 * @see #getDefaultProperties()
	 */
	File getConfigurationDir() {
		profileProperty("configuration_directory", defaultProperties) as File
	}

	/**
	 * Run the distribution specific configuration.
	 */
	void distributionSpecificConfiguration() {
	}
}
