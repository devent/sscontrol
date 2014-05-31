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
package com.anrisoftware.sscontrol.httpd.gitit.systemv

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.nginx_ubuntu_12_04.GititConfigFactory
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * <i>SystemV</i> <i>Gitit</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class SystemvService {

    @Inject
    SystemvServiceLogger logg

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    /**
     * The parent script that returns the properties.
     */
    Object script

    /**
     * Creates the <i>Gitit</i> service file.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void createService(Domain domain, GititService service) {
        def gitit = gititCommand domain, service
        def config = gititConfigFile domain, service
        def serviceFile = gititServiceFile domain
        def defaultsFile = gititServiceDefaultsFile domain
        def args = [:]
        args.gititScript = serviceFile
        args.userName = domain.domainUser.name
        def conf = gititServiceDefaultsTemplate.getText(true, "gititDefaults", "args", args)
        FileUtils.write defaultsFile, conf, charset
        logg.serviceDefaultsFileCreated script, defaultsFile, conf
        args.domainName = domainNameAsFileName domain
        args.gititCommand = gititCommand domain, service
        args.gititConfig = gititConfigFile domain, service
        args.gititDir = gititDir domain, service
        conf = gititServiceTemplate.getText(true, "gititService", "args", args)
        FileUtils.write serviceFile, conf, charset
        logg.serviceFileCreated this, serviceFile, conf
        changeFileModFactory.create(
                log: log, mod: "+x", files: serviceFile,
                command: script.chmodCommand,
                this, threads)()
    }

    /**
     * Activates the <i>Gitit</i> service.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void activateService(Domain domain, GititService service) {
        def name = gititServiceFile(domain).name
        scriptExecFactory.create(
                log: log, command: updateRcCommand, service: name,
                this, threads, activateServiceTemplate, "activateService")()
    }

    /**
     * Restarts the <i>Gitit</i> service.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link GititService}.
     */
    void restartService(Domain domain, GititService service) {
        File command = gititServiceFile(domain)
        if (!command.canExecute()) {
            command.setExecutable true
        }
        scriptExecFactory.create(
                log: log, command: command,
                this, threads, restartServiceTemplate, "restartService")()
    }

    /**
     * Returns the <i>Gitit</i> service name.
     */
    String getServiceName() {
        GititConfigFactory.WEB_NAME
    }

    /**
     * Sets the parent script that returns the properties.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the resources containing the <i>gitit</i> service defaults
     * configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititServiceDefaultsTemplate()

    /**
     * Returns the resources containing the <i>gitit</i> service
     * configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getGititServiceTemplate()

    /**
     * Returns the resources containing to activate a service template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getActivateServiceTemplate()

    /**
     * Returns the resources containing to restart the service template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getRestartServiceTemplate()

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

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName()).toString();
    }
}
