/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.apt

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.repo.service.RepoService

/**
 * Deploys the repository on an <i>apt</i> system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AptConfig {

    private Object script

    @Inject
    ParseAptSourcesListFactory parseAptSourcesListFactory

    @Inject
    DeployAptSourcesListFactory deployAptSourcesListFactory

    /**
     * Setups the default settings for the service.
     *
     * @param service
     *            the {@link RepoService} service.
     */
    void setupDefaults(RepoService service) {
    }

    /**
     * Reads the current sources list.
     *
     * @param service
     *            the {@link RepoService} service.
     */
    Map readSources(RepoService service) {
        parseAptSourcesListFactory.create(service, script).readSources()
    }

    /**
     * Deploys the sources list.
     *
     * @param service
     *            the {@link RepoService} service.
     *
     * @param sources
     *            the {@link Map} sources.
     */
    void deploySources(RepoService service, Map sources) {
        def deployApt = deployAptSourcesListFactory.create(service, sources, script)
        deployApt.deploySources()
        deployApt.deployEnableComponents()
    }

    void setScript(Object script) {
        this.script = script
    }

    Object getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
