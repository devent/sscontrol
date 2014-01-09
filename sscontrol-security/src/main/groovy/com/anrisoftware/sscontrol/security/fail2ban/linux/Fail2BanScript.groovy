/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.joda.time.Duration

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.security.banning.Backend
import com.anrisoftware.sscontrol.security.banning.BanningFactory
import com.anrisoftware.sscontrol.security.banning.Type
import com.anrisoftware.sscontrol.security.ignoring.IgnoringFactory
import com.anrisoftware.sscontrol.security.services.Service
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * fail2ban script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Fail2BanScript extends LinuxScript {

    @Inject
    private Fail2BanScriptLogger log

    @Inject
    private Map<String, Fail2BanFirewall> fail2BanFirewalls;

    @Inject
    DebugLoggingProperty debugLoggingProperty

    @Inject
    IgnoringFactory ignoringFactory

    @Inject
    BanningFactory banningFactory

    /**
     * The {@link Templates} for the script.
     */
    Templates scriptTemplates

    /**
     * Resource create fail2ban configuration.
     */
    TemplateResource configTemplate

    @Override
    def run() {
        super.run()
        this.scriptTemplates = templatesFactory.create "Fail2BanScript"
        this.configTemplate = scriptTemplates.getResource "config"
        setupDefaultLogging()
        setupDefaultIgnoring()
        setupDefaultBanning()
        beforeConfiguration()
        deployConfig()
        deployFirewallScript()
    }

    /**
     * Called before the fail2ban configuration.
     */
    void beforeConfiguration() {
    }

    /**
     * Setups the default debug logging.
     */
    void setupDefaultLogging() {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
        }
        if (!service.debug.args.containsKey("target")) {
            service.debug.args.target = defaultLoggingTarget
        }
    }

    /**
     * Returns the default logging target, for example {@code "ufw"}.
     *
     * <ul>
     * <li>profile property {@code "default_logging_target"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultLoggingTarget() {
        profileProperty "default_logging_target", defaultProperties
    }

    /**
     * Setups the default ignoring addresses.
     *
     * @see #getDefaultIgnoringAddresses()
     */
    void setupDefaultIgnoring() {
        service.services.each { Service s ->
            if (s.ignoring == null) {
                s.setIgnoring ignoringFactory.create(service, ["addresses": defaultIgnoringAddresses])
            }
        }
    }

    /**
     * Setups the default banning.
     *
     * @see #getDefaultIgnoringAddresses()
     */
    void setupDefaultBanning() {
        service.services.each { Service s ->
            if (s.banning == null) {
                def args = [:]
                args.retries = defaultMaxRetries
                args.time = defaultBanTime
                args.backend = defaultBackend
                args.type = defaultBlockType
                s.setBanning banningFactory.create(service, args)
            }
            s.banning.type == null ? s.banning.type = defaultBlockType : false
        }
    }

    /**
     * Deploys the fail2ban configuration.
     */
    void deployConfig() {
        localConfigFile.isFile() == false ? FileUtils.copyFile(configFile, localConfigFile) : false
        jailLocalConfigFile.isFile() == false ? FileUtils.copyFile(jailConfigFile, jailLocalConfigFile) : false
        def file = localConfigFile
        def config = currentConfiguration file
        deployConfiguration configurationTokens(), config, debugLggingConfigs(), file
    }

    /**
     * Logging configurations.
     */
    List debugLggingConfigs() {
        def debug = service.debug
        [
            new TokenTemplate(configTemplate.getText(true, "loglevelConfig_search"), configTemplate.getText(true, "loglevelConfig", "level", debug.level)),
            new TokenTemplate(configTemplate.getText(true, "logtargetConfig_search"), configTemplate.getText(true, "logtargetConfig", "target", debug.args.target)),
        ]
    }

    /**
     * Deploys the Firewall script.
     */
    void deployFirewallScript() {
        def name = firewall
        Fail2BanFirewall firewall = fail2BanFirewalls[name]
        log.checkFirewallScript this, firewall, name
        firewall.setScript this
        firewall.beforeConfiguration()
        service.services.each { Service s ->
            firewall.deployFirewallScript s
        }
    }

    /**
     * Returns the Firewall to use, for example {@code "ufw"}.
     *
     * <ul>
     * <li>profile property {@code "firewall"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFirewall() {
        profileProperty "firewall", defaultProperties
    }

    /**
     * Returns the configuration file, for
     * example {@code "fail2ban.conf".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getConfigFile() {
        profileFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the local configuration file, for
     * example {@code "fail2ban.local".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "local_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getLocalConfigFile() {
        profileFileProperty "local_configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the jail configuration file, for
     * example {@code "jail.conf".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "jail_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getJailConfigFile() {
        profileFileProperty "jail_configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the local jail configuration file, for
     * example {@code "jail.local".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "jail_local_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getJailLocalConfigFile() {
        profileFileProperty "jail_local_configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns list of ignored addresses, for example {@code "127.0.0.1".}
     *
     * <ul>
     * <li>profile property {@code "default_ignoring_addresses"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultIgnoringAddresses() {
        profileListProperty "default_ignoring_addresses", defaultProperties
    }

    /**
     * Returns banning time duration, for example {@code "PT10M"} for
     * 10 minutes.
     *
     * <ul>
     * <li>profile property {@code "default_ban_time"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Duration getDefaultBanTime() {
        profileDurationProperty "default_ban_time", defaultProperties
    }

    /**
     * Returns the maximum retries, for example {@code "3".}
     *
     * <ul>
     * <li>profile property {@code "default_max_retries"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getDefaultMaxRetries() {
        profileNumberProperty "default_max_retries", defaultProperties
    }

    /**
     * Returns the back-end name, for example {@code "polling".}
     *
     * <ul>
     * <li>profile property {@code "default_backend"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Backend getDefaultBackend() {
        def backend = profileProperty "default_backend", defaultProperties
        if (backend instanceof Backend) {
            return backend
        } else {
            return Backend.valueOf(backend)
        }
    }

    /**
     * Returns the back-end name, for example {@code "root@localhost".}
     *
     * <ul>
     * <li>profile property {@code "default_notify_email"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultNotifyEmail() {
        profileProperty "default_notify_email", defaultProperties
    }

    /**
     * Returns the block type name, for example {@code "reject", "deny".}
     *
     * <ul>
     * <li>profile property {@code "default_block_type"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Type getDefaultBlockType() {
        def type = profileProperty "default_block_type", defaultProperties
        if (type instanceof Type) {
            return type
        } else {
            return Type.valueOf(type)
        }
    }
}
