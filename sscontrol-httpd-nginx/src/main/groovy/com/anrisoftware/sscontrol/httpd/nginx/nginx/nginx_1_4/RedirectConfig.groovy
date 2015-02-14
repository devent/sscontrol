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
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScript
import com.anrisoftware.sscontrol.httpd.redirect.Redirect

/**
 * Redirect configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RedirectConfig {

    @Inject
    TemplatesFactory templatesFactory

    /**
     * The {@link Templates} for the script.
     */
    Templates redirectTemplates

    /**
     * Resource containing the Nginx redirects configuration templates.
     */
    TemplateResource redirectConfigTemplate

    NginxScript script

    def deployRedirect(Domain domain, Redirect redirect, List config) {
        deployDomainConfig domain, redirect, config
    }

    void deployDomainConfig(Domain domain, Redirect redirect, List config) {
        def args = [:]
        args.redirect = redirect
        args.proto = domainProto domain, redirect
        def configStr = redirectConfigTemplate.getText(
                true, "domainRedirects",
                "args", args)
        config << configStr
    }

    String domainProto(Domain domain, Redirect redirect) {
        def dest = redirect.destination
        def name = redirect.domain.name
        int index = dest.indexOf("://")
        if (index == -1) {
            domain.proto
        }
    }

    void setScript(LinuxScript script) {
        this.script = script
        redirectTemplates = templatesFactory.create "Nginx_1_4_Redirect"
        redirectConfigTemplate = redirectTemplates.getResource "config"
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
