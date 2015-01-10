/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.joda.time.Duration

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.security.fail2ban.Backend
import com.anrisoftware.sscontrol.security.fail2ban.Fail2banService
import com.anrisoftware.sscontrol.security.fail2ban.Jail
import com.anrisoftware.sscontrol.security.fail2ban.Type

/**
 * <i>Fail2ban 0.8.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Fail2ban_0_8_Config {

    /**
     * @see ServiceConfig#getScript()
     */
    private Object script

    @Inject
    private Fail2ban_0_8_ConfigLogger log

    @Inject
    private Map<String, Fail2BanFirewallConfig> fail2BanFirewalls

    /**
     * Resource create fail2ban configuration.
     */
    TemplateResource configTemplate

    @Inject
    final setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Fail2BanScript"
        this.configTemplate = templates.getResource "config"
    }

    /**
     * Setups the default debug logging.
     *
     * @param service
     *            the {@link Fail2banService} service.
     *
     * @see #getDefaultDebugLogLevel()
     * @see #getDefaultDebugLogTarget()
     */
    void setupDefaultDebug(Fail2banService service) {
        def logLevel = defaultDebugLogLevel
        def logTarget = defaultDebugLogTarget
        if (!service.debugLogging("level") || !service.debugLogging("level")["log"]) {
            service.debug "log", level: logLevel
        }
        if (!service.debugLogging("target") || !service.debugLogging("target")["log"]) {
            service.debug "log", target: logTarget
        }
    }

    /**
     * Setups the default ignoring addresses.
     *
     * @param service
     *            the {@link Fail2banService} service.
     *
     * @see #getDefaultIgnoringAddresses()
     */
    void setupDefaultIgnoring(Fail2banService service) {
        service.jails.each { Jail jail ->
            if (!jail.ignoreAddresses) {
                jail.ignore addresses: defaultIgnoringAddresses
            }
        }
    }

    /**
     * Setups the default banning.
     *
     * @param service
     *            the {@link Fail2banService} service.
     *
     * @see #getDefaultBanningTime()
     * @see #getDefaultBanningMaxRetries()
     * @see #getDefaultBanningBackend()
     * @see #getDefaultBanningType()
     */
    void setupDefaultBanning(Fail2banService service) {
        service.jails.each { Jail jail ->
            if (!jail.banningTime) {
                jail.banning time: defaultBanningTime
            }
            if (!jail.banningRetries) {
                jail.banning retries: defaultBanningMaxRetries
            }
            if (!jail.banningBackend) {
                jail.banning backend: defaultBanningBackend
            }
            if (!jail.banningType) {
                jail.banning type: defaultBanningType
            }
        }
    }

    /**
     * Deploys the <i>Fail2ban</i> configuration.
     *
     * @param service
     *            the {@link Fail2banService} service.
     *
     * @see #getConfigFile()
     * @see #getLocalConfigFile()
     * @see #getJailConfigFile()
     * @see #getJailLocalConfigFile()
     */
    void deployConfig(Fail2banService service) {
        localConfigFile.isFile() == false ? FileUtils.copyFile(configFile, localConfigFile) : false
        jailLocalConfigFile.isFile() == false ? FileUtils.copyFile(jailConfigFile, jailLocalConfigFile) : false
        def file = localConfigFile
        def config = currentConfiguration file
        def configs = [
            debugLggingConfigs(service)
        ]
        deployConfiguration configurationTokens(), config, configs, file
    }

    /**
     * Logging configurations.
     *
     * @param service
     *            the {@link Fail2banService} service.
     */
    List debugLggingConfigs(Fail2banService service) {
        def logLevel = service.debugLogging("level")["log"]
        def logTarget = service.debugLogging("target")["log"]
        [
            new TokenTemplate(configTemplate.getText(true, "loglevelConfig_search"), configTemplate.getText(true, "loglevelConfig", "level", logLevel)),
            new TokenTemplate(configTemplate.getText(true, "logtargetConfig_search"), configTemplate.getText(true, "logtargetConfig", "target", logTarget)),
        ]
    }

    /**
     * Deploys the firewall script.
     *
     * @param service
     *            the {@link Fail2banService} service.
     */
    void deployFirewallScript(Fail2banService service) {
        def name = firewall
        Fail2BanFirewallConfig firewall = fail2BanFirewalls[name]
        log.checkFirewallScript this, firewall, name
        firewall.setScript this
        firewall.beforeConfiguration service
        service.jails.each { Jail jail ->
            firewall.deployFirewallScript service, jail
        }
    }

    /**
     * Returns the Firewall to use, for example {@code "ufw"}.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_firewall"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    String getFirewall() {
        profileProperty "fail2ban_firewall", fail2banProperties
    }

    /**
     * Returns the configuration file, for
     * example {@code "fail2ban.conf".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_configuration_file"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     * @see #getConfigurationDir()
     */
    File getConfigFile() {
        profileFileProperty "fail2ban_configuration_file", configurationDir, fail2banProperties
    }

    /**
     * Returns the configuration directory, for
     * example {@code "/etc/fail2ban"}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_configuration_directory"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     * @see #getConfigurationDir()
     */
    File getConfigurationDir() {
        profileDirProperty "fail2ban_configuration_directory", fail2banProperties
    }

    /**
     * Returns the local configuration file, for
     * example {@code "fail2ban.local".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_local_configuration_file"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     * @see #getConfigurationDir()
     */
    File getLocalConfigFile() {
        profileFileProperty "fail2ban_local_configuration_file", configurationDir, fail2banProperties
    }

    /**
     * Returns the jail configuration file, for
     * example {@code "jail.conf".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_jail_configuration_file"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     * @see #getConfigurationDir()
     */
    File getJailConfigFile() {
        profileFileProperty "fail2ban_jail_configuration_file", configurationDir, fail2banProperties
    }

    /**
     * Returns the local jail configuration file, for
     * example {@code "jail.local".} If the file path is not absolute
     * then the file is assumed to be under the configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_jail_local_configuration_file"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     * @see #getConfigurationDir()
     */
    File getJailLocalConfigFile() {
        profileFileProperty "fail2ban_jail_local_configuration_file", configurationDir, fail2banProperties
    }

    /**
     * Returns the default logging level, for example {@code 0}.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_debug_log_level"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    int getDefaultDebugLogLevel() {
        profileNumberProperty "fail2ban_default_debug_log_level", fail2banProperties
    }

    /**
     * Returns the default logging target, for
     * example {@code "/var/log/fail2ban.log"}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_debug_log_target"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    String getDefaultDebugLogTarget() {
        profileProperty "fail2ban_default_debug_log_target", fail2banProperties
    }

    /**
     * Returns list of ignored addresses, for example {@code "127.0.0.1".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_ignoring_addresses"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    List getDefaultIgnoringAddresses() {
        profileListProperty "fail2ban_default_ignoring_addresses", fail2banProperties
    }

    /**
     * Returns banning time duration, for example {@code "PT10M"} for
     * 10 minutes.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_banning_time"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    Duration getDefaultBanningTime() {
        profileDurationProperty "fail2ban_default_banning_time", fail2banProperties
    }

    /**
     * Returns the maximum retries, for example {@code "3".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_banning_max_retries"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    int getDefaultBanningMaxRetries() {
        profileNumberProperty "fail2ban_default_banning_max_retries", fail2banProperties
    }

    /**
     * Returns the back-end name, for example {@code "polling".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_banning_backend"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    Backend getDefaultBanningBackend() {
        def backend = profileProperty "fail2ban_default_banning_backend", fail2banProperties
        if (backend instanceof Backend) {
            return backend
        } else {
            return Backend.valueOf(backend)
        }
    }

    /**
     * Returns the block type name, for example {@code "reject", "deny".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_banning_type"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    Type getDefaultBanningType() {
        def type = profileProperty "fail2ban_default_banning_type", fail2banProperties
        if (type instanceof Type) {
            return type
        } else {
            return Type.valueOf(type)
        }
    }

    /**
     * Returns the back-end name, for example {@code "root@localhost".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_default_notify_email"}</li>
     * </ul>
     *
     * @see #getFail2banProperties()
     */
    String getDefaultNotifyEmail() {
        profileProperty "fail2ban_default_notify_email", fail2banProperties
    }

    /**
     * Returns the default <i>Fail2ban</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFail2banProperties()

    /**
     * Returns the <i>Fail2ban</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * @see ServiceConfig#getScript()
     */
    Object getScript() {
        script
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
