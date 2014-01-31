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
package com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.linux

import java.util.regex.Pattern

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.phpldapadmin.PhpldapadminService;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * phpLDAPAdmin from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class PhpldapadminFromArchiveConfig extends PhpldapadminConfig {

    @Inject
    private PhpldapadminFromArchiveConfigLogger log

    Templates phpldapadminTemplates

    TemplateResource phpldapadminConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        downloadPhpldapadmin domain
        deployConfiguration domain, service
        deployServerConfig domain, service
    }

    /**
     * Downloads and unpacks the phpLDAPAdmin archive for the specified domain.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void downloadPhpldapadmin(Domain domain) {
        def name = FilenameUtils.getName adminArchive.path
        def target = new File(tmpDirectory, name)
        def extension = FilenameUtils.getExtension target.absolutePath
        def outputDir = adminConfigurationDir(domain)
        def linkTarget = adminLinkedConfigurationDir(domain)
        FileUtils.copyURLToFile adminArchive.toURL(), target
        unpack file: target, type: extension, output: outputDir, command: tarCommand, override: true
        linkTarget.delete()
        link files: outputDir, targets: linkTarget
        log.archiveDownloaded script, adminArchive, outputDir
    }

    /**
     * Returns the phpLDAPAdmin archive, for
     * example {@code "http://downloads.sourceforge.net/project/phpldapadmin/phpldapadmin-php5/1.2.3/phpldapadmin-1.2.3.tgz".}
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_archive"}</li>
     * </ul>
     *
     * @see #getPhpldapadminProperties()
     */
    URI getAdminArchive() {
        profileURIProperty "phpldapadmin_archive", phpldapadminProperties
    }

    /**
     * Deploys the phpldapadmin/configuration.
     */
    void deployConfiguration(Domain domain, PhpldapadminService service) {
        def configFile = adminConfigFile(domain)
        if (!configFile.isFile()) {
            FileUtils.copyFile adminExampleConfig(domain), configFile
        }
        def configs = adminConfigurations(domain, service)
        script.deployConfiguration configurationTokens("//"), adminConfiguration(domain), configs, configFile
        changeMod mod: "o-r", files: configFile
    }

    /**
     * Returns the phpldapadmin/configurations.
     */
    List adminConfigurations(Domain domain, PhpldapadminService service) {
        [
            commentFirstNewServer(),
            commentFirstNewServerName(),
            addIncludeServers(domain),
        ]
    }

    def commentFirstNewServer() {
        def search = phpldapadminConfigTemplate.getText(true, "firstNewServer_search")
        def replace = phpldapadminConfigTemplate.getText(true, "firstNewServer")
        def token = new TokenTemplate(search, replace)
        token.append = false
        token
    }

    def commentFirstNewServerName() {
        def search = phpldapadminConfigTemplate.getText(true, "firstNewServerName_search")
        def replace = phpldapadminConfigTemplate.getText(true, "firstNewServerName")
        def token = new TokenTemplate(search, replace)
        token.append = false
        token
    }

    def addIncludeServers(def domain) {
        def file = adminServersConfigFile(domain)
        def search = phpldapadminConfigTemplate.getText(true, "includeServersConfig_search", "file", Pattern.quote(file.name))
        def replace = phpldapadminConfigTemplate.getText(true, "includeServersConfig", "file", file.name)
        def token = new TokenTemplate(search, replace)
        token.append = false
        token.enclose = false
        token
    }

    /**
     * Deploy the servers configuration.
     */
    def deployServerConfig(Domain domain, PhpldapadminService service) {
        def string = phpldapadminConfigTemplate.getText true, "serversConfig", "service", service
        def file = adminServersConfigFile(domain)
        FileUtils.write file, string
        changeMod mod: "o-r", files: file
        log.serverConfigDeployed script, file, string
    }

    /**
     * Phpldapadmin configuration directory, for
     * example {@code "/etc/%s/phpldampadmin-1.2.3"}. The first argument
     * is replaced with the domain directory.
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_configuration_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see #getPhpldapadminProperties()
     */
    File adminConfigurationDir(Domain domain) {
        String path = profileProperty "phpldapadmin_configuration_directory", phpldapadminProperties
        new File(String.format(path, domainDir(domain)))
    }

    /**
     * The linked Phpldapadmin configuration directory, for
     * example {@code "/etc/%s/phpldampadmin"}. The first argument
     * is replaced with the domain directory.
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_linked_configuration_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see #getPhpldapadminProperties()
     */
    File adminLinkedConfigurationDir(Domain domain) {
        String path = profileProperty "phpldapadmin_linked_configuration_directory", phpldapadminProperties
        new File(String.format(path, domainDir(domain)))
    }

    /**
     * Phpldapadmin configuration file, for
     * example {@code "config/config.php"}. If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_configuration_file"}</li>
     * </ul>
     *
     * @see #adminLinkedConfigurationDir(Object)
     * @see #getPhpldapadminProperties()
     */
    File adminConfigFile(Domain domain) {
        def parent = adminLinkedConfigurationDir(domain)
        profileFileProperty "phpldapadmin_configuration_file", parent, phpldapadminProperties
    }

    /**
     * Phpldapadmin server configuration file, for
     * example {@code "config/robobee-servers.php"}. If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_servers_configuration_file"}</li>
     * </ul>
     *
     * @see #adminLinkedConfigurationDir(Object)
     * @see #getPhpldapadminProperties()
     */
    File adminServersConfigFile(Domain domain) {
        def parent = adminLinkedConfigurationDir(domain)
        profileFileProperty "phpldapadmin_servers_configuration_file", parent, phpldapadminProperties
    }

    /**
     * Example Phpldapadmin configuration file, for
     * example {@code "config/config.php.example"}. If the path is
     * not absolute then it is assume to be under the configuration directory.
     *
     * <ul>
     * <li>profile property {@code "phpldapadmin_example_configuration_file"}</li>
     * </ul>
     *
     * @see #adminLinkedConfigurationDir(Object)
     * @see #getPhpldapadminProperties()
     */
    File adminExampleConfig(Domain domain) {
        def parent = adminLinkedConfigurationDir(domain)
        profileFileProperty "phpldapadmin_example_configuration_file", parent, phpldapadminProperties
    }

    /**
     * Returns the current phpldapadmin/configuration.
     *
     * @see #adminConfigFile(Domain)
     */
    String adminConfiguration(Domain domain) {
        currentConfiguration adminConfigFile(domain)
    }

    void setScript(LinuxScript script) {
        super.setScript script
        phpldapadminTemplates = templatesFactory.create "PhpldapadminFromArchiveConfig"
        phpldapadminConfigTemplate = phpldapadminTemplates.getResource "config"
    }
}
