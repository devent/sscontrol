/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.apache_2_2

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService

/**
 * <i>Wordpress Apache php-fcgi</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class ApacheFcgiWordpressConfig extends FcgiWordpressConfig {

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

    /**
     * Creates the domain configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param refDomain
     *            the referenced {@link Domain}.
     *
     * @param service
     *            the <i>Wordpress</i> {@link WebService}.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, WebService service, List config) {
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def args = [:]
        args.serviceDir = serviceDir
        args.namePattern = namePattern(domain)
        args.serviceAlias = service.alias
        args.serviceAliasDir = serviceAliasDir
        args.sitesDirectory = script.script.sitesDirectory
        args.scriptsSubdirectory = scriptsSubdirectory
        args.blockNoReferrerRequests = script.blockNoReferrerRequests
        args.scriptStarterFileName = scriptStarterFileName
        args.domainName = domain.name
        def configStr = domainConfigTemplate.getText(true, "domainConfig", "args", args)
        config << configStr
    }

    String namePattern(Domain domain) {
        domain.name.replaceAll "\\.", "\\\\."
    }

    /**
     * Returns the service alias directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link RoundcubeService} <i>Wordpress</i> service.
     *
     * @see #wordpressDir(def)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, WordpressService service) {
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
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link RoundcubeService} <i>Wordpress</i> service.
     *
     * @see #wordpressDir(Domain, PiwikService)
     */
    String serviceDir(Domain domain, Domain refDomain, WordpressService service) {
        refDomain == null ? wordpressDir(domain, service).absolutePath :
                wordpressDir(refDomain, service).absolutePath
    }

    /**
     * Returns the <i>Wordpress</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} <i>Wordpress</i> service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see PiwikService#getPrefix()
     */
    File wordpressDir(Domain domain, WordpressService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the domain configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getDomainConfigTemplate()
}
