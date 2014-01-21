/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.redirect.Redirect

/**
 * Redirect configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RedirectConfig {

    /**
     * The {@link Templates} for the script.
     */
    Templates redirectTemplates

    /**
     * Resource containing the Apache redirects configuration templates.
     */
    TemplateResource redirectConfigTemplate

    LinuxScript script

    def deployRedirect(Domain domain, Redirect redirect, List config) {
        enableMod "rewrite"
        deployDomainConfig domain, redirect, config
    }

    void deployDomainConfig(Domain domain, Redirect redirect, List config) {
        def properties = [:]
        properties.destination = redirect.destination
        properties.serverAlias = serverAlias redirect
        properties.proto = domainProto domain, redirect
        properties.namePattern = namePattern domain.name
        def configStr = redirectConfigTemplate.getText(
                true, "domainRedirects",
                "properties", properties)
        config << configStr
    }

    String serverAlias(Redirect redirect) {
        def dest = redirect.destination
        def name = redirect.domain.name
        int index = dest.indexOf(name)
        if (index > 0) {
            index = dest.indexOf "://"
            index != -1 ? dest.substring(index + 3) : dest
        }
    }

    String domainProto(Domain domain, Redirect redirect) {
        def dest = redirect.destination
        def name = redirect.domain.name
        int index = dest.indexOf("://")
        if (index == -1) {
            domain.proto
        }
    }

    String namePattern(String name) {
        name.replaceAll("\\.", "\\\\.");
    }

    void setScript(LinuxScript script) {
        this.script = script
        redirectTemplates = script.templatesFactory.create "Apache_2_2_Redirect"
        redirectConfigTemplate = redirectTemplates.getResource "config"
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
