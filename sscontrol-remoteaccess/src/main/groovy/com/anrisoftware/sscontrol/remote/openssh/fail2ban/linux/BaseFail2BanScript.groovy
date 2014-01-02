/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.fail2ban.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.joda.time.Duration

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * fail2ban script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseFail2BanScript implements RemoteScript {

    /**
     * The {@link Templates} for the script.
     */
    Templates scriptTemplates

    /**
     * Resource create fail2ban configuration.
     */
    TemplateResource configTemplate

    @Inject
    Map<String, Fail2BanScript> fail2banScript

    LinuxScript script

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    @Override
    final void deployRemoteScript(RemoteService service) {
        if (!deployFail2ban) {
            return
        }
        setupParentScript()
        beforeFail2banConfiguration service
        deployConfig()
        deployFail2banScript service
    }

    void setupParentScript() {
        fail2banScript.each { key, Fail2BanScript value ->
            value.setScript this
        }
    }

    /**
     * Called before the fail2ban configuration.
     *
     * @param service
     *            the {@link RemoteService}
     */
    void beforeFail2banConfiguration(RemoteService service) {
    }

    /**
     * Setups the default debug logging.
     */
    void setupDefaultLogging(RemoteService service) {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
        }
        if (!service.debug.args.containsKey("storage")) {
            service.debug.args.storage = loggingStorage
        }
    }

    /**
     * Deploys the fail2ban configuration.
     */
    void deployConfig() {
        if (!fail2banLocalConfigFile.isFile()) {
            FileUtils.copyFile fail2banConfigFile, fail2banLocalConfigFile
        }
        def file = fail2banLocalConfigFile
        def config = currentConfiguration file
        def debug = debugLoggingProperty.defaultDebug this, "fail2ban_default_debug"
        deployConfiguration configurationTokens(), config, fail2banLoggingConfigs(debug), file
    }

    /**
     * Returns the logging configurations.
     */
    List fail2banLoggingConfigs(DebugLogging debug) {
        [
            new TokenTemplate(configTemplate.getText(true, "loglevelConfig_search"), configTemplate.getText(true, "loglevelConfig", "level", debug.level)),
            new TokenTemplate(configTemplate.getText(true, "logtargetConfig_search"), configTemplate.getText(true, "logtargetConfig", "target", debug.args.target)),
        ]
    }

    /**
     * Deploys the fail2ban script.
     *
     * @param service
     *            the {@link RemoteService}.
     */
    void deployFail2banScript(RemoteService service) {
        def firewall = fail2banFirewall
        fail2banScript["fail2ban.$firewall"].deployFail2banScript service
    }

    /**
     * Returns the firewall to use, for example {@code "ufw"}.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_firewall"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banFirewall() {
        profileProperty "fail2ban_firewall", defaultProperties
    }

    /**
     * Returns the list of fail2ban packages.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_packages"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getFail2banPackages() {
        profileListProperty "fail2ban_packages", defaultProperties
    }

    /**
     * Returns the fail2ban configuration directory, for
     * example {@code "/etc/fail2ban"}.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getFail2banConfigDir() {
        profileProperty("fail2ban_configuration_directory", defaultProperties) as File
    }

    /**
     * Returns the fail2ban UFW action file, for
     * example {@code "action.d/ufw.conf".} If the file path is not absolute
     * then the file is assumed to be under the fail2ban configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_ufw_action_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getFail2banUfwActionFile() {
        profileFileProperty "fail2ban_ufw_action_file", fail2banConfigDir, defaultProperties
    }

    /**
     * Returns the fail2ban configuration file, for
     * example {@code "fail2ban.conf".} If the file path is not absolute
     * then the file is assumed to be under the fail2ban configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getFail2banConfigFile() {
        profileFileProperty "fail2ban_configuration_file", fail2banConfigDir, defaultProperties
    }

    /**
     * Returns the local fail2ban configuration file, for
     * example {@code "fail2ban.local".} If the file path is not absolute
     * then the file is assumed to be under the fail2ban configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_local_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getFail2banLocalConfigFile() {
        profileFileProperty "fail2ban_local_configuration_file", fail2banConfigDir, defaultProperties
    }

    /**
     * Returns the fail2ban jail configuration file, for
     * example {@code "jail.conf".} If the file path is not absolute
     * then the file is assumed to be under the fail2ban configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_jail_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getFail2banJailConfigFile() {
        profileFileProperty "fail2ban_jail_configuration_file", fail2banConfigDir, defaultProperties
    }

    /**
     * Returns the local fail2ban jail configuration file, for
     * example {@code "jail.local".} If the file path is not absolute
     * then the file is assumed to be under the fail2ban configuration
     * directory.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_jail_local_configuration_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getFail2banJailLocalConfigFile() {
        profileFileProperty "fail2ban_jail_local_configuration_file", fail2banConfigDir, defaultProperties
    }

    /**
     * Returns list of ignored addresses, for
     * example {@code "127.0.0.1".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_ignore_addresses"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getFail2banIgnoreAddresses() {
        profileListProperty "fail2ban_ignore_addresses", defaultProperties
    }

    /**
     * Returns banning time duration, for
     * example {@code "PT10M"} for 10 minutes.
     *
     * <ul>
     * <li>profile property {@code "fail2ban_ban_time"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Duration getFail2banBanTime() {
        profileDurationProperty "fail2ban_ban_time", defaultProperties
    }

    /**
     * Returns the maximum retries, for example {@code "3".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_max_retries"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getFail2banMaxRetries() {
        profileNumberProperty "fail2ban_max_retries", defaultProperties
    }

    /**
     * Returns the back-end name, for example {@code "polling".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_backend"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banBackend() {
        profileProperty "fail2ban_backend", defaultProperties
    }

    /**
     * Returns the back-end name, for example {@code "root@localhost".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_destination_email"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banDestinationEmail() {
        profileProperty "fail2ban_destination_email", defaultProperties
    }

    /**
     * Returns the ban-action name, for example {@code "ufw".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_ban_action"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banBanAction() {
        profileProperty "fail2ban_ban_action", defaultProperties
    }

    /**
     * Returns the block type name, for example {@code "reject", "deny".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_block_type"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banBlockType() {
        profileProperty "fail2ban_block_type", defaultProperties
    }

    /**
     * Returns the fail2ban restart command, for
     * example {@code "/etc/init.d/fail2ban restart".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_restart_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banRestartCommand() {
        profileProperty "fail2ban_restart_command", defaultProperties
    }

    /**
     * Returns the list of fail2ban services to restart, for
     * example {@code "".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_restart_services"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getFail2banRestartServices() {
        profileListProperty "fail2ban_restart_services", defaultProperties
    }

    /**
     * Returns the application name to ban, for
     * example {@code "OpenSSH".}
     *
     * <ul>
     * <li>profile property {@code "fail2ban_application"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getFail2banApp() {
        profileProperty "fail2ban_application", defaultProperties
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
        this.scriptTemplates = templatesFactory.create "BaseFail2BanScript"
        this.configTemplate = scriptTemplates.getResource "config"
    }

    @Override
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
