package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.piwik.apache_2_2.ApacheFcgiPiwikConfig

/**
 * <i>Piwik Apache Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuApacheFcgiPiwikConfig extends ApacheFcgiPiwikConfig {

    Templates piwikTemplates

    TemplateResource domainConfigTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.piwikTemplates = factory.create "Piwik_Apache_Ubuntu_12_04"
        this.domainConfigTemplate = piwikTemplates.getResource "domain_config"
    }

    @Override
    TemplateResource getDomainConfigTemplate() {
        domainConfigTemplate
    }
}
