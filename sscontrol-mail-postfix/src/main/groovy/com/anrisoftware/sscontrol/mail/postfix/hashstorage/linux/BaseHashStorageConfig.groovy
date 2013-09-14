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
package com.anrisoftware.sscontrol.mail.postfix.hashstorage.linux

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.mail.postfix.linux.BindAddressesRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.StorageConfig
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BaseStorage
import com.anrisoftware.sscontrol.mail.statements.Alias
import com.anrisoftware.sscontrol.mail.statements.Domain
import com.anrisoftware.sscontrol.mail.statements.User
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Hash/storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseHashStorageConfig extends BaseStorage implements StorageConfig {

	public static final String NAME = "hash"

	@Inject
	BaseHashStorageConfigLogger log

	/**
	 * Renderer for {@code inet_interfaces}.
	 */
	@Inject
	BindAddressesRenderer bindAddressesRenderer

	/**
	 * Hash/storage templates.
	 */
	Templates hashStorageTemplates

	/**
	 * {@code main.cf} configuration templates.
	 */
	TemplateResource hashMainConfigurationTemplate

	/**
	 * Virtual domain and users configuration templates.
	 */
	TemplateResource postmapsConfigurationTemplate

	@Override
	String getStorageName() {
		NAME
	}

	@Override
	void deployStorage() {
		hashStorageTemplates = templatesFactory.create "BaseHashStorageConfig", templatesAttributes
		hashMainConfigurationTemplate = hashStorageTemplates.getResource "main_hash_configuration"
		postmapsConfigurationTemplate = hashStorageTemplates.getResource "postmaps_configuration"
		if (service.domains.size() > 0) {
			deployMain()
			deployVirtualDomains()
			deployAliases()
			deployMailbox()
			createVirtualDirectory()
		}
	}

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
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_domains.*", hashMainConfigurationTemplate.getText(true, "aliasDomains", "file", virtualDomainsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_maps.*", hashMainConfigurationTemplate.getText(true, "aliasMaps", "file", virtualAliasFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_base.*", hashMainConfigurationTemplate.getText(true, "mailboxBase", "dir", script.mailboxBaseDir))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_maps.*", hashMainConfigurationTemplate.getText(true, "mailboxMaps", "file", virtualMailboxFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_minimum_uid.*", hashMainConfigurationTemplate.getText(true, "minimumUid", "uid", script.minimumUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_uid_maps.*", hashMainConfigurationTemplate.getText(true, "uidMaps", "uid", script.virtualUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_gid_maps.*", hashMainConfigurationTemplate.getText(true, "gidMaps", "gid", script.virtualGid))
		deployConfiguration configurationTokens(), currentMainConfiguration, configuration, script.mainFile
	}

	/**
	 * Deploys the list of virtual domains.
	 *
	 * @see #getAliasDomainsFile()
	 */
	void deployVirtualDomains() {
		def configuration = service.domains.inject([]) { list, Domain domain ->
			if (domain.enabled) {
				list << new TokenTemplate("(?m)^\\#?${domain.name}.*", postmapsConfigurationTemplate.getText(true, "domain", "domain", domain))
			} else {
				list
			}
		}
		def currentConfiguration = currentConfiguration virtualDomainsFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, virtualDomainsFile
		rehashFile virtualDomainsFile
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
					def text = postmapsConfigurationTemplate.getText(true, "alias", "alias", alias)
					aliases << new TokenTemplate("(?m)^\\#?${alias.name}@${alias.domain.name}.*", text)
				} else {
					aliases
				}
			}
		}
		def currentConfiguration = currentConfiguration virtualAliasFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, virtualAliasFile
		rehashFile virtualAliasFile
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
					def path = script.createMailboxPath(user)
					def text = postmapsConfigurationTemplate.getText(true, "user", "user", user, "path", path)
					users << new TokenTemplate("(?m)^\\#?${user.name}@${user.domain.name}.*", text)
				} else {
					users
				}
			}
		}
		def currentConfiguration = currentConfiguration virtualMailboxFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, virtualMailboxFile
		rehashFile virtualMailboxFile
	}

	/**
	 * Creates the virtual mail box directory and set the owner.
	 */
	void createVirtualDirectory() {
		File dir = script.mailboxBaseDir
		dir.exists() ? null : dir.mkdirs()
		changeOwner([user: virtualUid, group: virtualGid, files: dir])
	}
}
