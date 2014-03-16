/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.command.script

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.apache.commons.io.FileUtils
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.templates.TemplatesResourcesModule
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.worker.STWorkerModule
import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerModule
import com.anrisoftware.sscontrol.workers.command.utils.SystemSelector
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute script with the script command worker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ScriptCommandTest {

    @Test
    void "chmod files"() {
        def mod = "-w"
        def worker = factory.create templates.getResource("chmod"),
                system, "args", ["command": "chmod", "mod": mod, "files": files]
        worker()
        assert files[0].canWrite() == false
        assert files[1].canWrite() == false
    }

    @Test
    void "serialize and chmod files"() {
        def mod = "-w"
        def worker = factory.create templates.getResource("chmod"),
                system, "args", ["command": "chmod", "mod": mod, "files": files]
        def workerB = reserialize worker
        injector.injectMembers workerB
        workerB()
        assert files[0].canWrite() == false
        assert files[1].canWrite() == false
    }

    File tmpdir

    List files

    @Before
    void createTmpDir() {
        tmpdir = File.createTempDir("ScriptCommandTest", null)
        files = [
            new File(tmpdir, "one"),
            new File(tmpdir, "two")
        ]
        files.each { FileUtils.touch(it) }
    }

    @After
    void deleteTmpDir() {
        tmpdir.deleteDir()
    }

    static system = SystemSelector.system.name

    static Injector injector

    static ScriptCommandWorkerFactory factory

    static TemplatesFactory templatesFactory

    static Templates templates

    @BeforeClass
    static void createFactories() {
        toStringStyle
        injector = createInjector()
        factory = injector.getInstance ScriptCommandWorkerFactory
        templatesFactory = injector.getInstance TemplatesFactory
        templates = templatesFactory.create("ScriptCommandTemplates")
    }

    static Injector createInjector() {
        Guice.createInjector(
                new ScriptCommandWorkerModule(),
                new ExecCommandWorkerModule(),
                new TextsResourcesDefaultModule(),
                new TemplatesResourcesModule(),
                new STWorkerModule(),
                new TemplatesDefaultMapsModule(),
                new STDefaultPropertiesModule())
    }

    static {
        toStringStyle
    }
}
