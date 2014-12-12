/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04.Ubuntu_12_04_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Gitit</i> configuration for <i>Nginx Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class NginxConfig extends Ubuntu_12_04_Config implements ServiceConfig {

    TemplateResource gititDomainTemplate

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
    final void setNginxTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Gitit_Nginx_Ubuntu_12_04"
        this.gititDomainTemplate = templates.getResource "domainconfig"
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
     *            the <i>Gitit</i> {@link GititService} service.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, GititService service, List config) {
        def serviceAliasDir = serviceAliasDir domain, refDomain, service
        def serviceDir = serviceDir domain, refDomain, service
        def args = [:]
        args.address = service.binding.addresses[0].address
        args.port = service.binding.addresses[0].port
        args.location = serviceAliasDir
        def configStr = gititDomainTemplate.getText(true, "domainConfig", "args", args)
        config << configStr
    }
}
