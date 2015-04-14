/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.resources.ToURLFactory
import com.anrisoftware.sscontrol.repo.service.RepoService
import com.anrisoftware.sscontrol.scripts.signrepo.SignRepoFactory

/**
 * Deploys the repository on an <i>apt</i> system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class AptConfig {

    private Object script

    @Inject
    ParseAptSourcesListFactory parseAptSourcesListFactory

    @Inject
    DeployAptSourcesListFactory deployAptSourcesListFactory

    @Inject
    SignRepoFactory signRepoFactory

    @Inject
    ToURLFactory toURLFactory

    @Inject
    DeployAptProxyFactory deployAptProxyFactory

    /**
     * Signs the repositories.
     *
     * @param service
     *            the {@link RepoService} service.
     */
    void signRepositories(RepoService service) {
        service.signKeys?.each { name, key ->
            def resource = toURLFactory.create().convert(key)
            signRepoFactory.create(
                    log: log,
                    command: aptkeyCommand,
                    key: resource,
                    name: name,
                    system: systemName,
                    tmp: tmpDirectory,
                    this, threads)()
        }
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

    /**
     * Deploys the proxy.
     *
     * @param service
     *            the {@link RepoService} service.
     */
    void deployProxy(RepoService service) {
        def deployApt = deployAptProxyFactory.create(service, script)
        deployApt.deployProxy()
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
