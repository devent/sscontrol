/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.linux.nginx

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.bindings.BindingFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Uses Nginx service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class NginxScript extends LinuxScript {

    @Inject
    private NginxScriptLogger log

    @Inject
    private BindingFactory bindingFactory

    /**
     * Returns the template resource containing commands.
     */
    abstract TemplateResource getNginxCommandsTemplate()

    @Override
    HttpdService getService() {
        super.getService();
    }

    /**
     * Returns the restart command for the Nginx service.
     *
     * <ul>
     * <li>property key {@code nginx_restart_command}</li>
     * <li>property key {@code restart_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    @Override
    String getRestartCommand() {
        if (containsKey("nginx_restart_command")) {
            profileProperty "nginx_restart_command", defaultProperties
        } else {
            profileProperty "restart_command", defaultProperties
        }
    }

    /**
     * Returns the path of the Nginx configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_configuration_directory"}</li>
     * <li>profile property {@code "configuration_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    @Override
    File getConfigurationDir() {
        if (containsKey("nginx_configuration_directory")) {
            profileDirProperty "nginx_configuration_directory", defaultProperties
        } else {
            profileDirProperty "configuration_directory", defaultProperties
        }
    }

    /**
     * Returns the directory for the included configuration, for
     * example {@code "conf.d".} If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_config_include_directory"}</li>
     * <li>profile property {@code "config_include_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getConfigIncludeDir() {
        if (containsKey("nginx_config_include_directory")) {
            profileFileProperty "nginx_config_include_directory", configurationDir, defaultProperties
        } else {
            profileFileProperty "config_include_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the directory for the available sites, for
     * example {@code "sites-available".} If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_sites_available_directory"}</li>
     * <li>profile property {@code "sites_available_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getSitesAvailableDir() {
        if (containsKey("nginx_sites_available_directory")) {
            profileFileProperty "nginx_sites_available_directory", configurationDir, defaultProperties
        } else {
            profileFileProperty "sites_available_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the directory for the enabled sites, for
     * example {@code "sites-enabled".} If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_sites_enabled_directory"}</li>
     * <li>profile property {@code "sites_enabled_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getSitesEnabledDir() {
        if (containsKey("nginx_sites_enabled_directory")) {
            profileFileProperty "nginx_sites_enabled_directory", configurationDir, defaultProperties
        } else {
            profileFileProperty "sites_enabled_directory", configurationDir, defaultProperties
        }
    }

    /**
     * Returns the path for the parent directory containing the sites,
     * for example {@code "/var/www".}
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
     * Returns the path for the Nginx configuration file,
     * for example {@code "nginx.conf".}  If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "nginx_config_file"}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getDefaultProperties()
     */
    File getNginxConfigFile() {
        profileFileProperty "nginx_config_file", configurationDir, defaultProperties
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
        profileProperty "ssl_subdirectory", defaultProperties
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
        profileProperty "web_subdirectory", defaultProperties
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
     * Returns the index files for
     * example {@code "index.php,index.html,index.htm".}
     *
     * <ul>
     * <li>profile property {@code "index_files"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getIndexFiles() {
        profileListProperty "index_files", defaultProperties
    }

    /**
     * Returns the SSL protocols, for
     * example {@code "SSLv3,TLSv1".}
     *
     * <ul>
     * <li>profile property {@code "ssl_protocols"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getSslProtocols() {
        profileListProperty "ssl_protocols", defaultProperties
    }

    /**
     * Returns the SSL ciphers, for
     * example {@code "ALL,!ADH,!EXPORT56,RC4+RSA,+HIGH,+MEDIUM,+EXP".}
     *
     * <ul>
     * <li>profile property {@code "ssl_ciphers"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getSslCiphers() {
        profileListProperty "ssl_ciphers", defaultProperties
    }

    /**
     * Returns the SSL session timeout, for
     * example {@code "5m".}
     *
     * <ul>
     * <li>profile property {@code "ssl_session_timeout"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSslSessionTimeout() {
        profileProperty "ssl_session_timeout", defaultProperties
    }

    /**
     * Returns the logging storage, for
     * example {@code "/var/log/nginx/error.log".}
     *
     * <ul>
     * <li>profile property {@code "logging_storage"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getLoggingStorage() {
        profileProperty "logging_storage", defaultProperties
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
     * Enable the specified sites.
     */
    def enableSites(List sites) {
        def files = sites.inject([]) { acc, val ->
            acc << new File(sitesAvailableDir, val)
        }
        def targets = sites.inject([]) { acc, val ->
            acc << new File(sitesEnabledDir, val)
        }
        link files: files, targets: targets, override: true
        log.enabledSites this, sites
    }
}
