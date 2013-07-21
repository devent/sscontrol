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
import com.anrisoftware.sscontrol.mail.statements.Alias
import com.anrisoftware.sscontrol.mail.statements.Domain
import com.anrisoftware.sscontrol.mail.statements.User
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures the postfix service to use MySQL database for virtual users
 * and domains.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MysqlScript extends LinuxScript {

	@Inject
	PostfixScriptLogger log

	@Inject
	BasePostfixScript basePostfixScript

	@Inject
	PostfixPropertiesProvider postfixProperties

	/**
	 * Renderer for {@code inet_interfaces}.
	 */
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
		runDistributionSpecific()
		basePostfixScript.postfixScript = this
		runScript basePostfixScript
		postfixTemplates = templatesFactory.create "PostfixScript", templatesAttributes
		mainTemplate = postfixTemplates.getResource "main_hash_configuration"
		postmapsTemplate = postfixTemplates.getResource "postmaps_configuration"
		if (service.domains.size() > 0) {
			deployMain()
			deployVirtualDomains()
			deployAliases()
			deployMailbox()
		}
		restartServices()
	}

	/**
	 * Deploy distribution specific configuration.
	 */
	abstract runDistributionSpecific()

	/**
	 * Returns additional template attributes.
	 */
	Map getTemplatesAttributes() {
		["renderers": [bindAddressesRenderer]]
	}

	/**
	 * Sets the virtual aliases and mailboxes.
	 *
	 * @see #getMainFile()
	 * @see #getAliasMapsFile()
	 */
	void deployMain() {
		def configuration = []
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_domains.*", mainTemplate.getText(true, "aliasDomains", "file", aliasDomainsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_maps.*", mainTemplate.getText(true, "aliasMaps", "file", aliasMapsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_base.*", mainTemplate.getText(true, "mailboxBase", "dir", mailboxBaseDir))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_maps.*", mainTemplate.getText(true, "mailboxMaps", "file", mailboxMapsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_minimum_uid.*", mainTemplate.getText(true, "minimumUid", "uid", minimumUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_uid_maps.*", mainTemplate.getText(true, "uidMaps", "uid", virtualUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_gid_maps.*", mainTemplate.getText(true, "gidMaps", "gid", virtualGid))
		def currentConfiguration = currentConfiguration mainFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, mainFile
	}

	/**
	 * Deploys the list of virtual domains.
	 *
	 * @see #getAliasDomainsFile()
	 */
	void deployVirtualDomains() {
		def configuration = service.domains.inject([]) { list, Domain domain ->
			if (domain.enabled) {
				list << new TokenTemplate("(?m)^\\#?${domain.name}.*", postmapsTemplate.getText(true, "domain", "domain", domain))
			} else {
				list
			}
		}
		def currentConfiguration = currentConfiguration aliasDomainsFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, aliasDomainsFile
		rehashFile aliasDomainsFile
	}

	/**
	 * Deploys the list of virtual aliases.
	 *
	 * @see #getAliasMapsFile()
	 */
	void deployAliases() {
		def configuration = service.domains.inject([]) { list, domain ->
			list << domain.aliases.inject([]) { aliases, Alias alias ->
				if (alias.enabled) {
					def text = postmapsTemplate.getText(true, "alias", "alias", alias)
					aliases << new TokenTemplate("(?m)^\\#?${alias.name}@${alias.domain.name}.*", text)
				} else {
					aliases
				}
			}
		}
		def currentConfiguration = currentConfiguration aliasMapsFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, aliasMapsFile
		rehashFile aliasMapsFile
	}

	/**
	 * Deploys the list of virtual mailbox.
	 *
	 * @see #getMailboxMapsFile()
	 */
	void deployMailbox() {
		def configuration = service.domains.inject([]) { list, domain ->
			list << domain.users.inject([]) { users, User user ->
				if (user.enabled) {
					def path = createMailboxPath(user)
					def text = postmapsTemplate.getText(true, "user", "user", user, "path", path)
					users << new TokenTemplate("(?m)^\\#?${user.name}@${user.domain.name}.*", text)
				} else {
					users
				}
			}
		}
		def currentConfiguration = currentConfiguration mailboxMapsFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, mailboxMapsFile
		rehashFile mailboxMapsFile
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
