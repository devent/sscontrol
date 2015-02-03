/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.apache_ubuntu_12_04

import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.apache_ubuntu_12_04.Ubuntu_12_04_ApachePhpmyadminConfigFactory.PROFILE_NAME

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.phpmyadmin.PhpmyadminService
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Ubuntu 12.04 phpMyAdmin</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends UbuntuPhpmyadminConfig implements ServiceConfig {

    public static final String NAME = "phpmyadmin"

    @Inject
    UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Inject
    UbuntuFcgiPhpmyadminConfig fcgiPhpmyadminConfig

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        fcgiPhpmyadminConfig.setScript script
        fcgiPhpmyadminConfig.deployDomain domain, refDomain, service, config
        createDomainConfig domain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        fcgiPhpmyadminConfig.setScript script
        fcgiPhpmyadminConfig.deployService domain, service, config
        setupDefaults service
        installPackages()
        fcgiPhpmyadminConfig.linkPhpconf domain
        deployConfiguration service
        reconfigureService()
        changeOwnerConfiguration domain
        importTables service
        createDomainConfig domain, service, config
    }

    void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
        def config = phpmyadminConfigTemplate.getText(
                true, "domainConfig",
                "domain", domain,
                "service", service,
                "properties", script,
                "fcgiProperties", fcgiPhpmyadminConfig.fcgiConfig)
        logg.domainConfigCreated script, domain, config
        serviceConfig << config
    }

    @Override
    String getProfile() {
        PROFILE_NAME
    }

    @Override
    String getServiceName() {
        NAME
    }

    @Override
    ContextProperties getMyadminProperties() {
        ubuntuPropertiesProvider.get()
    }
}
