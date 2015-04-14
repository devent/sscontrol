/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin.ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.security.spamassassin.SpamassassinService
import com.anrisoftware.sscontrol.security.spamassassin.linux.Spamassassin_3_Config

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

    @Inject
    InstallPackagesFactory installPackagesFactory

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
     *            the {@link SpamassassinService} service.
     *
     * @see #getSpamassassinDefaultsFile()
     */
    void deploySpamassassinDefaultConfig(SpamassassinService service) {
        def configs = [
            configToken(defaultsConfigTemplate, "enabledConfig", "enabled", enableSpamassassin),
            configToken(defaultsConfigTemplate, "cronEnabledConfig", "enabled", enableUpdateRules),
            configToken(defaultsConfigTemplate, "optionsConfig", "options", spamassassinOptions),
        ]
        def file = spamassassinDefaultsFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, configs, file
    }

    /**
     * Restarts the <i>Spamassassin</i> service.
     *
     * @param service
     *            the {@link SpamassassinService}.
     *
     * @see #getSpamassassinRestartCommand()
     * @see #getSpamassassinRestartServices()
     * @see #getSpamassassinRestartFlags()
     */
    void restartSpamassassin(SpamassassinService service) {
        def task = restartServicesFactory.create(
                log: log,
                command: spamassassinRestartCommand,
                services: spamassassinRestartServices,
                flags: spamassassinRestartFlags,
                this, threads)()
    }

    /**
     * Installs the <i>Spamassassin</i> packages.
     *
     * @see #getSpamassassinPackages()
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: spamassassinPackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Returns the <i>Spamassassin</i> restart command, for
     * example {@code "/etc/init.d/spamassassin"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_restart_command"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    String getSpamassassinRestartCommand() {
        profileProperty "spamassassin_restart_command", spamassassinProperties
    }

    /**
     * Returns the <i>Spamassassin</i> restart services, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_restart_services"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    List getSpamassassinRestartServices() {
        profileListProperty "spamassassin_restart_services", spamassassinProperties
    }

    /**
     * Returns the <i>Spamassassin</i> restart flags, for
     * example {@code "restart"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_restart_flags"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    String getSpamassassinRestartFlags() {
        profileProperty "spamassassin_restart_flags", spamassassinProperties
    }

    /**
     * Returns the {@code Spamassassin} service packages, for
     * example {@code "spamassassin"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_packages"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    List getSpamassassinPackages() {
        profileListProperty "spamassassin_packages", spamassassinProperties
    }

    /**
     * Returns enabled the <i>Spamassassin</i> service, for
     * example {@code "true"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_enable"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    boolean getEnableSpamassassin() {
        profileBooleanProperty "spamassassin_enable", spamassassinProperties
    }

    /**
     * Returns enabled the nightly update of <i>Spamassassin</i> rules, for
     * example {@code "true"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_update_rules_enable"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    boolean getEnableUpdateRules() {
        profileBooleanProperty "spamassassin_update_rules_enable", spamassassinProperties
    }

    /**
     * Returns the <i>Spamassassin</i> daemon options, for
     * example {@code "--create-prefs --max-children 5 --helper-home-dir"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_options"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    List getSpamassassinOptions() {
        profileListProperty "spamassassin_options", spamassassinProperties
    }

    /**
     * Returns the <i>Spamassassin</i> defaults file, for
     * example {@code "/etc/default/spamassassin"}
     *
     * <ul>
     * <li>profile property {@code "spamassassin_defaults_file"}</li>
     * </ul>
     *
     * @see #getSpamassassinProperties()
     */
    File getSpamassassinDefaultsFile() {
        profileProperty("spamassassin_defaults_file", spamassassinProperties) as File
    }
}
