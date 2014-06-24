/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
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
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddFactory

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

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    DurationAttributeRenderer durationAttributeRenderer

    Templates thinConfigTemplates

    TemplateResource thinConfigTemplate

    TemplateResource redmineConfigTemplate

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        createThinUser domain, service
        createThinDefaults domain, service
        createThinScript domain, service
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
    void createThinUser(Domain domain, RedmineService service) {
        localGroupAddFactory.create(
                log: log,
                command: groupAddCommand,
                systemGroup: true,
                groupsFile: groupsFile,
                groupName: thinGroup,
                this, threads)()
        localUserAddFactory.create(
                log: log,
                userName: thinUser,
                groupName: thinGroup,
                command: userAddCommand,
                systemUser: true,
                usersFile: usersFile,
                shell: "/bin/false",
                this, threads)()
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

    /**
     * Creates the <i>Thin</i> defaults.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void createThinDefaults(Domain domain, RedmineService service) {
        def file = thinDefaultsFile
        def conf = redmineConfigTemplate.getText(true, "thinDefaultsConfig", "args", [
            properties: this,
        ])
        FileUtils.write file, conf, charset
        logg.serviceFileCreated this, file, conf
    }

    /**
     * Creates the <i>Thin</i> script.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void createThinScript(Domain domain, RedmineService service) {
        def file = thinScriptFile
        def conf = redmineConfigTemplate.getText(true, "thinScriptConfig", "args", [
            properties: this,
        ])
        FileUtils.write file, conf, charset
        logg.serviceFileCreated this, file, conf
    }

    @Override
    void setScript(Object script) {
        super.setScript(script);
        this.thinConfigTemplates = templatesFactory.create("Thin_1_3_Config",
                [renderers: [
                        durationAttributeRenderer
                    ]])
        this.thinConfigTemplate = thinConfigTemplates.getResource("domain_service_config")
        this.redmineConfigTemplate = thinConfigTemplates.getResource("thin_config")
    }
}
