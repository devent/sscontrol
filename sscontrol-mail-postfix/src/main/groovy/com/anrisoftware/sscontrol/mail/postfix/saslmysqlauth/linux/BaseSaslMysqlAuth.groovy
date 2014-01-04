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
package com.anrisoftware.sscontrol.mail.postfix.saslmysqlauth.linux

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.mail.api.MailService
import com.anrisoftware.sscontrol.mail.postfix.saslauth.linux.BaseSaslAuth

/**
 * SASL/MySQL/authentication.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseSaslMysqlAuth extends BaseSaslAuth {

    public static final String STORAGE_NAME = "mysql"

    /**
     * SASL/authentication templates.
     */
    Templates salsMysqlTemplates

    /**
     * The {@code "smtpd.conf"} configuration templates.
     */
    TemplateResource smtpdConfTemplate

    @Override
    void deployAuth() {
        super.deployAuth()
        salsMysqlTemplates = templatesFactory.create "BaseSaslMysqlAuth"
        smtpdConfTemplate = salsMysqlTemplates.getResource "smtpdconfig"
        deploySmtpd()
        deploySmtpdPam()
    }

    /**
     * Configures the Postfix SASL {@code smtpd.conf} configuration.
     *
     * @see #getSaslSmtpdFile()
     */
    void deploySmtpd() {
        def config = smtpdConfTemplate.getText(
                true, "saslSmtpdConfig",
                "properties", this,
                "service", script.service)
        FileUtils.write saslSmtpdFile, config, charset
    }

    /**
     * Returns the Postfix SASL SMTPD {@code "smtpd.conf"} file, for
     * example {@code "/etc/postfix/sasl/smtpd.conf"}.
     *
     * <ul>
     * <li>profile property {@code "sasl_smtpd_file"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    File getSaslSmtpdFile() {
        profileProperty("sasl_smtpd_file", authProperties) as File
    }

    /**
     * Returns the mech list for SMTPD, for
     * example {@code "plain, login, cram-md5, digest-md5".}
     *
     * @see #getAuthProperties()
     */
    List getSaslMechList() {
        script.profileListProperty "sasl_mech_list", authProperties
    }

    /**
     * Returns SASL debug logging level.
     *
     * @see #getAuthProperties()
     */
    int getSaslLogLevel() {
        MailService service = script.service
        def level = service.debug.args.sasllevel
        level != null ? level : script.profileNumberProperty("sasl_debug_level", authProperties)
    }

    /**
     * Returns to allow plain text.
     *
     * @see #getAuthProperties()
     */
    boolean getSaslAllowPlaintext() {
        script.profileBooleanProperty "sasl_allow_plaintext", authProperties
    }

    /**
     * Configures the SMTPD PAM {@code "/etc/pam.d/smtp"} configuration.
     *
     * @see #getSaslSmtpdFile()
     */
    void deploySmtpdPam() {
        def config = smtpdConfTemplate.getText(
                true, "smtpdPamConfig",
                "properties", this,
                "service", script.service)
        FileUtils.write smtpdPamFile, config, charset
    }

    /**
     * Returns the PAM SMTPD {@code "/etc/pam.d/smtp"} file.
     *
     * <ul>
     * <li>profile property {@code "smtpd_pam_file"}</li>
     * </ul>
     *
     * @see #getAuthProperties()
     */
    File getSmtpdPamFile() {
        profileProperty("smtpd_pam_file", authProperties) as File
    }

    /**
     * Returns the MySQL field of the users table name, for
     * example {@code "users"}.
     *
     * @see #getAuthProperties()
     */
    String getUsersTable() {
        script.profileProperty "users_table", authProperties
    }

    /**
     * Returns the MySQL field of the log-in field, for
     * example {@code "login"}.
     *
     * @see #getAuthProperties()
     */
    String getLoginField() {
        script.profileProperty "login_field", authProperties
    }

    /**
     * Returns the MySQL field of the crypt password field, for
     * example {@code "crypt"}.
     *
     * @see #getAuthProperties()
     */
    String getCryptField() {
        script.profileProperty "crypt_field", authProperties
    }

    /**
     * Returns the MySQL field of the enabled password field, for
     * example {@code "enabled"}.
     *
     * @see #getAuthProperties()
     */
    String getEnabledField() {
        script.profileProperty "enabled_field", authProperties
    }

    @Override
    String getStorageName() {
        STORAGE_NAME
    }
}
