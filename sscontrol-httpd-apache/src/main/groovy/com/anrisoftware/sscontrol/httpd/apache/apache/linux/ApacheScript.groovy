/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingProperty
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.apache.api.AuthConfig
import com.anrisoftware.sscontrol.httpd.apache.apache.api.ServiceConfig
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Uses Apache service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class ApacheScript extends LinuxScript {

    @Inject
    private ApacheScriptLogger log

    @Inject
    private DebugLoggingProperty debugLoggingProperty

    @Inject
    Map<String, ServiceConfig> serviceConfigs

    @Inject
    Map<String, AuthConfig> authConfigs

    @Override
    def run() {
        authConfigs.each { it.value.script = this }
        serviceConfigs.each { it.value.script = this }
        super.run()
        setupDefaultBinding()
        setupDefaultDebug()
        distributionSpecificConfiguration()
    }

    /**
     * Returns the profile name.
     */
    abstract String getProfileName()

    /**
     * Run the distribution specific configuration.
     */
    abstract distributionSpecificConfiguration()

    /**
     * Returns the template resource containing Apache commands.
     */
    abstract TemplateResource getApacheCommandsTemplate()

    /**
     * Setups the default binding addresses.
     */
    void setupDefaultBinding() {
        if (service.binding.size() == 0) {
            defaultBinding.each { service.binding.addAddress(it) }
        }
    }

    /**
     * Setups the default debug logging.
     */
    void setupDefaultDebug() {
        if (service.debug == null) {
            service.debug = debugLoggingProperty.defaultDebug this
        }
    }

    /**
     * Returns the restart command for the Apache service.
     *
     * <ul>
     * <li>property key {@code apache_restart_command}</li>
     * <li>property key {@code restart_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    @Override
    String getRestartCommand() {
        if (containsKey("apache_restart_command")) {
            profileProperty "apache_restart_command", defaultProperties
        } else {
            profileProperty "restart_command", defaultProperties
        }
    }

    /**
     * Returns the enable mod command {@code a2enmod}.
     *
     * <ul>
     * <li>profile property {@code "enable_mod_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getEnableModCommand() {
        profileProperty "enable_mod_command", defaultProperties
    }

    /**
     * Returns the disable mod command {@code a2dismod}.
     *
     * <ul>
     * <li>profile property {@code "disable_mod_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDisableModCommand() {
        profileProperty "disable_mod_command", defaultProperties
    }

    /**
     * Returns the disable site command {@code a2ensite}.
     *
     * <ul>
     * <li>profile property {@code "enable_site_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getEnableSiteCommand() {
        profileProperty "enable_site_command", defaultProperties
    }

    /**
     * Returns the disable site command {@code a2dissite}.
     *
     * <ul>
     * <li>profile property {@code "disable_site_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDisableSiteCommand() {
        profileProperty "disable_size_command", defaultProperties
    }

    /**
     * Returns the Apache server command {@code apache2}.
     *
     * <ul>
     * <li>profile property {@code "apache_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getApacheCommand() {
        profileProperty "apache_command", defaultProperties
    }

    /**
     * Returns the Apache control command {@code apache2ctl}.
     *
     * <ul>
     * <li>profile property {@code "apache_control_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getApacheControlCommand() {
        profileProperty "apache_control_command", defaultProperties
    }

    /**
     * Returns the command to manage user files for basic authentication
     * {@code htpasswd}.
     *
     * <ul>
     * <li>profile property {@code "htpasswd_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getHtpasswdCommand() {
        profileProperty "htpasswd_command", defaultProperties
    }

    /**
     * Returns the path of the Apache configuration directory.
     *
     * <ul>
     * <li>profile property {@code "apache_configuration_directory"}</li>
     * <li>profile property {@code "configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    @Override
    File getConfigurationDir() {
        if (containsKey("apache_configuration_directory")) {
            profileDirProperty "apache_configuration_directory", defaultProperties
        } else {
            profileDirProperty "configuration_directory", defaultProperties
        }
    }

    /**
     * Returns the directory with the available sites. If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "apache_sites_available_directory"}</li>
     * <li>profile property {@code "sites_available_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getSitesAvailableDir() {
        if (containsKey("apache_sites_available_directory")) {
            profileDirProperty "apache_sites_available_directory", defaultProperties
        } else {
            profileFileProperty "sites_available_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the directory for the included configuration. If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "apache_config_include_directory"}</li>
     * <li>profile property {@code "config_include_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getConfigIncludeDir() {
        if (containsKey("apache_config_include_directory")) {
            profileDirProperty "apache_config_include_directory", defaultProperties
        } else {
            profileFileProperty "config_include_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the path for the Apache ports configuration file, for
     * example {@code "ports.conf".}
     * If the file path is not absolute then the file returned is under the
     * configuration directory.
     *
     * <ul>
     * <li>profile property {@code "ports_config_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getPortsConfigFile() {
        profileFileProperty "ports_config_file", configurationDir, defaultProperties
    }

    /**
     * Returns the path for the Apache default configuration file.
     *
     * <ul>
     * <li>profile property {@code "default_config_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getDefaultConfigFile() {
        profileFileProperty "default_config_file", sitesAvailableDir, defaultProperties
    }

    /**
     * Returns the path for the Apache virtual domains configuration file.
     *
     * <ul>
     * <li>profile property {@code "domains_config_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getDomainsConfigFile() {
        profileFileProperty "domains_config_file", configIncludeDir, defaultProperties
    }

    /**
     * Returns the path for the parent directory containing the sites.
     * For example {@code /var/www}.
     *
     * <ul>
     * <li>profile property {@code "sites_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getSitesDirectory() {
        profileProperty("sites_directory", defaultProperties) as File
    }

    /**
     * Returns the name of the directory to store authentication files.
     *
     * <ul>
     * <li>profile property {@code "auth_subdirectory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAuthSubdirectory() {
        profileProperty("auth_subdirectory", defaultProperties)
    }

    /**
     * Returns the name of the directory to store SSL/certificates files.
     *
     * <ul>
     * <li>profile property {@code "ssl_subdirectory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSslSubdirectory() {
        profileProperty("ssl_subdirectory", defaultProperties)
    }

    /**
     * Returns the name of the directory to store the site web files.
     *
     * <ul>
     * <li>profile property {@code "web_subdirectory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getWebSubdirectory() {
        profileProperty("web_subdirectory", defaultProperties)
    }

    /**
     * Returns the default authentication provider.
     *
     * <ul>
     * <li>profile property {@code "default_auth_provider"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    AuthProvider getDefaultAuthProvider() {
        AuthProvider.parse profileProperty("default_auth_provider", defaultProperties)
    }

    /**
     * Returns the default authentication type.
     *
     * <ul>
     * <li>profile property {@code "default_auth_type"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    AuthType getDefaultAuthType() {
        AuthType.parse profileProperty("default_auth_type", defaultProperties)
    }

    /**
     * Returns the group name pattern for site users.
     *
     * <ul>
     * <li>profile property {@code "group_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getGroupPattern() {
        profileProperty("group_pattern", defaultProperties)
    }

    /**
     * Returns the user name pattern for site users.
     *
     * <ul>
     * <li>profile property {@code "user_pattern"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUserPattern() {
        profileProperty("user_pattern", defaultProperties)
    }

    /**
     * Returns the minimum group ID for site users.
     *
     * <ul>
     * <li>profile property {@code "minimum_gid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumGid() {
        profileNumberProperty("minimum_gid", defaultProperties)
    }

    /**
     * Returns the minimum user ID for site users.
     *
     * <ul>
     * <li>profile property {@code "minimum_uid"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMinimumUid() {
        profileNumberProperty("minimum_uid", defaultProperties)
    }

    /**
     * Returns the SSL/certificates directory for the domain.
     *
     * @see #domainDir(Domain)
     * @see #getSslSubdirectory()
     */
    File sslDir(Domain domain) {
        new File(domainDir(domain), sslSubdirectory)
    }

    /**
     * Returns the directory for the domain web files.
     *
     * @see #domainDir(Domain)
     * @see #getWebSubdirectory()
     */
    File webDir(Domain domain) {
        new File(domainDir(domain), webSubdirectory)
    }

    /**
     * Returns the domain site directory.
     *
     * @see #getSitesDirectory()
     */
    File domainDir(Domain domain) {
        new File(sitesDirectory, domain.name)
    }

    /**
     * Returns the default bind addresses.
     *
     * <ul>
     * <li>profile property {@code "default_binding"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBinding() {
        profileListProperty "default_binding", defaultProperties
    }

    /**
     * Returns additional Apache/mpds to enable.
     *
     * <ul>
     * <li>profile property {@code "additional_mods"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getAdditionalMods() {
        profileListProperty "additional_mods", defaultProperties
    }

    /**
     * Returns unique domains. The domains are identified by their name.
     */
    List getUniqueDomains() {
        List domains = []
        Set names = []
        service.domains.each { Domain domain ->
            if (!names.contains(domain.name)) {
                names.add domain.name
                domains.add domain
            }
        }
        return domains
    }

    /**
     * Enable the specified Apache/mod.
     *
     * @param mod
     *            the {@link String} of Apache/mod.
     */
    void enableMod(String mod) {
        enableMods([mod])
    }

    /**
     * Enable the specified Apache/mods.
     *
     * @param mods
     *            the {@link List} of Apache/mods.
     */
    void enableMods(List mods) {
        mods.each {
            def packages = modPackages it
            if (packages.size() > 0) {
                installPackages packages
            }
        }
        def worker = scriptCommandFactory.create apacheCommandsTemplate,
                "enableMods",
                "command", enableModCommand,
                "mods", mods
        worker()
        log.enabledMods this, worker, mods
    }

    /**
     * Enable the specified sites.
     */
    def enableSites(def sites) {
        def worker = scriptCommandFactory.create apacheCommandsTemplate,
                "enableSites",
                "command", enableSiteCommand,
                "sites", sites
        worker()
        log.enabledSites this, worker, sites
    }

    /**
     * Returns the packages needed for the specified Apache/mod.
     *
     * @param mod
     *            the Apache/mod {@link String} name.
     *
     * @return the {@link List} of the packages.
     */
    List modPackages(String mod) {
        profileListProperty "${mod}_packages", defaultProperties
    }

    @Override
    HttpdService getService() {
        super.getService();
    }
}