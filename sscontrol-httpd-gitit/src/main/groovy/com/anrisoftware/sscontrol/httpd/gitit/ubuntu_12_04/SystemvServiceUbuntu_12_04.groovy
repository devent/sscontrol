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
package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.gitit.systemv.SystemvService

/**
 * <i>Ubuntu 12.04</i> <i>SystemV</i> <i>Gitit</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SystemvServiceUbuntu_12_04 extends SystemvService {

    @Inject
    TemplatesFactory templatesFactory

    Templates templates

    TemplateResource serviceDefaultsTemplate

    TemplateResource serviceTemplate

    @Override
    TemplateResource getGititServiceDefaultsTemplate() {
        return serviceDefaultsTemplate
    }

    @Override
    TemplateResource getGititServiceTemplate() {
        return serviceTemplate
    }

    @Override
    void setScript(Object script) {
        super.setScript(script);
        this.templates = templatesFactory.create "Gitit_Ubuntu_12_04"
        this.serviceDefaultsTemplate = templates.getResource "gitit_service_defaults"
        this.serviceTemplate = templates.getResource "gitit_service"
    }
}
