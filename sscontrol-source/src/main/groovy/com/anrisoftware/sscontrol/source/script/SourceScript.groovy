/*
 * Copyright 2015-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source.
 *
 * sscontrol-source is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.script

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.exec.runcommands.RunCommands
import com.anrisoftware.globalpom.exec.runcommands.RunCommandsFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.source.service.SourceService;
import com.anrisoftware.sscontrol.source.service.SourceServiceConfig;
import com.anrisoftware.sscontrol.source.service.SourceServiceConfigInfo;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;
import com.google.inject.Injector

/**
 * <i>Source</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class SourceScript extends LinuxScript {

    private static final String SERVICE_NAME = "source"

    @Inject
    private SourceScriptLogger logg

    private RunCommands runCommands

    @Inject
    Injector injector

    @Inject
    Map<String, SourceServiceConfig> serviceConfigs

    @Inject
    SourceServicesConfigProvider servicesConfigProvider

    @Inject
    final void setRunCommands(RunCommandsFactory factory) {
        this.runCommands = factory.create this, SERVICE_NAME
    }

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
    void deployConfigs(SourceService service) {
        def logg = logg
        def profile = profileName
        service.services.each { SourceSetupService sec ->
            def config = findServiceConfig profile, sec
            config.deployService sec
            logg.securityServiceDeployed this, config
        }
    }

    /**
     * Returns the run commands.
     */
    final RunCommands getRunCommands() {
        runCommands
    }

    /**
     * Returns the service name.
     */
    final String getServiceName() {
        return SERVICE_NAME
    }

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
    SourceServiceConfig findServiceConfig(String profile, SourceSetupService service) {
        def config = serviceConfigs["${profile}.${service.name}"]
        config = config != null ? config : findServicesConfigProvider(profile, service)
        logg.checkServiceConfig config, service, profile
        return config
    }

    private SourceServiceConfig findServicesConfigProvider(String profile, SourceSetupService service) {
        def factory = servicesConfigProvider.find(
                [
                    getSourceName: { service.name },
                    getProfileName: { profile },
                    getSourceService: { service }
                ] as SourceServiceConfigInfo)
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
