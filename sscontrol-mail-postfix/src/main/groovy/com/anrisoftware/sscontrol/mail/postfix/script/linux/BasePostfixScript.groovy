

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
package com.anrisoftware.sscontrol.mail.postfix.script.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger
import com.anrisoftware.sscontrol.mail.postfix.linux.BindAddressesRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.DeliveryConfig
import com.anrisoftware.sscontrol.mail.postfix.linux.DurationRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.PostfixPropertiesProvider
import com.anrisoftware.sscontrol.mail.postfix.linux.StorageConfig
import com.anrisoftware.sscontrol.mail.statements.User
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.google.inject.Provider

/**
 * Postfix/base configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BasePostfixScript extends LinuxScript {

	@Inject
	BasePostfixScriptLogger log

	/**
	 * The {@link Templates} for the Postfix/base script.
	 */
	Templates basePostfixTemplates

	/**
	 * {@code main.cf} configuration templates.
	 */
	TemplateResource mainConfigurationTemplate

	/**
	 * {@code /etc/mailname} configuration templates.
	 */
	TemplateResource mailnameConfigurationTemplate

	/**
	 * {@code postalias} command templates.
	 */
	TemplateResource postaliasCommandTemplate

	/**
	 * {@code postmap} command templates.
	 */
	TemplateResource postmapCommandTemplate

	/**
	 * Renderer for {@code inet_interfaces}.
	 */
	@Inject
	BindAddressesRenderer bindAddressesRenderer

	/**
	 * Renderer for time duration.
	 */
	@Inject
	DurationRenderer durationRenderer

	@Inject
	PostfixPropertiesProvider postfixPropertiesProvider

	/**
	 * Storages configuration.
	 */
	@Inject
	Map<String, Provider<StorageConfig>> storages

	/**
	 * Storages configuration.
	 */
	@Inject
	Map<String, Provider<DeliveryConfig>> deliveries

	/**
	 * Returns the profile name of the script.
	 */
	abstract String getProfileName()

	@Override
	def run() {
		basePostfixTemplates = templatesFactory.create "BasePostfixScript", templatesAttributes
		mainConfigurationTemplate = basePostfixTemplates.getResource "main_configuration"
		mailnameConfigurationTemplate = basePostfixTemplates.getResource("mailname_configuration")
		postaliasCommandTemplate = basePostfixTemplates.getResource "postalias_command"
		postmapCommandTemplate = basePostfixTemplates.getResource "postmap_command"
		super.run()
		runDistributionSpecific()
		appendDefaultDestinations()
		deployMailname()
		deployMain()
		reconfigureFiles()
		deployStorage()
		deployDelivery()
	}

	/**
	 * Runs distribution-specific configuration before
	 * the Postfix/configuration.
	 */
	abstract runDistributionSpecific()

	/**
	 * Deploy storage configuration.
	 */
	def deployStorage() {
		def provider = storages["${storageName}.${profileName}"]
		log.checkStorageConfig provider, this, storageName, profileName
		def config = provider.get()
		config.script = this
		config.deployStorage()
	}

	/**
	 * Deploy delivery configuration.
	 */
	def deployDelivery() {
		def provider = deliveries["${deliveryName}.${storageName}.${profileName}"]
		log.checkDeliveryConfig provider, this, deliveryName, storageName, profileName
		def config = provider.get()
		config.script = this
		config.deployDelivery()
	}

	/**
	 * Rehash and re-alias files.
	 */
	def reconfigureFiles() {
		aliasesDatabaseFile.isFile() ? null : FileUtils.touch(aliasesDatabaseFile)
		realiasFile aliasesDatabaseFile
	}

	/**
	 * Append the default destinations.
	 */
	void appendDefaultDestinations() {
		service.destinations defaultDestinations
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
	 * Returns the mail name configuration.
	 */
	List getMailnameConfiguration() {
		def template = new TokenTemplate(".*\n", mailnameConfigurationTemplate.getText(true, "name", service.domainName))
		template.enclose = false
		[template]
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
			new TokenTemplate("(?m)^\\#?myhostname.*", mainConfigurationTemplate.getText(true, "hostname", "mail", service)),
			new TokenTemplate("(?m)^\\#?myorigin.*", mainConfigurationTemplate.getText(true, "origin", "mail", service)),
			new TokenTemplate("(?m)^\\#?smtpd_banner.*", mainConfigurationTemplate.getText(true, "banner", "banner", banner)),
			new TokenTemplate("(?m)^\\#?relayhost.*", mainConfigurationTemplate.getText(true, "relayhost", "mail", service)),
			new TokenTemplate("(?m)^\\#?inet_interfaces.*", mainConfigurationTemplate.getText(true, "interfaces", "mail", service)),
			new TokenTemplate("(?m)^\\#?mydestination.*", mainConfigurationTemplate.getText(true, "destinations", "mail", service)),
			new TokenTemplate("(?m)^\\#?masquerade_domains.*", mainConfigurationTemplate.getText(true, "masqueradeDomains", "mail", service)),
			new TokenTemplate("(?m)^\\#?alias_maps.*", mainConfigurationTemplate.getText(true, "aliasMaps", "file", aliasesMapsFile)),
			new TokenTemplate("(?m)^\\#?alias_database.*", mainConfigurationTemplate.getText(true, "aliasDatabase", "file", aliasesDatabaseFile)),
			new TokenTemplate("(?m)^\\#?unknown_local_recipient_reject_code.*", mainConfigurationTemplate.getText(true, "unknownLocalRecipientRejectCode", "code", unknownLocalRecipientRejectCode)),
			new TokenTemplate("(?m)^\\#?delay_warning_time.*", mainConfigurationTemplate.getText(true, "delayWarningTime", "time", delayWarningTime)),
			new TokenTemplate("(?m)^\\#?maximal_queue_lifetime.*", mainConfigurationTemplate.getText(true, "maximalQueueLifetime", "time", maximalQueueLifetime)),
			new TokenTemplate("(?m)^\\#?minimal_backoff_time.*", mainConfigurationTemplate.getText(true, "minimalRetriesDelay", "time", minimalRetriesDelay)),
			new TokenTemplate("(?m)^\\#?maximal_backoff_time.*", mainConfigurationTemplate.getText(true, "maximumRetriesDelay", "time", maximalRetriesDelay)),
			new TokenTemplate("(?m)^\\#?smtp_helo_timeout.*", mainConfigurationTemplate.getText(true, "heloTimeout", "time", heloTimeout)),
			new TokenTemplate("(?m)^\\#?smtpd_recipient_limit.*", mainConfigurationTemplate.getText(true, "recipientLimit", "limit", recipientLimit)),
			new TokenTemplate("(?m)^\\#?smtpd_soft_error_limit.*", mainConfigurationTemplate.getText(true, "backOffErrorLimit", "limit", backOffErrorLimit)),
			new TokenTemplate("(?m)^\\#?smtpd_hard_error_limit.*", mainConfigurationTemplate.getText(true, "blockingErrorLimit", "limit", blockingErrorLimit)),
			new TokenTemplate("(?m)^\\#?smtpd_helo_restrictions.*", mainConfigurationTemplate.getText(true, "heloRestrictions", "restrictions", heloRestrictions)),
			new TokenTemplate("(?m)^\\#?smtpd_sender_restrictions.*", mainConfigurationTemplate.getText(true, "senderRestrictions", "restrictions", senderRestrictions)),
			new TokenTemplate("(?m)^\\#?smtpd_client_restrictions.*", mainConfigurationTemplate.getText(true, "clientRestrictions", "restrictions", clientRestrictions)),
			new TokenTemplate("(?m)^\\#?smtpd_recipient_restrictions.*", mainConfigurationTemplate.getText(true, "recipientRestrictions", "restrictions", recipientRestrictions)),
			new TokenTemplate("(?m)^\\#?smtpd_data_restrictions.*", mainConfigurationTemplate.getText(true, "dataRestrictions", "restrictions", dataRestrictions)),
			new TokenTemplate("(?m)^\\#?smtpd_helo_required.*", mainConfigurationTemplate.getText(true, "heloRequired", "flag", heloRequired)),
			new TokenTemplate("(?m)^\\#?smtpd_delay_reject.*", mainConfigurationTemplate.getText(true, "delayReject", "flag", delayReject)),
			new TokenTemplate("(?m)^\\#?disable_vrfy_command.*", mainConfigurationTemplate.getText(true, "disableVrfyCommand", "flag", disableVrfyCommand)),
		]
		deployConfiguration configurationTokens(), currentMainConfiguration, configuration, mainFile
	}

	/**
	 * Returns the storage method for domains and users.
	 *
	 * <ul>
	 * <li>profile property {@code "storage"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getStorageName() {
		profileProperty "storage"
	}

	/**
	 * Returns the delivery method.
	 *
	 * <ul>
	 * <li>profile property {@code "delivery"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getDeliveryName() {
		profileProperty "delivery"
	}

	/**
	 * Returns a list of the default destination domains.
	 *
	 * <ul>
	 * <li>profile property {@code "default_destinations"}</li>
	 * </ul>
	 *
	 * @see #getPostfixProperties()
	 */
	List getDefaultDestinations() {
		profileListProperty "default_destinations", postfixProperties
	}

	/**
	 * Returns the banner text that follows the 220 status code in the SMTP
	 * greeting banner.
	 *
	 * <ul>
	 * <li>profile property {@code "banner"}</li>
	 * </ul>
	 *
	 * @see #getPostfixProperties()
	 */
	String getBanner() {
		profileProperty "banner", postfixProperties
	}

	/**
	 * Returns the absolute path of the mail name configuration file.
	 * If the path is not absolute then it is assume to be under
	 * the configuration directory.
	 *
	 * <ul>
	 * <li>property {@code "mailname_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getMailnameFile() {
		propertyFile "mailname_file"
	}

	/**
	 * Returns the {@code main.cf} file. If the path is not absolute then it
	 * is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>property {@code "main_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getMainFile() {
		propertyFile "main_file"
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
		profileProperty("configuration_directory") as File
	}

	/**
	 * Returns the absolute path of the aliases maps file.
	 * If the path is not absolute then it is assume to be under
	 * the configuration directory. For example {@code "aliases"}.
	 *
	 * <ul>
	 * <li>profile property {@code "aliases_maps_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getAliasesMapsFile() {
		propertyFile "aliases_maps_file"
	}

	/**
	 * Returns the absolute path of the aliases database file.
	 * If the path is not absolute then it is assume to be under
	 * the configuration directory. For example {@code "aliases"}.
	 *
	 * <ul>
	 * <li>profile property {@code "aliases_database_file"}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getAliasesDatabaseFile() {
		propertyFile "aliases_database_file"
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
	 * Returns the command for the {@code postalias} command. Can be a full
	 * path or just the command name that can be found in the current
	 * search path.
	 *
	 * <ul>
	 * <li>profile property {@code "postalias_command"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getPostaliasCommand() {
		profileProperty("postalias_command", defaultProperties)
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
		profileProperty("mailbox_pattern", postfixProperties)
	}

	/**
	 * Returns the numerical Postfix/SMTP server response code when a
	 * local recipient address is unknown. Example {@code 550}.
	 *
	 * <ul>
	 * <li>profile property {@code "unknown_local_recipient_reject_code"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	int getUnknownLocalRecipientRejectCode() {
		profileNumberProperty("unknown_local_recipient_reject_code", postfixProperties)
	}

	/**
	 * Returns the time after which the sender receives a copy of the message
	 * headers of mail that is still queued. Example {@code PT4H}.
	 *
	 * <ul>
	 * <li>profile property {@code "delay_warning_time"}</li>
	 * </ul>
	 *
	 * @see Duration#parse(String)
	 * @see #postfixProperties
	 */
	Duration getDelayWarningTime() {
		profileDurationProperty("delay_warning_time", postfixProperties)
	}

	/**
	 * Returns the time after which the message is consider as undeliverable.
	 * Example {@code P7D}.
	 *
	 * <ul>
	 * <li>profile property {@code "maximal_queue_lifetime"}</li>
	 * </ul>
	 *
	 * @see Duration#parse(String)
	 * @see #postfixProperties
	 */
	Duration getMaximalQueueLifetime() {
		profileDurationProperty("maximal_queue_lifetime", postfixProperties)
	}

	/**
	 * Returns the minimal time between attempts to deliver a deferred message.
	 * Example {@code PT15M}.
	 *
	 * <ul>
	 * <li>profile property {@code "minimal_retries_delay"}</li>
	 * </ul>
	 *
	 * @see Duration#parse(String)
	 * @see #postfixProperties
	 */
	Duration getMinimalRetriesDelay() {
		profileDurationProperty("minimal_retries_delay", postfixProperties)
	}

	/**
	 * Returns the maximal time between attempts to deliver a deferred message.
	 * Example {@code PT2H}.
	 *
	 * <ul>
	 * <li>profile property {@code "maximal_retries_delay"}</li>
	 * </ul>
	 *
	 * @see Duration#parse(String)
	 * @see #postfixProperties
	 */
	Duration getMaximalRetriesDelay() {
		profileDurationProperty("maximal_retries_delay", postfixProperties)
	}

	/**
	 * Returns the Postfix/SMTP client time limit for sending the HELO or
	 * EHLO command, and for receiving the initial remote SMTP server response.
	 * Example {@code PT60S}.
	 *
	 * <ul>
	 * <li>profile property {@code "helo_timeout"}</li>
	 * </ul>
	 *
	 * @see Duration#parse(String)
	 * @see #postfixProperties
	 */
	Duration getHeloTimeout() {
		profileDurationProperty("helo_timeout", postfixProperties)
	}

	/**
	 * Returns the maximal number of recipients that the Postfix/SMTP server
	 * accepts per message delivery request.
	 * Example {@code 16}.
	 *
	 * <ul>
	 * <li>profile property {@code "recipient_limit"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	int getRecipientLimit() {
		profileNumberProperty("recipient_limit", postfixProperties)
	}

	/**
	 * Returns the number of errors before the Postfix/SMTP server slows
	 * down all its responses. Example {@code 3}.
	 *
	 * <ul>
	 * <li>profile property {@code "back_off_error_limit"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	int getBackOffErrorLimit() {
		profileNumberProperty("back_off_error_limit", postfixProperties)
	}

	/**
	 * Returns the maximal number of errors a remote SMTP client is allowed
	 * to make without delivering mail. Example {@code 12}.
	 *
	 * <ul>
	 * <li>profile property {@code "blocking_error_limit"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	int getBlockingErrorLimit() {
		profileNumberProperty("blocking_error_limit", postfixProperties)
	}

	/**
	 * Returns the restrictions that the Postfix/SMTP server applies in the
	 * context of a client HELO command.
	 *
	 * <ul>
	 * <li>profile property {@code "helo_restrictions"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getHeloRestrictions() {
		profileProperty("helo_restrictions", postfixProperties)
	}

	/**
	 * Returns the restrictions that the Postfix/SMTP server applies in the
	 * context of a client MAIL FROM command.
	 *
	 * <ul>
	 * <li>profile property {@code "sender_restrictions"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getSenderRestrictions() {
		profileProperty("sender_restrictions", postfixProperties)
	}

	/**
	 * Returns the restrictions that the Postfix/SMTP server applies in the
	 * context of a client connection request.
	 *
	 * <ul>
	 * <li>profile property {@code "client_restrictions"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getClientRestrictions() {
		profileProperty("client_restrictions", postfixProperties)
	}

	/**
	 * Returns the restrictions that the Postfix/SMTP server applies in the
	 * context of a client RCPT TO command
	 *
	 * <ul>
	 * <li>profile property {@code "recipient_restrictions"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getRecipientRestrictions() {
		profileProperty("recipient_restrictions", postfixProperties)
	}

	/**
	 * Returns the restrictions that the Postfix/SMTP server applies in the
	 * context of the SMTP DATA command.
	 *
	 * <ul>
	 * <li>profile property {@code "data_restrictions"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	String getDataRestrictions() {
		profileProperty("data_restrictions", postfixProperties)
	}

	/**
	 * Require that a remote SMTP client introduces itself with the HELO/EHLO
	 * command.
	 *
	 * <ul>
	 * <li>profile property {@code "helo_required"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	boolean isHeloRequired() {
		profileProperty("helo_required", postfixProperties)
	}

	/**
	 * Wait until the RCPT TO/ETRN command.
	 *
	 * <ul>
	 * <li>profile property {@code "delay_reject"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	boolean isDelayReject() {
		profileProperty("delay_reject", postfixProperties)
	}

	/**
	 * Disable the SMTP VRFY command.
	 *
	 * <ul>
	 * <li>profile property {@code "disable_vrfy_command"}</li>
	 * </ul>
	 *
	 * @see #postfixProperties
	 */
	boolean isDisableVrfyCommand() {
		profileProperty("disable_vrfy_command", postfixProperties)
	}

	/**
	 * Returns the group name of the virtual user accounts.
	 *
	 * <ul>
	 * <li>profile property {@code "virtual_group_name"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getVirtualGroupName() {
		profileProperty "virtual_group_name"
	}

	/**
	 * Returns the user name of the virtual user accounts.
	 *
	 * <ul>
	 * <li>profile property {@code "virtual_name_name"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getVirtualUserName() {
		profileProperty "virtual_name_name"
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
	int getMinimumUid() {
		profileNumberProperty "minimum_uid"
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
	int getVirtualUid() {
		profileNumberProperty "virtual_uid"
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
	int getVirtualGid() {
		profileNumberProperty "virtual_gid"
	}

	/**
	 * Returns the Postfix/properties.
	 */
	def getPostfixProperties() {
		postfixPropertiesProvider.get()
	}

	/**
	 * Returns additional template attributes.
	 */
	Map getTemplatesAttributes() {
		["renderers": [
				bindAddressesRenderer,
				durationRenderer
			]]
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
	 * Returns the current {@code main.cf} configuration. This is usually the
	 * configuration file {@code /etc/postfix/main.cf}.
	 *
	 * @see #getMainFile()
	 */
	String getCurrentMainConfiguration() {
		currentConfiguration mainFile
	}

	/**
	 * Execute the {@code postmap} command on the specified file.
	 *
	 * @see #getPostmapCommand()
	 */
	void rehashFile(File file) {
		def worker = scriptCommandFactory.create(postmapCommandTemplate, "postmapCommand", postmapCommand, "file", file)()
		log.rehashFileDone this, file, worker
	}

	/**
	 * Execute the {@code postalias} command on the specified file.
	 *
	 * @see #getPostmapCommand()
	 */
	void realiasFile(File file) {
		def worker = scriptCommandFactory.create(postaliasCommandTemplate, "command", postaliasCommand, "file", file)()
		log.realiasFileDone this, file, worker
	}

	/**
	 * Replace the variables of the mailbox path pattern.
	 * <ul>
	 * <li>{@code <domain>} is replaced with the user domain;
	 * <li>{@code <user>} is replaced with the user name;
	 * </ul>
	 *
	 * @param user
	 * 			  the {@link User}.
	 */
	String createMailboxPath(User user) {
		ST st = new ST(mailboxPattern).add("domain", user.domain.name).add("user", user.name)
		st.render()
	}
}
