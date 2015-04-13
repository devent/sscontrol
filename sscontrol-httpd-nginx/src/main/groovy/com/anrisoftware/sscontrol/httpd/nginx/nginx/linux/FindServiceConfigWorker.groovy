/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigInfo
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.google.inject.Injector
import com.google.inject.assistedinject.Assisted

/**
 * Find service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FindServiceConfigWorker {

    private final LinuxScript script

    private final Map<String, ServiceConfig> serviceConfigs

    @Inject
    private FindServiceConfigWorkerLogger log

    @Inject
    private Injector injector

    @Inject
    private WebServicesConfigProvider webServicesConfigProvider

    /**
     * @see FindServiceConfigWorkerFactory#create(LinuxScript)
     */
    @Inject
    FindServiceConfigWorker(Map<String, ServiceConfig> serviceConfigs, @Assisted LinuxScript script) {
        this.serviceConfigs = serviceConfigs
        this.script = script
        serviceConfigs.values().each { it.script = script }
    }

    /**
     * Finds the service configuration for the specified profile and service.
     *
     * @param profile
     *            the profile {@link String} name.
     *
     * @param service
     *            the {@link WebService}.
     *
     * @return the {@link ServiceConfig}.
     */
    ServiceConfig findServiceConfig(String profile, WebService service) {
        def config = serviceConfigs["${profile}.${service.name}"]
        config = config != null ? config : findWebServicesConfigProvider(profile, service)
        log.checkServiceConfig config, service, profile
        return config
    }

    private ServiceConfig findWebServicesConfigProvider(String profile, WebService service) {
        def factory = webServicesConfigProvider.find(
                [
                    getServiceName: { NginxScript.NGINX_NAME },
                    getWebName: { service.name },
                    getProfileName: { profile },
                    getWebService: { service }
                ] as ServiceConfigInfo)
        factory.setParent injector
        def serviceScript = factory.getScript()
        serviceScript.setScript script
        return serviceScript
    }
}
