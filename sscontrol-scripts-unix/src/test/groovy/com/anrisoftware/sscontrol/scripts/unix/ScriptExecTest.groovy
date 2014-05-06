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
package com.anrisoftware.sscontrol.scripts.unix

import groovy.util.logging.Slf4j

import java.util.concurrent.TimeoutException

import org.joda.time.Duration
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.threads.api.Threads
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsModule
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.worker.STWorkerModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see ScriptExec
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class ScriptExecTest {

    @Test
    void "exec script"() {
        def scriptExec = scriptExecFactory.create(
                log: log, text: "foo", this, threads, echoScriptTemplate, "echo")()
    }

    @Test(expected = TimeoutException)
    void "exec script timeout"() {
        def scriptExec = scriptExecFactory.create(
                log: log, text: "foo", sleep: 5, timeout: Duration.standardSeconds(1),
                this, threads, echoScriptTemplate, "echoTimeout")()
    }

    static Injector injector

    static ScriptExecFactory scriptExecFactory

    static PropertiesThreadsFactory threadsFactory

    static TestThreadsPropertiesProvider threadsPoolProvider

    static TemplateResource echoScriptTemplate

    Threads threads

    @BeforeClass
    static void createFactory() {
        injector = Guice.createInjector(
                new UnixScriptsModule(),
                new UnixScriptsModule.ExecCommandModule(),
                new PropertiesThreadsModule(),
                new TemplatesResourcesModule(),
                new TemplatesDefaultMapsModule(),
                new STWorkerModule(),
                new STDefaultPropertiesModule())
        scriptExecFactory = injector.getInstance ScriptExecFactory
        threadsFactory = injector.getInstance PropertiesThreadsFactory
        threadsPoolProvider = injector.getInstance TestThreadsPropertiesProvider
        loadTemplates()
    }

    static void loadTemplates() {
        Templates templates = injector.getInstance(TemplatesFactory).create("ScriptExecTest")
        echoScriptTemplate = templates.getResource "echo_script"
    }

    @Before
    void createThreadsPool() {
        threads = threadsFactory.create();
        threads.setProperties threadsPoolProvider.get()
        threads.setName("script");
    }
}
