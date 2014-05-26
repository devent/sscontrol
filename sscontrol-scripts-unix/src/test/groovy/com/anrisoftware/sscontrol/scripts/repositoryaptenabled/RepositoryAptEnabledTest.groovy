/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.repositoryaptenabled

import groovy.util.logging.Slf4j

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.worker.STWorkerModule
import com.anrisoftware.sscontrol.scripts.unix.TestThreadsPropertiesProvider
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule.ExecCommandModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see RepositoryAptEnabled
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RepositoryAptEnabledTest {

    @Test
    void "not contains repository"() {
        def file = createSourcesListFile sourcesListResource
        def repositoryEnabled = repositoryEnabledFactory.create(
                log: log,
                repository: "universe",
                distributionName: "precise",
                packagesSourcesFile: file,
                this, threads)()
        assert repositoryEnabled.contains == false
    }

    @Test
    void "contains repository"() {
        def file = createSourcesListFile sourcesListResourceWithRep
        def repositoryEnabled = repositoryEnabledFactory.create(
                log: log,
                repository: "universe",
                distributionName: "precise",
                packagesSourcesFile: file,
                this, threads)()
        assert repositoryEnabled.contains == true
    }

    static Injector injector

    static RepositoryAptEnabledFactory repositoryEnabledFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    static TemplateResource echoScriptTemplate

    static URL sourcesListResource = RepositoryAptEnabledTest.class.getResource("sources_list.txt")

    static URL sourcesListResourceWithRep = RepositoryAptEnabledTest.class.getResource("sources_list_withrep.txt")

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new UnixScriptsModule(),
                new RepositoryAptEnabledModule(),
                new UnixScriptsModule.ExecCommandModule(),
                new PropertiesThreadsModule(),
                new TemplatesResourcesModule(),
                new TemplatesDefaultMapsModule(),
                new STWorkerModule(),
                new STDefaultPropertiesModule())
        repositoryEnabledFactory = injector.getInstance RepositoryAptEnabledFactory
        threadsFactory = injector.getInstance PropertiesThreadsFactory
        threadsPoolProvider = injector.getInstance TestThreadsPropertiesProvider
    }

    @Before
    void createThreadsPool() {
        threads = threadsFactory.create();
        threads.setProperties threadsPoolProvider.get()
        threads.setName("script");
    }

    File createSourcesListFile(URL resource) {
        def dir = tmp.newFolder()
        def file = new File(dir, "sources.list")
        FileUtils.write file, IOUtils.toString(resource)
        return file
    }
}