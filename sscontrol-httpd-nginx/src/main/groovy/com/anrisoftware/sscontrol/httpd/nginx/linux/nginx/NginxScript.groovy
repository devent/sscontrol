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

    @Override
    def run() {
        super.run()
        distributionSpecificConfiguration()
    }

    /**
     * Run the distribution specific configuration.
     */
    abstract distributionSpecificConfiguration()

    /**
     * Returns the template resource containing commands.
     */
    abstract TemplateResource getNginxCommandsTemplate()

    @Override
    HttpdService getService() {
        super.getService();
    }

    /**
     * Returns the directory for the included configuration. If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "config_include_directory"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getConfigIncludeDir() {
        def path = profileProperty("config_include_directory", defaultProperties)
        if (path instanceof File) {
            return path
        } else {
            def file = new File(path)
            return file.absolute ? file : new File(configurationDir, path)
        }
    }

    /**
     * Returns the path for the default configuration file.
     *
     * <ul>
     * <li>profile property {@code "default_config_file"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getDefaultConfigFile() {
        def file = profileProperty("default_config_file", defaultProperties)
        new File(sitesAvailableDir, file)
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
}
