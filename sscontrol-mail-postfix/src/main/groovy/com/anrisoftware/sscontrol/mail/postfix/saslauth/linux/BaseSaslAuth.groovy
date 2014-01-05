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
package com.anrisoftware.sscontrol.mail.postfix.saslauth.linux

import java.util.regex.Pattern

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.mail.postfix.linux.AuthConfig
import com.anrisoftware.sscontrol.mail.postfix.script.linux.BaseAuth
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * SASL/authentication.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseSaslAuth extends BaseAuth implements AuthConfig {

    public static final String NAME = "sasl"

    @Inject
    private BaseSaslAuthLogger log

    /**
     * SASL/authentication templates.
     */
    Templates salsTemplates

    /**
     * The {@code "main.cf"} configuration templates.
     */
    TemplateResource mainSaslConfTemplate

    /**
     * The {@code "saslauthd"} configuration templates.
     */
    TemplateResource saslAuthConfTemplate

    @Override
    void deployAuth() {
        salsTemplates = templatesFactory.create "BaseSalsAuth"
        mainSaslConfTemplate = salsTemplates.getResource "mainconf"
        saslAuthConfTemplate = salsTemplates.getResource "saslauthconf"
        updatePostfixUser()
        makeChrootDirectory()
        deployMain()
        deploySaslauthd()
    }

    /**
     * Update the Postfix user.
     */
    void updatePostfixUser() {
        changeUser userName: postfixUser, groups: [saslGroup]
    }

    /**
     * Returns the SASL local group, for example {@code "sasl"}.
     *
     * <ul>
     * <li>profile property {@code "sasl_group"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSaslGroup() {
        profileProperty "sasl_group", authProperties
    }

    /**
     * Returns the Postfix local user, for example {@code "postfix"}.
     *
     * <ul>
     * <li>profile property {@code "postfix_user"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getPostfixUser() {
        profileProperty "postfix_user", authProperties
    }

    /**
     * Creates the Postfix chroot {@code saslauthd} directory.
     */
    void makeChrootDirectory() {
        chrootSaslauthdDirectory.mkdirs()
        log.chrootDirectoryCreated script, chrootSaslauthdDirectory
    }

    /**
     * Returns the Postfix chroot {@code saslauthd} directory, for
     * example {@code "/var/spool/postfix/var/run/saslauthd".}
     *
     * <ul>
     * <li>profile property {@code "chroot_saslauthd_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getChrootSaslauthdDirectory() {
        profileDirProperty "chroot_saslauthd_directory", authProperties
    }

    /**
     * Configures the Postfix {@code main.cf} configuration.
     *
     * @see #getMainFile()
     */
    void deployMain() {
        def configuration = []
        configuration << saslAuthEnableConf()
        configuration << allowBrokenSaslAuthClientsConf()
        configuration << saslSecurityOptionsConf()
        configuration << saslLocalDomainConf()
        deployConfiguration configurationTokens(), script.currentMainConfiguration, configuration, script.mainFile
    }

    def saslAuthEnableConf() {
        def replace = mainSaslConfTemplate.getText(true, "authEnableConfig", "enabled", saslAuthEnabled)
        def search = mainSaslConfTemplate.getText(true, "authEnableConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns that SASL authentication is enabled, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "sasl_auth_enabled"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getSaslAuthEnabled() {
        profileBooleanProperty "sasl_auth_enabled", script.defaultProperties
    }

    def allowBrokenSaslAuthClientsConf() {
        def replace = mainSaslConfTemplate.getText(true, "allowBrokenSaslAuthClientsConfig", "enabled", allowBrokenSaslAuthClients)
        def search = mainSaslConfTemplate.getText(true, "allowBrokenSaslAuthClientsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns that allow broken SASL authentication is enabled, for
     * example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "allow_broken_sasl_auth_clients"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    boolean getAllowBrokenSaslAuthClients() {
        profileBooleanProperty "allow_broken_sasl_auth_clients", authProperties
    }

    def saslSecurityOptionsConf() {
        def replace = mainSaslConfTemplate.getText(true, "saslSecurityOptionsConfig", "options", saslSecurityOptions)
        def search = mainSaslConfTemplate.getText(true, "saslSecurityOptionsConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns SASL authentication security options, for
     * example {@code "noanonymous"}.
     *
     * <ul>
     * <li>profile property {@code "sasl_security_options"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    List getSaslSecurityOptions() {
        profileListProperty "sasl_security_options", authProperties
    }

    def saslLocalDomainConf() {
        def replace = mainSaslConfTemplate.getText(true, "saslLocalDomainConfig", "config", saslLocalDomain)
        def search = mainSaslConfTemplate.getText(true, "saslLocalDomainConfig_search")
        new TokenTemplate(search, replace, escape: false)
    }

    /**
     * Returns SASL authentication local domain, for
     * example {@code ""}.
     *
     * <ul>
     * <li>profile property {@code "sasl_local_domain"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    List getSaslLocalDomain() {
        profileListProperty "sasl_local_domain", defaultProperties
    }

    /**
     * Configures the SASL {@code saslauthd} configuration.
     *
     * @see #getSaslauthdFile()
     */
    void deploySaslauthd() {
        def configuration = []
        configuration << saslStartEnableConf()
        configuration << saslOptionsConf()
        def config = currentConfiguration saslauthdFile
        deployConfiguration configurationTokens(), config, configuration, saslauthdFile
    }

    /**
     * Returns the SASL {@code "saslauthd"} file, for
     * example {@code "/etc/default/saslauthd"}.
     *
     * <ul>
     * <li>profile property {@code "sasl_authd_file"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    File getSaslauthdFile() {
        profileProperty("sasl_authd_file", authProperties) as File
    }

    def saslStartEnableConf() {
        def replace = saslAuthConfTemplate.getText(true, "saslStartEnableConfig", "enabled", saslAuthEnabled)
        def search = saslAuthConfTemplate.getText(true, "saslStartEnableConfig_search")
        new TokenTemplate(search, replace)
    }

    def saslOptionsConf() {
        def replace = saslAuthConfTemplate.getText(true, "saslOptionsConfig", "options", saslOptions)
        def search = saslAuthConfTemplate.getText(true, "saslOptionsConfig_search")
        new TokenTemplate(search, replace, flags: Pattern.MULTILINE)
    }

    /**
     * Returns the SASL options, for
     * example {@code "-r -c -m /var/spool/postfix/var/run/saslauthd"}.
     *
     * <ul>
     * <li>profile property {@code "sasl_options"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    String getSaslOptions() {
        profileProperty "sasl_options", authProperties
    }

    @Override
    String getAuthName() {
        NAME
    }

    /**
     * Returns the authentication properties.
     */
    abstract ContextProperties getAuthProperties()
}
