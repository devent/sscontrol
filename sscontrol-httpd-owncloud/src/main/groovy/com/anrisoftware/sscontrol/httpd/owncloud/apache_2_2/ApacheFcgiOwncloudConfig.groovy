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
package com.anrisoftware.sscontrol.httpd.owncloud.apache_2_2

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Apache php-fcgi ownCloud</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class ApacheFcgiOwncloudConfig extends FcgiOwncloudConfig {

    @Inject
    private ApacheFcgiOwncloudConfigLogger log

    private Object script

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
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, OwncloudService service, List config) {
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def properties = [:]
        properties.domain = domain
        properties.service = service
        properties.script = script
        properties.config = this
        properties.serviceAliasDir = serviceAliasDir
        properties.serviceDir = serviceDir
        properties.namePattern = namePattern(domain)
        def configStr = domainConfigTemplate.getText(true, "domainConfig", "properties", properties)
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
     *            the {@link OwncloudService} service.
     *
     * @see #wordpressDir(def)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, OwncloudService service) {
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
     *            the {@link OwncloudService} service.
     *
     * @see #wordpressDir(Domain, OwncloudService)
     */
    String serviceDir(Domain domain, Domain refDomain, OwncloudService service) {
        refDomain == null ? piwikDir(domain, service).absolutePath :
                piwikDir(refDomain, service).absolutePath
    }

    /**
     * Returns the <i>ownCloud</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see OwncloudService#getPrefix()
     */
    File piwikDir(Domain domain, OwncloudService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the domain configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getDomainConfigTemplate()

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
    abstract String getServiceName()

    /**
     * Returns the service profile name.
     */
    String getProfile() {
        script.getProfile()
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

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
