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
	MysqlScriptLogger log

	@Inject
	BasePostfixScript postfixScript

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
	Templates mysqlTemplates

	TemplateResource tablesTemplate

	TemplateResource mainTemplate

	TemplateResource postmapsTemplate

	@Override
	def run() {
		super.run()
		//runDistributionSpecific()
		postfixScript.postfixScript = this
		runScript postfixScript
		mysqlTemplates = templatesFactory.create "MysqlScript"
		tablesTemplate = mysqlTemplates.getResource "database_tables"
		//mainTemplate = mysqlTemplates.getResource "main_configuration"
		//postmapsTemplate = mysqlTemplates.getResource "postmaps_configuration"
		deployDatabaseTables()
		//if (service.domains.size() > 0) {
		//deployMain()
		//deployVirtualDomains()
		//deployAliases()
		//deployMailbox()
		//}
		//restartServices()
	}

	/**
	 * Deploy distribution specific configuration.
	 */
	abstract runDistributionSpecific()

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
	 * Creates the tables in the database for virtual domains and users.
	 */
	void deployDatabaseTables() {
		def worker
		worker = scriptCommandFactory.create(tablesTemplate, "createAliasesTable",
				"mysqlCommand", mysqlCommand, "service", service)()
		log.deployedAliasesTable this, worker
		worker = scriptCommandFactory.create(tablesTemplate, "createDomainsTable",
				"mysqlCommand", mysqlCommand, "service", service)()
		log.deployedDomainsTable this, worker
		worker = scriptCommandFactory.create(tablesTemplate, "createUsersTable",
				"mysqlCommand", mysqlCommand, "service", service)()
		log.deployedUsersTable this, worker
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
	 * Returns the command for the MySQL server. Can be a full
	 * path or just the command name that can be found in the current
	 * search path.
	 *
	 * <ul>
	 * <li>profile property {@code "mysql_command"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getMysqlCommand() {
		profileProperty("mysql_command", defaultProperties)
	}
}
