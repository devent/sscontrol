/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.nginx_thin_ubuntu_12_04;

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Redmine</i> configuration for <i>Nginx/Thin Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class NginxConfig extends Ubuntu_12_04_Config implements ServiceConfig {

    @Inject
    NginxConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    Templates nginxTemplates

    TemplateResource domainConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults domain, service
        super.deployDomain domain, refDomain, service, config
        createDomainConfig domain, refDomain, service, config
        createDomainUpstreamConfig domain, refDomain, service
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults domain, service
        super.deployService domain, service, config
        createDomainConfig domain, null, service, config
        createDomainUpstreamConfig domain, null, service
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
     *            the <i>Redmine</i> {@link RedmineService} service.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, RedmineService service, List config) {
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def configStr = domainConfigTemplate.getText(
                true, "domainConfig", "args", [
                    domain: domain,
                    prefix: service.prefix,
                    alias: serviceAliasDir,
                    domainName: domainNameAsFileName(domain),
                ])
        config << configStr
    }

    /**
     * Creates the domain upstream cluster configuration.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @param refDomain
     *            the referenced {@link Domain}.
     *
     * @param service
     *            the <i>Redmine</i> {@link RedmineService} service.
     */
    void createDomainUpstreamConfig(Domain domain, Domain refDomain, RedmineService service) {
        def file = domainUpstreamConfigFile domain, service
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def configStr = domainConfigTemplate.getText(
                true, "upstreamConfig", "args", [
                    domain: domain,
                    prefix: service.prefix,
                    sockets: serviceAliasDir,
                    domainName: domainNameAsFileName(domain),
                ])
        FileUtils.write file, configStr
        logg.deployDomainUpstreamConfig this, domain, file
    }

    /**
     * Returns the domain upstream configuration file inside the
     * domain directory, for example
     * {@code /etc/nginx/sites-available/100-robobee-domain.com-upstream.conf}
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    File domainUpstreamConfigFile(Domain domain, RedmineService service) {
        def file = new ST(upstreamDomainFile).add("domainName", domain.name).render()
        new File(sitesAvailableDir, file)
    }

    /**
     * Returns the upstream domain configuration file, for
     * example {@code "100-robobee-<domainName>-upstream.conf".}
     *
     * <ul>
     * <li>profile property {@code "redmine_upstream_domain_file"}</li>
     * </ul>
     *
     * @see #getRedmineProperties()
     */
    String getUpstreamDomainFile() {
        profileProperty "redmine_upstream_domain_file", redmineProperties
    }

    /**
     * @see LinuxScript#getName()
     */
    String getName() {
        script.getName()
    }

    @Override
    public void setScript(LinuxScript script) {
        super.setScript(script)
        this.nginxTemplates = templatesFactory.create "Redmine_Nginx_Thin_Ubuntu_12_04"
        this.domainConfigTemplate = nginxTemplates.getResource "domain_config"
    }
}
