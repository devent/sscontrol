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
package com.anrisoftware.sscontrol.mail.postfix.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.mail.statements.Domain
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
	MysqlPropertiesProvider postfixProperties

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

	TemplateResource configTemplate

	TemplateResource dataTemplate

	@Override
	def run() {
		super.run()
		runDistributionSpecific()
		postfixScript.postfixProperties = postfixProperties.get()
		postfixScript.postfixScript = this
		runScript postfixScript
		mysqlTemplates = templatesFactory.create "MysqlScript"
		tablesTemplate = mysqlTemplates.getResource "database_tables"
		mainTemplate = mysqlTemplates.getResource "main_configuration"
		configTemplate = mysqlTemplates.getResource "database_configuration"
		dataTemplate = mysqlTemplates.getResource "database_data"
		deployDatabaseTables()
		deployMain()
		deployVirtualMailboxFile()
		deployDomains()
		deployAliases()
		deployUsers()
		restartServices()
	}

	/**
	 * Deploy distribution specific configuration.
	 */
	abstract runDistributionSpecific()

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
				"mysqlCommand", mysqlCommand, "service", service, "profile", postfixScript)()
		log.deployedUsersTable this, worker
	}

	/**
	 * Sets the virtual aliases and mailboxes.
	 */
	void deployMain() {
		def configuration = []
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_base.*", mainTemplate.getText(true, "mailboxBase", "dir", postfixScript.mailboxBaseDir))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_maps.*", mainTemplate.getText(true, "mailboxMaps", "file", postfixScript.mailboxMapsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_maps.*", mainTemplate.getText(true, "aliasMaps", "file", postfixScript.aliasMapsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_domains.*", mainTemplate.getText(true, "mailboxDomains", "file", postfixScript.mailboxDomainsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_uid_maps.*", mainTemplate.getText(true, "uidMaps", "uid", postfixScript.virtualUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_gid_maps.*", mainTemplate.getText(true, "gidMaps", "gid", postfixScript.virtualGid))
		def currentConfiguration = currentConfiguration postfixScript.mainFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, postfixScript.mainFile
	}

	/**
	 * Deploys the virtual mailbox database configuration.
	 */
	void deployVirtualMailboxFile() {
		def string = configTemplate.getText(true, "mailbox", "service", service)
		FileUtils.write postfixScript.mailboxMapsFile, string
		string = configTemplate.getText(true, "alias", "service", service)
		FileUtils.write postfixScript.aliasMapsFile, string
		string = configTemplate.getText(true, "domains", "service", service)
		FileUtils.write postfixScript.mailboxDomainsFile, string
	}

	/**
	 * Deploys the virtual domains.
	 */
	void deployDomains() {
		def worker = scriptCommandFactory.create(dataTemplate, "insertDomains",
				"mysqlCommand", mysqlCommand, "service", service)()
		log.deployedDomainsData this, worker
	}

	/**
	 * Deploys the virtual aliases.
	 */
	void deployAliases() {
		for (Domain domain : service.domains) {
			if (domain.aliases.empty) {
				continue
			}
			def worker = scriptCommandFactory.create(dataTemplate, "insertAliases",
					"mysqlCommand", mysqlCommand, "service", service, "domain", domain)()
			log.deployedAliasesData this, worker
		}
	}

	/**
	 * Deploys the virtual users.
	 */
	void deployUsers() {
		for (Domain domain : service.domains) {
			if (domain.users.empty) {
				continue
			}
			def worker = scriptCommandFactory.create(dataTemplate, "insertUsers",
					"mysqlCommand", mysqlCommand, "service", service, "domain", domain)()
			log.deployedUsersData this, worker
		}
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
