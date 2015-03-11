/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.core

import static org.apache.commons.lang3.StringUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory

/**
 * Parses the <i>ownCloud</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ConfigParser {

    private Object script

    @Inject
    ConfigParserLogger log

    @Inject
    DebugLevelRenderer debugLevelRenderer

    @Inject
    ChangeFileModFactory changeFileModFactory

    TemplateResource owncloudConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Owncloud_7_Config"
        owncloudConfigTemplate = templates.getResource "config"
    }

    /**
     * Deploys the <i>ownCloud</i> configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void deployConfig(Domain domain, OwncloudService service) {
        File file = owncloudConfigFile domain, service
        if (file.exists()) {
            deployConfigIni(domain, service)
            secureConfigIni domain, service
        }
    }

    private secureConfigIni(Domain domain, OwncloudService service) {
        File file = owncloudConfigFile domain, service
        changeFileModFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "o-rw",
                files: file,
                this, threads)()
    }

    private deployConfigIni(Domain domain, OwncloudService service) {
        def file = owncloudConfigFile domain, service
        def current = removeArrayEnd currentConfiguration(file)
        def configs = [
            datadirectoryConfig(domain, service),
            overwritecliurlConfig(domain, service),
            dbtypeConfig(domain, service),
            dbnameConfig(domain, service),
            dbhostConfig(domain, service),
            dbtableprefixConfig(domain, service),
            dbuserConfig(domain, service),
            dbpasswordConfig(domain, service),
            forcesslConfig(domain, service),
            overwritewebrootConfig(domain, service),
        ]
        deployConfiguration configurationTokens('//'), current, configs, file
        appendArrayEnd file
    }

    private String removeArrayEnd(String config) {
        join(split(config, '\n').inject([]) { list, line ->
            if (line =~ /(?m)^\);/) {
                list
            } else {
                list << line
            }
        }, '\n')
    }

    private appendArrayEnd(File file) {
        FileUtils.write file, '\n);\n', charset, true
    }

    def datadirectoryConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDatadirectorySearch")
        def replace = owncloudConfigTemplate.getText(true, "configDatadirectory", "dir", owncloudDataDirectory(domain, service))
        new TokenTemplate(search, replace)
    }

    def overwritecliurlConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configOverwritecliurlSearch")
        def replace = owncloudConfigTemplate.getText(true, "configOverwritecliurl", "url", owncloudOverwriteCliUrl(domain, service))
        new TokenTemplate(search, replace)
    }

    def dbtypeConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDbtypeSearch")
        def replace = owncloudConfigTemplate.getText(true, "configDbtype", "type", service.database.adapter)
        new TokenTemplate(search, replace)
    }

    def dbnameConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDbnameSearch")
        def replace = owncloudConfigTemplate.getText(true, "configDbname", "name", service.database.database)
        new TokenTemplate(search, replace)
    }

    def dbhostConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDbhostSearch")
        def replace = owncloudConfigTemplate.getText(true, "configDbhost", "host", service.database.host, "port", service.database.port)
        new TokenTemplate(search, replace)
    }

    def dbtableprefixConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDbtableprefixSearch")
        def replace = owncloudConfigTemplate.getText(true, "configDbtableprefix", "tableprefix", service.database.prefix)
        new TokenTemplate(search, replace)
    }

    def dbuserConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDbuserSearch")
        def replace = owncloudConfigTemplate.getText(true, "configDbuser", "user", service.database.user)
        new TokenTemplate(search, replace)
    }

    def dbpasswordConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configDbpasswordSearch")
        def replace = owncloudConfigTemplate.getText(true, "configDbpassword", "password", service.database.password)
        new TokenTemplate(search, replace)
    }

    def forcesslConfig(Domain domain, OwncloudService service) {
        def search = owncloudConfigTemplate.getText(true, "configForcesslSearch")
        def replace = owncloudConfigTemplate.getText(true, "configForcessl", "enabled", owncloudForcesslEnabled)
        new TokenTemplate(search, replace)
    }

    def overwritewebrootConfig(Domain domain, OwncloudService service) {
        def alias = service.alias
        if (!alias.empty && !alias.startsWith("/")) {
            alias = "/" + alias
        }
        def search = owncloudConfigTemplate.getText(true, "configOverwritewebrootSearch")
        def replace = owncloudConfigTemplate.getText(true, "configOverwritewebroot", "root", alias)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Sets the script script.
     */
    void setScript(Object parent) {
        this.script = parent
    }

    /**
     * Returns the script script.
     */
    Object getScript() {
        script
    }

    /**
     * Delegates missing properties to the script script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the script script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this).append(script.toString()).toString();
    }
}
