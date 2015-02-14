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
package com.anrisoftware.sscontrol.mail.postfix.script.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.joda.time.Duration
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.mail.api.MailService
import com.anrisoftware.sscontrol.mail.certificate.Certificate
import com.anrisoftware.sscontrol.mail.postfix.linux.AuthConfig
import com.anrisoftware.sscontrol.mail.postfix.linux.BasePostfixScriptLogger
import com.anrisoftware.sscontrol.mail.postfix.linux.BindAddressesRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.DeliveryConfig
import com.anrisoftware.sscontrol.mail.postfix.linux.DurationRenderer
import com.anrisoftware.sscontrol.mail.postfix.linux.PostfixPropertiesProvider
import com.anrisoftware.sscontrol.mail.postfix.linux.StorageConfig
import com.anrisoftware.sscontrol.mail.statements.User
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory;
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory;
import com.google.inject.Provider

/**
 * Postfix/base configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class BasePostfixScript extends LinuxScript {

    @Inject
    BasePostfixScriptLogger logg

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * The {@link Templates} for the Postfix/base script.
     */
    Templates basePostfixTemplates

    /**
     * {@code main.cf} configuration templates.
     */
    TemplateResource mainConfigurationTemplate

    /**
     * {@code master.cf} configuration templates.
     */
    TemplateResource masterConfigurationTemplate

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
     * Authentication configurations.
     */
    @Inject
    Map<String, Provider<AuthConfig>> authentications

    /**
     * Returns the profile name of the script.
     */
    abstract String getProfileName()

    @Override
    def run() {
        basePostfixTemplates = templatesFactory.create "BasePostfixScript", templatesAttributes
        mainConfigurationTemplate = basePostfixTemplates.getResource "main_configuration"
        masterConfigurationTemplate = basePostfixTemplates.getResource "master_configuration"
        mailnameConfigurationTemplate = basePostfixTemplates.getResource("mailname_configuration")
        postaliasCommandTemplate = basePostfixTemplates.getResource "postalias_command"
        postmapCommandTemplate = basePostfixTemplates.getResource "postmap_command"
        setupDefaultDebug()
        runDistributionSpecific()
        appendDefaultDestinations()
        deployMailname()
        deployMain()
        deployMaster()
        reconfigureFiles()
        deployStorage()
        deployDelivery()
        deployAuth()
    }

    /**
     * Setups the default debug logging.
     */
    void setupDefaultDebug() {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
        }
        if (service.debug.level == null) {
            service.debug.level = 0
        }
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
        logg.checkStorageConfig provider, this, storageName, profileName
        def config = provider.get()
        config.script = this
        config.deployStorage()
    }

    /**
     * Deploy delivery configuration.
     */
    def deployDelivery() {
        if (deliveryName == null) {
            return
        }
        def provider = deliveries["${deliveryName}.${storageName}.${profileName}"]
        logg.checkDeliveryConfig provider, this, deliveryName, storageName, profileName
        def config = provider.get()
        config.script = this
        config.deployDelivery()
    }

    /**
     * Deploy authentication configuration.
     */
    def deployAuth() {
        if (authName == null) {
            return
        }
        def provider = authentications["${authName}.${storageName}.${profileName}"]
        logg.checkAuthConfig provider, this, authName, storageName, profileName
        def config = provider.get()
        config.script = this
        config.deployAuth()
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
            myHostnameConf(),
            myOriginConf(),
            smtpdBannerConf(),
            relayhostConf(),
            inetInterfacesConf(),
            myDestinationConf(),
            masqueradeDomainsConf(),
            aliasMapsConf(),
            aliasDatabaseConf(),
            smtpTlsSecurityLevelConf(),
            smtpdTlsSecurityLevelConf(),
            smtpTlsNoteStarttlsOfferConf(),
            smtpdTlsLoglevelConf(),
            smtpdTlsReceivedHeaderConf(),
            smtpdTlsSessionCacheTimeoutConf(),
            randomSourceConf(),
            certFileConf(),
            keyFileConf(),
            caFileConf(),
            unknownLocalRecipientRejectCodeConf(),
            delayWarningTimeConf(),
            maximalQueueLifetimeConf(),
            minimalRetriesDelayConf(),
            maximumRetriesDelayConf(),
            heloTimeoutConf(),
            recipientLimitConf(),
            backOffErrorLimitConf(),
            blockingErrorLimitConf(),
            heloRestrictionsConf(),
            senderRestrictionsConf(),
            clientRestrictionsConf(),
            recipientRestrictionsConf(),
            dataRestrictionsConf(),
            heloRequiredConf(),
            delayRejectConf(),
            disableVrfyCommandConf(),
        ]
        deployConfiguration configurationTokens(), currentMainConfiguration, configuration, mainFile
    }

    def myHostnameConf() {
        def replace = mainConfigurationTemplate.getText(true, "hostnameConfig", "mail", service)
        def search = mainConfigurationTemplate.getText(true, "hostnameConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def myOriginConf() {
        def replace = mainConfigurationTemplate.getText(true, "originConfig", "mail", service)
        def search = mainConfigurationTemplate.getText(true, "originConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def smtpdBannerConf() {
        def replace = mainConfigurationTemplate.getText(true, "bannerConfig", "banner", banner)
        def search = mainConfigurationTemplate.getText(true, "bannerConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def relayhostConf() {
        def replace = mainConfigurationTemplate.getText(true, "relayhostConfig", "mail", service)
        def search = mainConfigurationTemplate.getText(true, "relayhostConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def inetInterfacesConf() {
        def replace = mainConfigurationTemplate.getText(true, "interfacesConfig", "mail", service)
        def search = mainConfigurationTemplate.getText(true, "interfacesConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def myDestinationConf() {
        def replace = mainConfigurationTemplate.getText(true, "destinationsConfig", "mail", service)
        def search = mainConfigurationTemplate.getText(true, "destinationsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def masqueradeDomainsConf() {
        def replace = mainConfigurationTemplate.getText(true, "masqueradeDomainsConfig", "mail", service)
        def search = mainConfigurationTemplate.getText(true, "masqueradeDomainsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def aliasMapsConf() {
        def replace = mainConfigurationTemplate.getText(true, "aliasMapsConfig", "file", aliasesMapsFile)
        def search = mainConfigurationTemplate.getText(true, "aliasMapsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def aliasDatabaseConf() {
        def replace = mainConfigurationTemplate.getText(true, "aliasDatabaseConfig", "file", aliasesDatabaseFile)
        def search = mainConfigurationTemplate.getText(true, "aliasDatabaseConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def smtpTlsSecurityLevelConf() {
        if (service.certificate == null) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "smtpTlsSecurityLevelConfig", "level", smtpTlsSecurityLevel)
        def search = mainConfigurationTemplate.getText(true, "smtpTlsSecurityLevelConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns the SMTP TLS security level, for example {@code "may"}, see
     * <a href="http://www.postfix.org/postconf.5.html#smtp_tls_security_level">smtp_tls_security_level [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "smtp_tls_security_level"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    String getSmtpTlsSecurityLevel() {
        profileProperty "smtp_tls_security_level", defaultProperties
    }

    def smtpdTlsSecurityLevelConf() {
        if (service.certificate == null) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "smtpdTlsSecurityLevelConfig", "level", smtpdTlsSecurityLevel)
        def search = mainConfigurationTemplate.getText(true, "smtpdTlsSecurityLevelConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns the SMTPD TLS security level, for example {@code "may"}, see
     * <a href="http://www.postfix.org/postconf.5.html#smtpd_tls_security_level">smtpd_tls_security_level [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "smtpd_tls_security_level"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    String getSmtpdTlsSecurityLevel() {
        profileProperty "smtpd_tls_security_level", defaultProperties
    }

    def smtpTlsNoteStarttlsOfferConf() {
        if (service.certificate == null) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "smtpTlsNoteStarttlsOfferConfig", "enabled", smtpTlsNoteStarttlsOffer)
        def search = mainConfigurationTemplate.getText(true, "smtpTlsNoteStarttlsOfferConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns to log the hostname of a remote SMTP server that offers STARTTLS, for example {@code "true"}, see
     * <a href="http://www.postfix.org/postconf.5.html#smtp_tls_note_starttls_offer">smtp_tls_note_starttls_offer [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "tls_note_starttls_offer"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    boolean getSmtpTlsNoteStarttlsOffer() {
        profileBooleanProperty "tls_note_starttls_offer", defaultProperties
    }

    def smtpdTlsLoglevelConf() {
        if (service.certificate == null) {
            return []
        }
        DebugLogging debug = service.debug
        def tlslevel = debug.args.tlslevel == null ? defaultTlsLevel : debug.args.tlslevel
        def replace = mainConfigurationTemplate.getText(true, "smtpdTlsLoglevelConfig", "level", tlslevel)
        def search = mainConfigurationTemplate.getText(true, "smtpdTlsLoglevelConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns default TLS logging level, for example {@code "0"}, see
     * <a href="http://www.postfix.org/postconf.5.html#smtpd_tls_loglevel">smtpd_tls_loglevel [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "default_tls_loglevel"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    int getDefaultTlsLevel() {
        profileNumberProperty "default_tls_loglevel", defaultProperties
    }

    def smtpdTlsReceivedHeaderConf() {
        if (service.certificate == null) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "smtpdTlsReceivedHeaderConfig", "enabled", smtpdTlsReceivedHeader)
        def search = mainConfigurationTemplate.getText(true, "smtpdTlsReceivedHeaderConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns that the Postfix SMTP server produces Received: message headers, for example {@code "true"}, see
     * <a href="http://www.postfix.org/postconf.5.html#smtpd_tls_received_header">smtpd_tls_received_header [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "tls_received_header"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    boolean getSmtpdTlsReceivedHeader() {
        profileBooleanProperty "tls_received_header", defaultProperties
    }

    def smtpdTlsSessionCacheTimeoutConf() {
        if (service.certificate == null) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "smtpdTlsSessionCacheTimeoutConfig", "time", smtpdTlsSessionCacheTimeout.standardSeconds)
        def search = mainConfigurationTemplate.getText(true, "smtpdTlsSessionCacheTimeoutConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns the expiration time of Postfix SMTP server TLS session cache information, for example {@code "PT1H"}, see
     * <a href="http://www.postfix.org/postconf.5.html#smtpd_tls_session_cache_timeout">smtpd_tls_session_cache_timeout [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "tls_session_cache_timeout"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    Duration getSmtpdTlsSessionCacheTimeout() {
        profileDurationProperty "tls_session_cache_timeout", defaultProperties
    }

    def randomSourceConf() {
        if (service.certificate == null) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "randomSourceConfig", "source", randomSource)
        def search = mainConfigurationTemplate.getText(true, "randomSourceConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns the external entropy source for the pseudo random number generator pool, for example {@code "dev:/dev/urandom"}, see
     * <a href="http://www.postfix.org/postconf.5.html#tls_random_source">tls_random_source [postfix.org].</a>
     *
     * <ul>
     * <li>profile property {@code "random_source"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    String getRandomSource() {
        profileProperty "random_source", defaultProperties
    }

    def certFileConf() {
        def file = deployCertFile(service)
        if (!file) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "certFileConfig", "file", file)
        def search = mainConfigurationTemplate.getText(true, "certFileConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Deploys the certificate file to the server.
     *
     * @param service
     *            the {@link MailService} service.
     *
     * @return the certificate {@code File} or {@code null}.
     */
    File deployCertFile(MailService service) {
        Certificate cert = service.certificate
        File file = null
        if (cert == null) {
            return file
        }
        def name = FilenameUtils.getName cert.cert.path
        file = new File(certsDir, name)
        FileUtils.copyURLToFile cert.cert.toURL(), file
        changeFileOwnerFactory.create(
                log: log, owner: rootUser, ownerGroup: rootGroup, files: [file],
                command: chownCommand, this, threads)()
        changeFileModFactory.create(
                log: log, mod: "o-rw", files: [file],
                command: chmodCommand, this, threads)()
        return file
    }

    def keyFileConf() {
        def file = deployCertKeyFile(service)
        if (!file) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "keyFileConfig", "file", file)
        def search = mainConfigurationTemplate.getText(true, "keyFileConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Deploys the certificate key file to the server.
     *
     * @param service
     *            the {@link MailService} service.
     *
     * @return the certificate {@code File} or {@code null}.
     */
    File deployCertKeyFile(MailService service) {
        Certificate cert = service.certificate
        File file = null
        if (cert == null) {
            return file
        }
        def name = FilenameUtils.getName cert.key.path
        file = new File(certKeysDir, name)
        FileUtils.copyURLToFile cert.key.toURL(), file
        changeFileOwnerFactory.create(
                log: log, owner: rootUser, ownerGroup: rootGroup, files: [file],
                command: chownCommand, this, threads)()
        changeFileModFactory.create(
                log: log, mod: "o-rw", files: [file],
                command: chmodCommand, this, threads)()
        return file
    }

    def caFileConf() {
        def file = deployCertCaFile(service)
        if (!file) {
            return []
        }
        def replace = mainConfigurationTemplate.getText(true, "caFileConfig", "file", file)
        def search = mainConfigurationTemplate.getText(true, "caFileConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Deploys the certificate CA file to the server.
     *
     * @param service
     *            the {@link MailService} service.
     *
     * @return the certificate {@code File} or {@code null}.
     */
    File deployCertCaFile(MailService service) {
        Certificate cert = service.certificate
        File file = null
        if (cert == null || cert.ca == null) {
            return file
        }
        def name = FilenameUtils.getName cert.ca.path
        file = new File(certsDir, name)
        FileUtils.copyURLToFile cert.ca.toURL(), file
        changeFileOwnerFactory.create(
                log: log, owner: rootUser, ownerGroup: rootGroup, files: [file],
                command: chownCommand, this, threads)()
        changeFileModFactory.create(
                log: log, mod: "o-rw", files: [file],
                command: chmodCommand, this, threads)()
        return file
    }

    /**
     * Deploys the certificate PEM file to the server.
     *
     * @param service
     *            the {@link MailService} service.
     *
     * @return the certificate {@code File} or {@code null}.
     */
    File deployCertPemFile(MailService service) {
        Certificate cert = service.certificate
        File file = null
        if (cert == null || cert.pem == null) {
            return file
        }
        def name = FilenameUtils.getName cert.pem.path
        file = new File(certsDir, name)
        FileUtils.copyURLToFile cert.pem.toURL(), file
        changeFileOwnerFactory.create(
                log: log, owner: rootUser, ownerGroup: rootGroup, files: [file],
                command: chownCommand, this, threads)()
        changeFileModFactory.create(
                log: log, mod: "o-rw", files: [file],
                command: chmodCommand, this, threads)()
        return file
    }

    /**
     * Returns the certification files directory, for
     * example {@code "/etc/ssl/certs".}
     *
     * <ul>
     * <li>profile property {@code "certificates_directory"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    File getCertsDir() {
        profileDirProperty "certificates_directory", defaultProperties
    }

    /**
     * Returns the certification keys files directory, for
     * example {@code "/etc/ssl/private".}
     *
     * <ul>
     * <li>profile property {@code "certificates_keys_directory"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    File getCertKeysDir() {
        profileDirProperty "certificates_keys_directory", defaultProperties
    }

    def unknownLocalRecipientRejectCodeConf() {
        def replace = mainConfigurationTemplate.getText(true, "unknownLocalRecipientRejectCodeConfig", "code", unknownLocalRecipientRejectCode)
        def search = mainConfigurationTemplate.getText(true, "unknownLocalRecipientRejectCodeConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def delayWarningTimeConf() {
        def replace = mainConfigurationTemplate.getText(true, "delayWarningTimeConfig", "time", delayWarningTime)
        def search = mainConfigurationTemplate.getText(true, "delayWarningTimeConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def maximalQueueLifetimeConf() {
        def replace = mainConfigurationTemplate.getText(true, "maximalQueueLifetimeConfig", "time", maximalQueueLifetime)
        def search = mainConfigurationTemplate.getText(true, "maximalQueueLifetimeConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def minimalRetriesDelayConf() {
        def replace = mainConfigurationTemplate.getText(true, "minimalRetriesDelayConfig", "time", minimalRetriesDelay)
        def search = mainConfigurationTemplate.getText(true, "minimalRetriesDelayConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def maximumRetriesDelayConf() {
        def replace = mainConfigurationTemplate.getText(true, "maximumRetriesDelayConfig", "time", maximalRetriesDelay)
        def search = mainConfigurationTemplate.getText(true, "maximumRetriesDelayConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def heloTimeoutConf() {
        def replace = mainConfigurationTemplate.getText(true, "heloTimeoutConfig", "time", heloTimeout)
        def search = mainConfigurationTemplate.getText(true, "heloTimeoutConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def recipientLimitConf() {
        def replace = mainConfigurationTemplate.getText(true, "recipientLimitConfig", "limit", recipientLimit)
        def search = mainConfigurationTemplate.getText(true, "recipientLimitConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def backOffErrorLimitConf() {
        def replace = mainConfigurationTemplate.getText(true, "backOffErrorLimitConfig", "limit", backOffErrorLimit)
        def search = mainConfigurationTemplate.getText(true, "backOffErrorLimitConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def blockingErrorLimitConf() {
        def replace = mainConfigurationTemplate.getText(true, "blockingErrorLimitConfig", "limit", blockingErrorLimit)
        def search = mainConfigurationTemplate.getText(true, "blockingErrorLimitConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def heloRestrictionsConf() {
        def replace = mainConfigurationTemplate.getText(true, "heloRestrictionsConfig", "restrictions", heloRestrictions)
        def search = mainConfigurationTemplate.getText(true, "heloRestrictionsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def senderRestrictionsConf() {
        def replace = mainConfigurationTemplate.getText(true, "senderRestrictionsConfig", "restrictions", senderRestrictions)
        def search = mainConfigurationTemplate.getText(true, "senderRestrictionsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def clientRestrictionsConf() {
        def replace = mainConfigurationTemplate.getText(true, "clientRestrictionsConfig", "restrictions", clientRestrictions)
        def search = mainConfigurationTemplate.getText(true, "clientRestrictionsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def recipientRestrictionsConf() {
        def replace = mainConfigurationTemplate.getText(true, "recipientRestrictionsConfig", "restrictions", recipientRestrictions)
        def search = mainConfigurationTemplate.getText(true, "recipientRestrictionsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def dataRestrictionsConf() {
        def replace = mainConfigurationTemplate.getText(true, "dataRestrictionsConfig", "restrictions", dataRestrictions)
        def search = mainConfigurationTemplate.getText(true, "dataRestrictionsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def heloRequiredConf() {
        def replace = mainConfigurationTemplate.getText(true, "heloRequiredConfig", "flag", heloRequired)
        def search = mainConfigurationTemplate.getText(true, "heloRequiredConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def delayRejectConf() {
        def replace = mainConfigurationTemplate.getText(true, "delayRejectConfig", "flag", delayReject)
        def search = mainConfigurationTemplate.getText(true, "delayRejectConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    def disableVrfyCommandConf() {
        def replace = mainConfigurationTemplate.getText(true, "disableVrfyCommandConfig", "flag", disableVrfyCommand)
        def search = mainConfigurationTemplate.getText(true, "disableVrfyCommandConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Sets the main configuration of the mail server. This is done usually in
     * the {@code /etc/postfix/main.cf} file.
     *
     * @see #getMasterFile()
     */
    void deployMaster() {
        def configuration = [
            submissionConf(),
            smtpsConf(),
        ]
        deployConfiguration configurationTokens(), currentMasterConfiguration, configuration, masterFile
    }

    /**
     * Returns the current {@code master.cf} configuration. This is usually the
     * configuration file {@code /etc/postfix/master.cf}.
     *
     * @see #getMasterFile()
     */
    String getCurrentMasterConfiguration() {
        currentConfiguration masterFile
    }

    def submissionConf() {
        boolean enabled = service.certificate != null && submissionEnabled
        def replace = masterConfigurationTemplate.getText(true, "submissionConfig", "enabled", enabled)
        def search = masterConfigurationTemplate.getText(true, "submissionConfig_search")
        new TokenTemplate(search, replace, enclose: false, escape: false)
    }

    /**
     * Returns to enable submission port, for
     * example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "submission_enabled"}</li>
     * </ul>
     *
     * @see #getPostfixProperties()
     */
    boolean getSubmissionEnabled() {
        profileBooleanProperty "submission_enabled", defaultProperties
    }

    def smtpsConf() {
        boolean enabled = service.certificate != null
        def replace = masterConfigurationTemplate.getText(true, "smtpsConfig", "enabled", enabled)
        def search = masterConfigurationTemplate.getText(true, "smtpsConfig_search")
        new TokenTemplate(search, replace, enclose: false, escape: false)
    }

    /**
     * Returns the storage method for domains and users.
     *
     * <ul>
     * <li>profile property {@code "storage"}</li>
     * </ul>
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
     */
    String getDeliveryName() {
        containsKey("delivery") ? profileProperty("delivery") : null
    }

    /**
     * Returns the delivery method.
     *
     * <ul>
     * <li>profile property {@code "auth"}</li>
     * </ul>
     */
    String getAuthName() {
        containsKey("auth") ? profileProperty("auth") : null
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
     * @see #getMailnameConfigurationDir()
     * @see #getDefaultProperties()
     */
    File getMailnameFile() {
        profileFileProperty "mailname_file", mailnameConfigurationDir, defaultProperties
    }

    /**
     * Returns the absolute path of the mail name configuration directory.
     *
     * <ul>
     * <li>property {@code "mailname_configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getMailnameConfigurationDir() {
        profileDirProperty "mailname_configuration_directory", defaultProperties
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
        profileFileProperty "main_file", configurationDir, defaultProperties
    }

    /**
     * Returns the {@code master.cf} file. If the path is not absolute then it
     * is assume to be under the configuration directory.
     *
     * <ul>
     * <li>property {@code "master_file"}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getDefaultProperties()
     */
    File getMasterFile() {
        profileFileProperty "master_file", configurationDir, defaultProperties
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
        profileFileProperty "aliases_maps_file", configurationDir, defaultProperties
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
        profileFileProperty "aliases_database_file", configurationDir, defaultProperties
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
        profileProperty "postmap_command", defaultProperties
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
        profileProperty "postalias_command", defaultProperties
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
        profileDirProperty "mailbox_base_directory", defaultProperties
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
        profileProperty "mailbox_pattern", postfixProperties
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
        profileNumberProperty "unknown_local_recipient_reject_code", postfixProperties
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
        profileDurationProperty "delay_warning_time", postfixProperties
    }

    /**
     * Returns the time after which the message is consider as not possible
     * to deliver. Example {@code P7D}.
     *
     * <ul>
     * <li>profile property {@code "maximal_queue_lifetime"}</li>
     * </ul>
     *
     * @see Duration#parse(String)
     * @see #postfixProperties
     */
    Duration getMaximalQueueLifetime() {
        profileDurationProperty "maximal_queue_lifetime", postfixProperties
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
        profileDurationProperty "minimal_retries_delay", postfixProperties
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
        profileDurationProperty "maximal_retries_delay", postfixProperties
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
        profileDurationProperty "helo_timeout", postfixProperties
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
        profileNumberProperty "recipient_limit", postfixProperties
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
        profileNumberProperty "back_off_error_limit", postfixProperties
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
        profileNumberProperty "blocking_error_limit", postfixProperties
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
        profileProperty "helo_restrictions", postfixProperties
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
        profileProperty "sender_restrictions", postfixProperties
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
        profileProperty "client_restrictions", postfixProperties
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
        profileProperty "recipient_restrictions", postfixProperties
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
        profileProperty "data_restrictions", postfixProperties
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
        profileProperty "helo_required", postfixProperties
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
        profileProperty "delay_reject", postfixProperties
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
        profileProperty "disable_vrfy_command", postfixProperties
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
        profileProperty "virtual_group_name", defaultProperties
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
        profileProperty "virtual_name_name", defaultProperties
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
        profileNumberProperty "minimum_uid", defaultProperties
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
        profileNumberProperty "virtual_uid", defaultProperties
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
        profileNumberProperty "virtual_gid", defaultProperties
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
        def task = scriptExecFactory.create(
                log: log,
                command: postmapCommand,
                file: file,
                this, threads, postmapCommandTemplate, "postmap")()
        logg.rehashFileDone this, file, task
    }

    /**
     * Execute the {@code postalias} command on the specified file.
     *
     * @see #getPostmapCommand()
     */
    void realiasFile(File file) {
        def task = scriptExecFactory.create(
                log: log,
                command: postaliasCommand,
                file: file,
                this, threads, postaliasCommandTemplate, "postalias")()
        logg.realiasFileDone this, file, task
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

    /**
     * Returns the Postfix user, for example {@code "postfix"}.
     *
     * <ul>
     * <li>profile property {@code "postfix_user"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getPostfixUser() {
        profileProperty "postfix_user", defaultProperties
    }

    /**
     * Returns the Postfix group, for example {@code "postfix"}.
     *
     * <ul>
     * <li>profile property {@code "postfix_group"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getPostfixGroup() {
        profileProperty "postfix_group", defaultProperties
    }
}
