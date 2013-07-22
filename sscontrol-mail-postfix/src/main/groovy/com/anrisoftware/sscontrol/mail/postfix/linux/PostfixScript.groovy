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
 * Configures the postfix service from hash files.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class PostfixScript extends LinuxScript {

	@Inject
	PostfixScriptLogger log

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
	Templates postfixTemplates

	TemplateResource mainTemplate

	TemplateResource postmapsTemplate

	@Override
	def run() {
		super.run()
		runDistributionSpecific()
		postfixScript.postfixScript = this
		runScript postfixScript
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
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_domains.*", mainTemplate.getText(true, "aliasDomains", "file", postfixScript.virtualDomainsFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_alias_maps.*", mainTemplate.getText(true, "aliasMaps", "file", postfixScript.virtualAliasFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_base.*", mainTemplate.getText(true, "mailboxBase", "dir", postfixScript.mailboxBaseDir))
		configuration << new TokenTemplate("(?m)^\\#?virtual_mailbox_maps.*", mainTemplate.getText(true, "mailboxMaps", "file", postfixScript.virtualMailboxFile))
		configuration << new TokenTemplate("(?m)^\\#?virtual_minimum_uid.*", mainTemplate.getText(true, "minimumUid", "uid", postfixScript.minimumUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_uid_maps.*", mainTemplate.getText(true, "uidMaps", "uid", postfixScript.virtualUid))
		configuration << new TokenTemplate("(?m)^\\#?virtual_gid_maps.*", mainTemplate.getText(true, "gidMaps", "gid", postfixScript.virtualGid))
		def currentConfiguration = currentConfiguration postfixScript.mainFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, postfixScript.mainFile
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
		def currentConfiguration = currentConfiguration postfixScript.virtualDomainsFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, postfixScript.virtualDomainsFile
		postfixScript.rehashFile postfixScript.virtualDomainsFile
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
		def currentConfiguration = currentConfiguration postfixScript.virtualAliasFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, postfixScript.virtualAliasFile
		postfixScript.rehashFile postfixScript.virtualAliasFile
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
					def path = postfixScript.createMailboxPath(user)
					def text = postmapsTemplate.getText(true, "user", "user", user, "path", path)
					users << new TokenTemplate("(?m)^\\#?${user.name}@${user.domain.name}.*", text)
				} else {
					users
				}
			}
		}
		def currentConfiguration = currentConfiguration postfixScript.virtualMailboxFile
		deployConfiguration configurationTokens(), currentConfiguration, configuration, postfixScript.virtualMailboxFile
		postfixScript.rehashFile postfixScript.virtualMailboxFile
	}
}
