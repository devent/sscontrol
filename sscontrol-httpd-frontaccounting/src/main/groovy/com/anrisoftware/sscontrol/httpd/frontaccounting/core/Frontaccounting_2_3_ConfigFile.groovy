/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.core

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.frontaccounting.FrontaccountingService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>FrontAccounting 2.3</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Frontaccounting_2_3_ConfigFile {

    private Object script

    @Inject
    private Frontaccounting_2_3_ConfigFileLogger log

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    private TemplateResource configTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Frontaccounting_2_3_Config"
        this.configTemplate = templates.getResource "config"
    }

    /**
     * Creates the <i>FrontAccounting</i> service directories.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     */
    void deployConfig(Domain domain, FrontaccountingService service) {
        File file = frontaccountingConfigFile(domain, service)
        if (!file.exists()) {
            return
        }
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, baseConfs(service), file
        log.baseConfigDeployed this, domain, file
    }

    /**
     * Returns <i>FrontAccounting</i> configuration.
     */
    List baseConfs(FrontaccountingService service) {
        [
            titleConfig(service),
            debugSqlConfig(service),
            debugGoConfig(service),
            debugPdfConfig(service),
            debugSqlTrailConfig(service),
            debugSelectTrailConfig(service),
        ]
    }

    def titleConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "titleConfigSearch")
        def replace = configTemplate.getText(true, "titleConfig", "title", service.siteTitle)
        new TokenTemplate(search, replace)
    }

    def debugSqlConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "debugSqlConfigSearch")
        def replace = configTemplate.getText(true, "debugSqlConfig", "level", service.debugLogging("level")["sql"])
        new TokenTemplate(search, replace)
    }

    def debugGoConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "debugGoConfigSearch")
        def replace = configTemplate.getText(true, "debugGoConfig", "level", service.debugLogging("level")["go"])
        new TokenTemplate(search, replace)
    }

    def debugPdfConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "debugPdfConfigSearch")
        def replace = configTemplate.getText(true, "debugPdfConfig", "level", service.debugLogging("level")["pdf"])
        new TokenTemplate(search, replace)
    }

    def debugSqlTrailConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "debugSqlTrailConfigSearch")
        def replace = configTemplate.getText(true, "debugSqlTrailConfig", "level", service.debugLogging("level")["sqltrail"])
        new TokenTemplate(search, replace)
    }

    def debugSelectTrailConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "debugSelectTrailConfigSearch")
        def replace = configTemplate.getText(true, "debugSelectTrailConfig", "level", service.debugLogging("level")["select"])
        new TokenTemplate(search, replace)
    }

    def databaseUserConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "databaseUserConfigSearch")
        def replace = configTemplate.getText(true, "databaseUserConfig", "user", service.database.user)
        new TokenTemplate(search, replace)
    }

    def databasePasswordConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "databasePasswordConfigSearch")
        def replace = configTemplate.getText(true, "databasePasswordConfig", "password", service.database.password)
        new TokenTemplate(search, replace)
    }

    def databaseDbConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "databaseDbConfigSearch")
        def replace = configTemplate.getText(true, "databaseDbConfig", "db", service.database.database)
        new TokenTemplate(search, replace)
    }

    def databaseHostConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "databaseHostConfigSearch")
        def replace = configTemplate.getText(true, "databaseHostConfig", "host", service.database.host, "port", service.database.port)
        new TokenTemplate(search, replace)
    }

    def databasePrefixConfig(FrontaccountingService service) {
        def search = configTemplate.getText(true, "databasePrefixConfigSearch")
        def replace = configTemplate.getText(true, "databasePrefixConfig", "prefix", service.database.prefix)
        new TokenTemplate(search, replace)
    }

    def databaseDriverConfig(FrontaccountingService service) {
        if (service.database.driver == null) {
            return []
        }
        def search = configTemplate.getText(true, "databaseDriverConfigSearch")
        def replace = configTemplate.getText(true, "databaseDriverConfig", "driver", service.database.driver)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the <i>FrontAccounting</i> service name.
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
