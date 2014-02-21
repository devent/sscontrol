package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04;

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
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

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        installPackages gititPackages
    }

    @Override
    ContextProperties getGititProperties() {
        gititProperties.get()
    }

    @Override
    String getProfile() {
        GititConfigFactory.PROFILE_NAME
    }
}
