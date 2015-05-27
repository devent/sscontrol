/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>FUDForum 3</i> install file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fudforum_3_InstallFile {

    private Object script

    @Inject
    private Fudforum_3_InstallFileLogger log

    @Inject
    private DatabaseTypeRenderer databaseTypeRenderer

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    private TemplateResource configTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Fudforum_3_Config", ["renderers": [databaseTypeRenderer]]
        this.configTemplate = templates.getResource "install_ini"
    }

    /**
     * Creates the <i>FUDForum</i> service directories.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void deployConfig(Domain domain, FudforumService service) {
        File file = fudforumInstallFile(domain, service)
        def args = [:]
        args.site = service.site
        args.installDirectory = fudforumDir(domain, service)
        args.dataDirectory = fudforumDir(domain, service)
        args.databaseHost = service.database["host"]
        args.databaseUser = service.database["user"]
        args.databasePassword = service.database["password"]
        args.databaseName = service.database["database"]
        args.databaseTablePrefix = service.database["prefix"]
        args.databaseType = service.database["type"]
        args.domainName = domain.name
        args.language = service.language
        args.template = service.template
        args.rootLogin = service.rootLogin
        args.rootPassword = service.rootPassword
        args.rootEmail = service.rootEmail
        String config = configTemplate.getText(true, "installIni", "args", args)
        FileUtils.write file, config, charset
        log.configDeployed this, domain, config, file
    }

    /**
     * Returns the <i>FUDForum</i> service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
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
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
