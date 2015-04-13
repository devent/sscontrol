/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core

import com.anrisoftware.sscontrol.httpd.redmine.RedmineService

/**
 * Setups email settings based on the {@link DeliveryMethod}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class SetupEmailSettingsMap {

    /**
     * Setups the email settings.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @param emaildeliveryMap
     *            the email {@link Map} settings.
     */
    abstract void setup(RedmineService service, Map emaildeliveryMap)

    static Map lazyGetMap(Map map, String name) {
        Map pmap = map[name]
        if (pmap == null) {
            map[name] = [:]
            pmap = map[name]
        }
        return pmap
    }
}

/**
 * Setups {@link DeliveryMethod#smtp} email settings.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SmtpSetupEmailSettingsMap extends SetupEmailSettingsMap {

    void setup(RedmineService service, Map emaildeliveryMap) {
        def settings = lazyGetMap emaildeliveryMap, settingsMapName
        setupSettings service, settings
    }

    String getSettingsMapName() {
        "smtp_settings"
    }

    void setupSettings(RedmineService service, Map settings) {
        def mail = service.mail
        if (mail.host != null) {
            settings.address = mail.host
        }
        if (mail.port != null) {
            settings.port = mail.port
        }
        if (mail.method != null) {
            settings.delivery_method = mail.method
        }
        if (mail.domain != null) {
            settings.domain = mail.domain
        }
        if (mail.auth != null) {
            settings.authentication = mail.auth
        }
        if (mail.user != null) {
            settings.user_name = mail.user
        }
        if (mail.password != null) {
            settings.password = mail.password
        }
        if (mail.ssl != null) {
            settings.ssl = mail.ssl
        }
        if (mail.startTlsAuto != null) {
            settings.enable_starttls_auto = mail.startTlsAuto
        }
        if (mail.opensslVerifyMode != null) {
            settings.openssl_verify_mode = mail.opensslVerifyMode
        }
    }
}

/**
 * Setups {@link DeliveryMethod#async_smtp} email settings.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AsyncSmtpSetupEmailSettingsMap extends SmtpSetupEmailSettingsMap {

    String getSettingsMapName() {
        "async_smtp_settings"
    }
}

/**
 * Setups {@link DeliveryMethod#sendmail} email settings.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SendmailSetupEmailSettingsMap extends SetupEmailSettingsMap {

    void setup(RedmineService service, Map emaildeliveryMap) {
        def settings = lazyGetMap emaildeliveryMap, settingsMapName
        setupSettings service, settings
    }

    String getSettingsMapName() {
        "sendmail_settings"
    }

    void setupSettings(RedmineService service, Map settings) {
        if (service.mail.arguments) {
            settings.arguments = service.mail.arguments
        }
    }
}

/**
 * Setups {@link DeliveryMethod#async_sendmail} email settings.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AsyncSendmailSetupEmailSettingsMap extends SendmailSetupEmailSettingsMap {

    String getSettingsMapName() {
        "async_smtp_settings"
    }
}

