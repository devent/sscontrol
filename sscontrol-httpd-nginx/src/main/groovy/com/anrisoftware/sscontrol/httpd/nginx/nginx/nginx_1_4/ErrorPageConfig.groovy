/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScript

/**
 * Error page configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ErrorPageConfig {

    /**
     * Template resource containing the error page configuration templates.
     */
    TemplateResource errorPageConfigTemplate

    NginxScript script

    /**
     * Creates the error page configuration.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param config
     *            the {@link List} of the configurations.
     */
    void deployConfig(Domain domain, List config) {
        def args = [:]
        args.domain = domain
        args.properties = script
        config << errorPageConfigTemplate.getText(
                true, "errorPage",
                "args", args)
        config << errorPageConfigTemplate.getText(
                true, "errorLocation",
                "args", args)
    }

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Nginx_1_4"
        this.errorPageConfigTemplate = templates.getResource "error_page_config"
    }

    void setScript(LinuxScript script) {
        this.script = script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
