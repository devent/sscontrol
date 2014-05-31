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
