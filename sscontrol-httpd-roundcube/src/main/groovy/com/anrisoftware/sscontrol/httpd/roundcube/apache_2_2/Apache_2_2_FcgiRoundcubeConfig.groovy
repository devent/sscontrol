/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.apache_2_2

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Apache php-fcgi Roundcube</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Apache_2_2_FcgiRoundcubeConfig extends FcgiRoundcubeConfig {

    @Inject
    private Apache_2_2_FcgiRoundcubeConfigLogger log

    private Object script

    TemplateResource domainConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        super.deployDomain domain, refDomain, service, config
        createDomainConfig domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        super.deployService domain, service, config
        createDomainConfig domain, null, service, config
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2_FcgiRoundcubeConfig"
        domainConfigTemplate = templates.getResource "domain_config"
    }

    /**
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, RoundcubeService service, List config) {
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDirectory = serviceDir domain, refDomain, service
        def args = [:]
        args.domain = domain
        args.service = service
        args.serviceDirectory = serviceDirectory
        args.sitesDirectory = sitesDirectory
        args.scriptsSubdirectory = scriptsSubdirectory
        args.scriptStarterFileName = scriptStarterFileName
        args.serviceAliasDir = serviceAliasDir
        args.namePattern = namePattern domain
        def configStr = domainConfigTemplate.getText(true, "domainConfig", "args", args)
        config << configStr
        log.createdDomainConfig this, domain, configStr
    }

    String namePattern(Domain domain) {
        domain.name.replaceAll "\\.", "\\\\."
    }

    /**
     * Returns the service alias directory path.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the references {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #serviceDir(Domain, Domain, RoundcubeService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, RoundcubeService service) {
        def serviceDir = serviceDir domain, refDomain, service
        service.alias.empty ? "$serviceDir/" : serviceDir
    }

    /**
     * Returns the service directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #roundcubeDir(Domain, RoundcubeService)
     */
    String serviceDir(Domain domain, Domain refDomain, RoundcubeService service) {
        refDomain == null ? roundcubeDir(domain, service).absolutePath :
                roundcubeDir(refDomain, service).absolutePath
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
     * Returns the service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the service profile name.
     */
    String getProfile() {
        script.getProfile()
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
    String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
