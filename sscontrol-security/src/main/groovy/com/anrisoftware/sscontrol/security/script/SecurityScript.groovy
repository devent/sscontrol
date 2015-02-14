/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.script

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.security.service.SecService
import com.anrisoftware.sscontrol.security.service.SecurityService
import com.anrisoftware.sscontrol.security.service.ServiceConfig
import com.anrisoftware.sscontrol.security.service.ServiceConfigInfo
import com.google.inject.Injector

/**
 * <i>Security</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class SecurityScript extends LinuxScript {

    private static final String SERVICE_NAME = "security"

    @Inject
    private SecurityScriptLogger logg

    @Inject
    Injector injector

    @Inject
    Map<String, ServiceConfig> serviceConfigs

    @Inject
    SecServicesConfigProvider servicesConfigProvider

    @Override
    def run() {
        serviceConfigs.each { it.value.script = this }
        deployConfigs service
    }

    /**
     * Deploys the configurations.
     *
     * @param service
     *            the {@link SecurityService} service.
     */
    void deployConfigs(SecurityService service) {
        def logg = logg
        def profile = profileName
        service.services.each { SecService sec ->
            def config = findServiceConfig profile, sec
            config.deployService sec
            logg.securityServiceDeployed this, config
        }
    }

    /**
     * Returns the service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfileName()

    /**
     * Finds the service configuration for the specified profile and service.
     *
     * @param profile
     *            the profile {@link String} name.
     *
     * @param service
     *            the {@link SecService}.
     *
     * @return the {@link ServiceConfig}.
     */
    ServiceConfig findServiceConfig(String profile, SecService service) {
        def config = serviceConfigs["${profile}.${service.name}"]
        config = config != null ? config : findServicesConfigProvider(profile, service)
        logg.checkServiceConfig config, service, profile
        return config
    }

    private ServiceConfig findServicesConfigProvider(String profile, SecService service) {
        def factory = servicesConfigProvider.find(
                [
                    getServiceName: { SERVICE_NAME },
                    getSecName: { service.name },
                    getProfileName: { profile },
                    getSecService: { service }
                ] as ServiceConfigInfo)
        factory.setParent injector
        def script = factory.getScript()
        script.setScript this
        return script
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
