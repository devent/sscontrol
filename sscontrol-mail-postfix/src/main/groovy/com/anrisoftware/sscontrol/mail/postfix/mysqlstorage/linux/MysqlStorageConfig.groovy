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
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.mail.postfix.linux.BindAddressesRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.StorageConfig
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BaseStorage
import com.anrisoftware.sscontrol.mail.statements.Domain
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Mysql/storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MysqlStorageConfig extends BaseStorage implements StorageConfig {

	public static final String NAME = "mysql"

	@Inject
	MysqlStorageConfigLogger log

	@Inject
	MysqlStoragePropertiesProvider mysqlStorageProperties

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
	String getStorageName() {
		NAME
	}

	@Override
	void deployStorage() {
		mysqlTemplates = templatesFactory.create "MysqlStorageConfig"
		tablesTemplate = mysqlTemplates.getResource "database_tables"
		mainTemplate = mysqlTemplates.getResource "main_configuration"
		configTemplate = mysqlTemplates.getResource "database_configuration"
		dataTemplate = mysqlTemplates.getResource "database_data"
		installPackages mysqlStoragePackages
		deployDatabaseTables()
		deployMain()
		deployVirtualMailboxFile()
		deployDomains()
		deployAliases()
		deployUsers()
		createVirtualDirectory()
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
				"mysqlCommand", mysqlCommand, "service", service, "profile", script)()
		log.deployedUsersTable this, worker
	}

	/**
	 * Sets the virtual aliases and mailboxes.
	 */
	void deployMain() {
		def configuration = []
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_base.*", mainTemplate.getText(true, "mailboxBase", "dir", script.mailboxBaseDir))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_maps.*", mainTemplate.getText(true, "mailboxMaps", "file", mailboxMapsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_maps.*", mainTemplate.getText(true, "aliasMaps", "file", aliasMapsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_domains.*", mainTemplate.getText(true, "mailboxDomains", "file", mailboxDomainsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_uid_maps.*", mainTemplate.getText(true, "uidMaps", "uid", script.virtualUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_gid_maps.*", mainTemplate.getText(true, "gidMaps", "gid", script.virtualGid))
		deployConfiguration configurationTokens(), currentMainConfiguration, configuration, mainFile
	}

	/**
	 * Deploys the virtual mailbox database configuration.
	 */
	void deployVirtualMailboxFile() {
		def string = configTemplate.getText(true, "mailbox", "service", service)
		FileUtils.write mailboxMapsFile, string
		string = configTemplate.getText(true, "alias", "service", service)
		FileUtils.write aliasMapsFile, string
		string = configTemplate.getText(true, "domains", "service", service)
		FileUtils.write mailboxDomainsFile, string
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
	 * Creates the virtual mail box directory and set the owner.
	 */
	void createVirtualDirectory() {
		File dir = script.mailboxBaseDir
		dir.exists() ? null : dir.mkdirs()
		addGroup([groupName: virtualGroupName, groupId: virtualGid, systemGroup: true])
		addUser([userName: virtualUserName, groupName: virtualGroupName, userId: virtualUid, systemGroup: true])
		changeOwner([owner: virtualUid, ownerGroup: virtualGid, files: dir])
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
		profileProperty "mysql_command", storageProperties
	}

	/**
	 * Returns list of needed packages.
	 */
	List getMysqlStoragePackages() {
		profileListProperty "packages", storageProperties
	}

	/**
	 * Returns the absolute path of the mailbox maps file.
	 *
	 * <ul>
	 * <li>profile property {@code "mailbox_maps_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getStorageProperties()
	 */
	File getMailboxMapsFile() {
		propertyFile "mailbox_maps_file", storageProperties
	}

	/**
	 * Returns the absolute path of the alias maps file.
	 *
	 * <ul>
	 * <li>profile property {@code "alias_maps_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getStorageProperties()
	 */
	File getAliasMapsFile() {
		propertyFile "alias_maps_file", storageProperties
	}

	/**
	 * Returns the absolute path of the mailbox domains maps file.
	 *
	 * <ul>
	 * <li>profile property {@code "mailbox_domains_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getStorageProperties()
	 */
	File getMailboxDomainsFile() {
		propertyFile "mailbox_domains_file", storageProperties
	}
}
