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
                "properties", this, "service", service)()
        log.deployedAliasesTable script, worker
        worker = scriptCommandFactory.create(tablesTemplate, "createDomainsTable",
                "properties", this, "service", service)()
        log.deployedDomainsTable script, worker
        worker = scriptCommandFactory.create(tablesTemplate, "createUsersTable",
                "properties", this, "service", service, "profile", script)()
        log.deployedUsersTable script, worker
    }

    /**
     * Sets the virtual aliases and mailboxes.
     */
    void deployMain() {
        def configuration = []
        configuration << new TokenTemplate(mailboxBaseSearch, mailboxBase)
        configuration << new TokenTemplate(mailboxMapsSearch, mailboxMaps)
        configuration << new TokenTemplate(aliasesMapsSearch, aliasesMaps)
        configuration << new TokenTemplate(mailboxDomainsSearch, mailboxDomains)
        configuration << new TokenTemplate(uidMapsSearch, uidMaps)
        configuration << new TokenTemplate(gidMapsSearch, gidMaps)
        deployConfiguration configurationTokens(), currentMainConfiguration, configuration, mainFile
    }

    String getMailboxBaseSearch() {
        mainTemplate.getText(true, "mailboxBaseSearch")
    }

    String getMailboxBase() {
        mainTemplate.getText(true, "mailboxBase", "dir", script.mailboxBaseDir)
    }

    String getMailboxMapsSearch() {
        mainTemplate.getText(true, "mailboxMapsSearch")
    }

    String getMailboxMaps() {
        mainTemplate.getText(true, "mailboxMaps", "file", mailboxMapsFile)
    }

    String getAliasesMapsSearch() {
        mainTemplate.getText(true, "aliasesMapsSearch")
    }

    String getAliasesMaps() {
        mainTemplate.getText(true, "aliasMaps", "file", aliasMapsFile)
    }

    String getMailboxDomainsSearch() {
        mainTemplate.getText(true, "mailboxDomainsSearch")
    }

    String getMailboxDomains() {
        mainTemplate.getText(true, "mailboxDomains", "file", mailboxDomainsFile)
    }

    String getUidMapsSearch() {
        mainTemplate.getText(true, "uidMapsSearch")
    }

    String getUidMaps() {
        mainTemplate.getText(true, "uidMaps", "uid", script.virtualUid)
    }

    String getGidMapsSearch() {
        mainTemplate.getText(true, "gidMapsSearch")
    }

    String getGidMaps() {
        mainTemplate.getText(true, "gidMaps", "gid", script.virtualGid)
    }

    /**
     * Deploys the virtual mailbox database configuration.
     */
    void deployVirtualMailboxFile() {
        def string = configTemplate.getText(true, "mailbox", "properties", this, "service", service)
        FileUtils.write mailboxMapsFile, string
        log.mailboxMapsDeployed script, mailboxMapsFile, string
        string = configTemplate.getText(true, "alias", "properties", this, "service", service)
        FileUtils.write aliasMapsFile, string
        log.aliasMapsDeployed script, mailboxMapsFile, string
        string = configTemplate.getText(true, "domains", "properties", this, "service", service)
        FileUtils.write mailboxDomainsFile, string
        log.mailboxDomainsDeployed script, mailboxMapsFile, string
        changeMod mod: "o-r", files: [
            mailboxMapsFile,
            aliasMapsFile,
            mailboxDomainsFile
        ]
    }

    /**
     * Deploys the virtual domains.
     */
    void deployDomains() {
        service.resetDomains.resetDomains ? resetDomains() : false
        def worker = scriptCommandFactory.create(dataTemplate, "insertDomains",
                "properties", this, "service", service)()
        log.deployedDomainsData script, worker
    }

    /**
     * Resets virtual domains.
     */
    void resetDomains() {
        def worker = scriptCommandFactory.create(dataTemplate, "resetDomains",
                "properties", this, "service", service)()
        log.resetDomainsData script, worker
    }

    /**
     * Deploys the virtual aliases.
     */
    void deployAliases() {
        service.resetDomains.resetAliases ? resetAliases() : false
        for (Domain domain : service.domains) {
            if (domain.aliases.empty) {
                continue
            }
            def worker = scriptCommandFactory.create(dataTemplate, "insertAliases",
                    "properties", this, "service", service, "domain", domain)()
            log.deployedAliasesData script, worker
        }
    }

    /**
     * Resets aliases.
     */
    void resetAliases() {
        def worker = scriptCommandFactory.create(dataTemplate, "resetAliases",
                "properties", this, "service", service)()
        log.resetAliasesData script, worker
    }

    /**
     * Deploys the virtual users.
     */
    void deployUsers() {
        service.resetDomains.resetUsers ? resetUsers() : false
        for (Domain domain : service.domains) {
            if (domain.users.empty) {
                continue
            }
            def worker = scriptCommandFactory.create(dataTemplate, "insertUsers",
                    "properties", this, "service", service, "domain", domain)()
            log.deployedUsersData script, worker
        }
    }

    /**
     * Resets users.
     */
    void resetUsers() {
        def worker = scriptCommandFactory.create(dataTemplate, "resetUsers",
                "properties", this, "service", service)()
        log.resetUsersData script, worker
    }

    /**
     * Creates the virtual mail box directory and set the owner.
     */
    void createVirtualDirectory() {
        File dir = script.mailboxBaseDir
        dir.exists() ? null : dir.mkdirs()
        addGroup groupName: virtualGroupName, groupId: virtualGid, systemGroup: true
        addUser userName: virtualUserName, groupName: virtualGroupName, userId: virtualUid, systemGroup: true
        changeOwner owner: virtualUid, ownerGroup: virtualGid, files: dir
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
        profileFileProperty "mailbox_maps_file", configurationDir, storageProperties
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
        profileFileProperty "alias_maps_file", configurationDir, storageProperties
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
        profileFileProperty "mailbox_domains_file", configurationDir, storageProperties
    }

    String getAliasesTable() {
        script.profileProperty "aliases_table", storageProperties
    }

    String getIdField() {
        script.profileProperty "id_field", storageProperties
    }

    String getMailField() {
        script.profileProperty "mail_field", storageProperties
    }

    String getDestinationField() {
        script.profileProperty "destination_field", storageProperties
    }

    String getEnabledField() {
        script.profileProperty "enabled_field", storageProperties
    }

    String getDomainsTable() {
        script.profileProperty "domains_table", storageProperties
    }

    String getDomainField() {
        script.profileProperty "domain_field", storageProperties
    }

    String getTransportField() {
        script.profileProperty "transport_field", storageProperties
    }

    String getUsersTable() {
        script.profileProperty "users_table", storageProperties
    }

    String getLoginField() {
        script.profileProperty "login_field", storageProperties
    }

    String getNameField() {
        script.profileProperty "name_field", storageProperties
    }

    String getUidField() {
        script.profileProperty "uid_field", storageProperties
    }

    String getGidField() {
        script.profileProperty "gid_field", storageProperties
    }

    String getHomeField() {
        script.profileProperty "home_field", storageProperties
    }

    String getMaildirField() {
        script.profileProperty "maildir_field", storageProperties
    }

    String getChangePasswordField() {
        script.profileProperty "change_password_field", storageProperties
    }

    String getClearField() {
        script.profileProperty "clear_field", storageProperties
    }

    String getCryptField() {
        script.profileProperty "crypt_field", storageProperties
    }

    String getQuotaField() {
        script.profileProperty "quota_field", storageProperties
    }

    String getProcmailrcField() {
        script.profileProperty "procmailrc_field", storageProperties
    }

    String getSpamassassinrcField() {
        script.profileProperty "spamassassinrc_field", storageProperties
    }
}
