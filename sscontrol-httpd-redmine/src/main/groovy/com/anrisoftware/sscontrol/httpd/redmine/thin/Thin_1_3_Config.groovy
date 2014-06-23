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
package com.anrisoftware.sscontrol.httpd.redmine.thin

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Thin 1.3</i> <i>Redmine</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Thin_1_3_Config extends AbstractThinConfig {

    @Inject
    Thin_1_3_ConfigLogger logg

    @Inject
    TemplatesFactory templatesFactory

    Templates thinConfigTemplates

    TemplateResource thinConfigTemplate

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        createDomainServiceConfig domain, service
    }

    /**
     * Creates the <i>Thin</i> configuration for the service.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void createDomainServiceConfig(Domain domain, RedmineService service) {
        def file = domainConfigurationFile domain, service
        def conf = thinConfigTemplate.getText(true, "domainServiceConfig", "args", [
            properties: this,
            domain: domain,
            chdir: redmineDir(domain, service),
            logFile: domainLogFile(domain, service),
            pidFile: domainPidFile(domain, service),
            socketFile: domainSocketFile(domain, service),
        ])
        FileUtils.write file, conf, charset
        logg.serviceFileCreated this, file, conf
    }

    @Override
    void setScript(Object script) {
        super.setScript(script);
        this.thinConfigTemplates = templatesFactory.create("Thin_1_3_Config")
        this.thinConfigTemplate = thinConfigTemplates.getResource("domain_service_config")
    }
}
