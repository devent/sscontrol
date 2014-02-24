package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_Config
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Gitit</i> configuration for <i>Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends Gitit_0_10_Config implements ServiceConfig {

    @Inject
    GititPropertiesProvider gititProperties

    @Inject
    UbuntuHsenvFromSourceConfig ubuntuHsenvFromSourceConfig

    Templates gititTemplates

    TemplateResource gititCommandTemplate

    TemplateResource gititServiceConfigTemplate

    TemplateResource gititServiceDefaultsConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        ubuntuHsenvFromSourceConfig.deployDomain domain, refDomain, service, config
        super.deployDomain domain, refDomain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        installPackages gititPackages
        ubuntuHsenvFromSourceConfig.deployService domain, service, config
        super.deployService domain, service, config
    }

    @Override
    String gititCommand(Domain domain, GititService service) {
        ubuntuHsenvFromSourceConfig.hsenvGititCommand domain, service
    }

    @Override
    TemplateResource getGititServiceTemplate() {
        gititServiceConfigTemplate
    }

    @Override
    TemplateResource getGititServiceDefaultsTemplate() {
        gititServiceDefaultsConfigTemplate
    }

    @Override
    ContextProperties getGititProperties() {
        gititProperties.get()
    }

    @Override
    String getProfile() {
        GititConfigFactory.PROFILE_NAME
    }

    @Override
    public void setScript(LinuxScript script) {
        super.setScript(script)
        ubuntuHsenvFromSourceConfig.setScript this
        gititTemplates = templatesFactory.create "Gitit_Ubuntu_12_04"
        gititCommandTemplate = gititTemplates.getResource "gititcommands"
        gititServiceConfigTemplate = gititTemplates.getResource "gititserviceconfig"
        gititServiceDefaultsConfigTemplate = gititTemplates.getResource "gititservicedefaultsconfig"
    }
}
