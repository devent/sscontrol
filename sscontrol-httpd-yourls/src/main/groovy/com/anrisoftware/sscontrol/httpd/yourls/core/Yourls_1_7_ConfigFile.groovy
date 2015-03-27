/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.core

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.yourls.Access
import com.anrisoftware.sscontrol.httpd.yourls.YourlsService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>Yourls 1.7</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Yourls_1_7_ConfigFile {

    private Object script

    @Inject
    private Yourls_1_7_ConfigFileLogger log

    @Inject
    private ConvertAttributeRenderer convertAttributeRenderer

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    private TemplateResource configTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Yourls_1_7_Config", ["renderers": [convertAttributeRenderer]]
        this.configTemplate = templates.getResource "config"
    }

    /**
     * Creates the <i>Yourls</i> service directories.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void deployConfig(Domain domain, YourlsService service) {
        File file = yourlsConfigFile(domain, service)
        if (!file.exists()) {
            FileUtils.copyFile yourlsConfigSampleFile(domain, service), file
        }
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, baseConfs(service), file
        log.baseConfigDeployed this, domain, file
    }

    /**
     * Returns <i>Yourls</i> configuration.
     */
    List baseConfs(YourlsService service) {
        [
            databaseUserConfig(service),
            databasePasswordConfig(service),
            databaseDbConfig(service),
            databaseHostConfig(service),
            databasePrefixConfig(service),
            databaseDriverConfig(service),
            siteConfig(service),
            timezoneConfig(service),
            langConfig(service),
            uniqueConfig(service),
            privateSiteConfig(service),
            privateStatsConfig(service),
            privateApiConfig(service),
            cookieConfig(service),
            userConfig(service),
            debugConfig(service),
            convertConfig(service),
            reservedConfig(service),
        ]
    }

    def databaseUserConfig(YourlsService service) {
        def search = configTemplate.getText(true, "databaseUserConfigSearch")
        def replace = configTemplate.getText(true, "databaseUserConfig", "user", service.database.user)
        new TokenTemplate(search, replace)
    }

    def databasePasswordConfig(YourlsService service) {
        def search = configTemplate.getText(true, "databasePasswordConfigSearch")
        def replace = configTemplate.getText(true, "databasePasswordConfig", "password", service.database.password)
        new TokenTemplate(search, replace)
    }

    def databaseDbConfig(YourlsService service) {
        def search = configTemplate.getText(true, "databaseDbConfigSearch")
        def replace = configTemplate.getText(true, "databaseDbConfig", "db", service.database.database)
        new TokenTemplate(search, replace)
    }

    def databaseHostConfig(YourlsService service) {
        def search = configTemplate.getText(true, "databaseHostConfigSearch")
        def replace = configTemplate.getText(true, "databaseHostConfig", "host", service.database.host, "port", service.database.port)
        new TokenTemplate(search, replace)
    }

    def databasePrefixConfig(YourlsService service) {
        def search = configTemplate.getText(true, "databasePrefixConfigSearch")
        def replace = configTemplate.getText(true, "databasePrefixConfig", "prefix", service.database.prefix)
        new TokenTemplate(search, replace)
    }

    def databaseDriverConfig(YourlsService service) {
        if (service.database.driver == null) {
            return []
        }
        def search = configTemplate.getText(true, "databaseDriverConfigSearch")
        def replace = configTemplate.getText(true, "databaseDriverConfig", "driver", service.database.driver)
        new TokenTemplate(search, replace)
    }

    def siteConfig(YourlsService service) {
        def search = configTemplate.getText(true, "siteConfigSearch")
        def replace = configTemplate.getText(true, "siteConfig", "site", service.site)
        new TokenTemplate(search, replace)
    }

    def timezoneConfig(YourlsService service) {
        def search = configTemplate.getText(true, "timezoneConfigSearch")
        def replace = configTemplate.getText(true, "timezoneConfig", "offset", service.gmtOffset)
        new TokenTemplate(search, replace)
    }

    def langConfig(YourlsService service) {
        def search = configTemplate.getText(true, "langConfigSearch")
        def replace = configTemplate.getText(true, "langConfig", "lang", service.language)
        new TokenTemplate(search, replace)
    }

    def uniqueConfig(YourlsService service) {
        def search = configTemplate.getText(true, "uniqueConfigSearch")
        def replace = configTemplate.getText(true, "uniqueConfig", "enabled", service.uniqueUrls)
        new TokenTemplate(search, replace)
    }

    def privateSiteConfig(YourlsService service) {
        def search = configTemplate.getText(true, "privateSiteConfigSearch")
        def replace = configTemplate.getText(true, "privateSiteConfig", "enabled", service.siteAccess == Access.closed)
        new TokenTemplate(search, replace)
    }

    def privateStatsConfig(YourlsService service) {
        def search = configTemplate.getText(true, "privateStatsConfigSearch")
        def replace = configTemplate.getText(true, "privateStatsConfig", "enabled", service.statsAccess == Access.closed)
        new TokenTemplate(search, replace)
    }

    def privateApiConfig(YourlsService service) {
        def search = configTemplate.getText(true, "privateApiConfigSearch")
        def replace = configTemplate.getText(true, "privateApiConfig", "enabled", service.apiAccess == Access.closed)
        new TokenTemplate(search, replace)
    }

    def cookieConfig(YourlsService service) {
        def search = configTemplate.getText(true, "cookieConfigSearch")
        def replace = configTemplate.getText(true, "cookieConfig", "cookie", yourlsCookieKey)
        new TokenTemplate(search, replace)
    }

    def userConfig(YourlsService service) {
        def search = configTemplate.getText(true, "userConfigSearch")
        def replace = configTemplate.getText(true, "userConfig", "users", service.users)
        new TokenTemplate(search, replace)
    }

    def debugConfig(YourlsService service) {
        def search = configTemplate.getText(true, "debugConfigSearch")
        def replace = configTemplate.getText(true, "debugConfig", "enabled", service.debugLogging("level")["yourls"] > 0)
        new TokenTemplate(search, replace)
    }

    def convertConfig(YourlsService service) {
        def search = configTemplate.getText(true, "convertConfigSearch")
        def replace = configTemplate.getText(true, "convertConfig", "convert", service.urlConvertMode)
        new TokenTemplate(search, replace)
    }

    def reservedConfig(YourlsService service) {
        def search = configTemplate.getText(true, "reservedConfigSearch")
        def replace = configTemplate.getText(true, "reservedConfig", "reserved", service.reserved)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the <i>Yourls</i> service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
    }

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the parent script.
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
