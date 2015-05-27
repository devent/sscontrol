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
package com.anrisoftware.sscontrol.httpd.fudforum.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.fudforum.apache_2_2.ApacheFcgiFudforumConfig

/**
 * <i>FUDForum Apache Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuApacheFcgiFudforumConfig extends ApacheFcgiFudforumConfig {

    Templates piwikTemplates

    TemplateResource domainConfigTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.piwikTemplates = factory.create "Fudforum_Apache_Ubuntu_12_04"
        this.domainConfigTemplate = piwikTemplates.getResource "domain_config"
    }

    @Override
    TemplateResource getDomainConfigTemplate() {
        domainConfigTemplate
    }

    @Override
    String getServiceName() {
        Ubuntu_12_04_ApacheFudforumConfigFactory.WEB_NAME
    }
}
