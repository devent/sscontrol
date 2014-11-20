/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.wordpress.apache_2_2.ApacheFcgiWordpressConfig

/**
 * <i>Ubuntu 12.04</i> fcgi <i>Wordpress</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuFcgiWordpressConfig extends ApacheFcgiWordpressConfig {

    @Inject
    TemplatesFactory templatesFactory

    Templates domainConfigTemplates

    TemplateResource domainConfigTemplate

    TemplateResource getDomainConfigTemplate() {
        domainConfigTemplate
    }

    @Override
    void setScript(Object script) {
        super.setScript script
        this.domainConfigTemplates = templatesFactory.create("Wordpress_3")
        this.domainConfigTemplate = domainConfigTemplates.getResource("domain")
    }
}
