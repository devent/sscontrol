/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-thin.
 *
 * sscontrol-httpd-thin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-thin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-thin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.thin.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserAddFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Thin 1.3</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Thin_1_3_Config extends AbstractThinConfig {

    @Inject
    Thin_1_3_ConfigLogger logg

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    DurationAttributeRenderer durationAttributeRenderer

    @Inject
    RestartServicesFactory restartServicesFactory

    TemplateResource domainConfigTemplate

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
        createThinUser domain, service
        createThinDefaults domain, service
        createThinScript domain, service
        createDomainServiceConfig domain, null, service
    }

    /**
     * Restarts <i>Thin</i> services.
     */
    void restartServices() {
        restartServicesFactory.create(
                log: log,
                command: thinRestartCommand,
                services: thinRestartServices,
                this, threads)()
    }

    /**
     * Creates the <i>Thin</i> configuration for the service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void createThinUser(Domain domain, WebService service) {
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
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void createDomainServiceConfig(Domain domain, Domain refDomain, WebService service) {
        def file = domainConfigurationFile domain, service
        def conf = domainConfigTemplate.getText(true, "domainServiceConfig", "args", [
            properties: this,
            domain: domain,
            chdir: serviceDir(domain, refDomain, service),
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
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void createThinDefaults(Domain domain, WebService service) {
        def file = thinDefaultsFile
        def conf = thinConfigTemplate.getText(true, "thinDefaultsConfig", "args", [
            properties: this,
        ])
        FileUtils.write file, conf, charset
        logg.serviceFileCreated this, file, conf
    }

    /**
     * Creates the <i>Thin</i> script.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void createThinScript(Domain domain, WebService service) {
        def file = thinScriptFile
        def conf = thinConfigTemplate.getText(true, "thinScriptConfig", "args", [
            properties: this,
        ])
        FileUtils.write file, conf, charset
        logg.serviceFileCreated this, file, conf
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                files: file,
                mod: "+x",
                this, threads)()
    }

    /**
     * Returns the service directory.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the referenced {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link WebService} service.
     */
    abstract String serviceDir(Domain domain, Domain refDomain, WebService service)

    @Inject
    void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create("Thin_1_3_Config",
                [renderers: [
                        durationAttributeRenderer
                    ]])
        this.domainConfigTemplate = templates.getResource("domain_service_config")
        this.thinConfigTemplate = templates.getResource("thin_config")
    }
}
