/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.mail.postfix.linux.BindAddressesRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.StorageConfig
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BaseStorage
import com.anrisoftware.sscontrol.mail.statements.Alias
import com.anrisoftware.sscontrol.mail.statements.Domain
import com.anrisoftware.sscontrol.mail.statements.User
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory;
import com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddFactory;
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserAddFactory;

/**
 * Hash/storage.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class HashStorageConfig extends BaseStorage implements StorageConfig {

    public static final String NAME = "hash"

    @Inject
    HashStorageConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

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
        service.resetDomains.resetDomains ? resetDomains() : false
        def currentConfiguration = currentConfiguration virtualDomainsFile
        deployConfiguration configurationTokens(), currentConfiguration, configuration, virtualDomainsFile
        rehashFile virtualDomainsFile
    }

    /**
     * Removes the old domains file.
     */
    void resetDomains() {
        virtualDomainsFile.delete()
        logg.domainsReseted this, virtualDomainsFile
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
        service.resetDomains.resetAliases ? resetAliases() : false
        def currentConfiguration = currentConfiguration virtualAliasFile
        deployConfiguration configurationTokens(), currentConfiguration, configuration, virtualAliasFile
        rehashFile virtualAliasFile
    }

    /**
     * Removes the old aliases file.
     */
    void resetAliases() {
        virtualAliasFile.delete()
        logg.aliasesReseted this, virtualAliasFile
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
        service.resetDomains.resetUsers ? resetUsers() : false
        def currentConfiguration = currentConfiguration virtualMailboxFile
        deployConfiguration configurationTokens(), currentConfiguration, configuration, virtualMailboxFile
        rehashFile virtualMailboxFile
    }

    /**
     * Removes the old mailbox file.
     */
    void resetUsers() {
        virtualMailboxFile.delete()
        logg.usersReseted this, virtualMailboxFile
    }

    /**
     * Creates the virtual mail box directory and set the owner.
     */
    void createVirtualDirectory() {
        File dir = script.mailboxBaseDir
        dir.exists() ? null : dir.mkdirs()
        localGroupAddFactory.create(
                log: log,
                groupName: virtualGroupName,
                groupId: virtualGid,
                systemGroup: true,
                command: script.groupAddCommand,
                groupsFile: script.groupsFile,
                this, threads)()
        localUserAddFactory.create(
                log: log,
                userName: virtualUserName,
                groupName: virtualGroupName,
                userId: virtualUid,
                systemUser: true,
                command: script.userAddCommand,
                usersFile: script.usersFile,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log,
                owner: virtualUid,
                ownerGroup: virtualGid,
                files: [dir],
                command: script.chownCommand,
                this, threads)()
    }
}
