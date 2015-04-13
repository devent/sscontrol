/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04;

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Citadel Nginx Ubuntu 14.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Citadel_Nginx_Ubuntu_14_04_Config implements ServiceConfig {

    private LinuxScript script

    @Inject
    Webcit_8_Ubuntu_14_04_Config webcitConfig

    @Inject
    Citadel_8_Ubuntu_14_04_Config citadelConfig

    @Inject
    TimeoutDurationAttributeRenderer timeoutDurationAttributeRenderer

    @Inject
    InstallPackagesFactory installPackagesFactory

    TemplateResource citadelDomainTemplate

    @Inject
    final void setNginxTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Citadel_Nginx_Ubuntu_14_04_Config", ["renderers": [
                timeoutDurationAttributeRenderer
            ]]
        this.citadelDomainTemplate = templates.getResource "domain_config"
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        citadelConfig.setupDefaults service
        createDomainConfig domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        installPackages()
        citadelConfig.setupDefaults service
        citadelConfig.setupCitadel service
        citadelConfig.deployCerts service
        citadelConfig.restartCitadel service
        webcitConfig.deployWebcitDefaultConfig domain, service
        webcitConfig.restartWebcit service
        createDomainConfig domain, null, service, config
    }

    /**
     * Installs the <i>Citadel</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: citadelConfig.citadelPackages,
                system: systemName,
                this, threads)()
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
     *            the {@link CitadelService} service.
     *
     * @param config
     *            the {@link List} configuration.
     */
    void createDomainConfig(Domain domain, Domain refDomain, CitadelService service, List config) {
        def serviceLocation = serviceLocation service
        def args = [:]
        args.webcitAddress = webcitConfig.webcitAddress
        args.webcitHttpPort = webcitConfig.webcitHttpPort
        args.webcitLocation = serviceLocation
        args.listsubLocation = "$serviceLocation${webcitConfig.listsubLocation}"
        args.groupdavLocation = "$serviceLocation${webcitConfig.groupdavLocation}"
        args.freebusyLocation = "$serviceLocation${webcitConfig.freebusyLocation}"
        args.webcitprops = webcitConfig
        def configStr = citadelDomainTemplate.getText(true, "domainConfig", "args", args)
        config << configStr
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link CitadelService}.
     *
     * @return the location.
     */
    String serviceLocation(CitadelService service) {
        String location = service.alias == null ? "" : service.alias
        if (!location.startsWith("/")) {
            location = "/$location"
        }
        return location
    }

    /**
     * Returns the domain name as a file name.
     *
     * @param domain
     *            the {@link Domain}.
     *
     * @return the file name.
     */
    String domainNameAsFileName(Domain domain) {
        domain.name.replaceAll(/\./, "_")
    }

    @Override
    String getProfile() {
        Citadel_Nginx_Ubuntu_14_04_ConfigFactory.PROFILE_NAME
    }

    @Override
    String getServiceName() {
        Citadel_Nginx_Ubuntu_14_04_ConfigFactory.WEB_NAME
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
        webcitConfig.setScript this
        citadelConfig.setScript this
    }

    @Override
    LinuxScript getScript() {
        script
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
