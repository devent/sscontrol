/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.spamassassin_ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.citadel.spamassassin.Spamassassin_3_Config
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Spamassassin 3.x Ubuntu</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Spamassassin_3_Ubuntu_Config extends Spamassassin_3_Config {

    @Inject
    RestartServicesFactory restartServicesFactory

    TemplateResource defaultsConfigTemplate

    @Inject
    final void setUbuntuTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Spamassassin_3_Ubuntu_Config"
        this.defaultsConfigTemplate = templates.getResource "defaults_config"
    }

    /**
     * Deploys the <i>Spamassassin</i> defaults configuration.
     *
     * @param service
     *            the {@link CitadelService} service.
     *
     * @see #getSpamassassinDefaultsFile()
     * @see #getCitadelProperties()
     */
    void deploySpamassassinDefaultConfig(CitadelService service) {
        def configs = [
            configToken(defaultsConfigTemplate, "spamassassinEnabledConfig", "enabled", enableSpamassassin),
            configToken(defaultsConfigTemplate, "spamassassinCronEnabledConfig", "enabled", enableUpdateSpamassassinRules),
        ]
        def file = spamassassinDefaultsFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    /**
     * Restarts the <i>Spamassassin</i> service.
     *
     * @param service
     *            the {@link CitadelService}.
     *
     * @see #getCitadelProperties()
     */
    void restartSpamassassin(CitadelService service) {
        def task = restartServicesFactory.create(
                log: log,
                command: spamassassinRestartCommand,
                services: [],
                flags: spamassassinRestartFlags,
                this, threads)()
    }

    /**
     * Returns enabled the <i>Spamassassin</i> service, for
     * example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_enable"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    boolean getEnableSpamassassin() {
        profileBooleanProperty "spamassassin_enable", citadelProperties
    }

    /**
     * Returns enabled the nightly update of <i>Spamassassin</i> rules, for
     * example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_update_rules_enable"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    boolean getEnableUpdateSpamassassinRules() {
        profileBooleanProperty "spamassassin_update_rules_enable", citadelProperties
    }

    /**
     * Returns the <i>Spamassassin</i> defaults file, for
     * example {@code "/etc/default/spamassassin"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_defaults_file"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    File getSpamassassinDefaultsFile() {
        profileProperty("spamassassin_defaults_file", citadelProperties) as File
    }
}
