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
package com.anrisoftware.sscontrol.workers.command.exec

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.resources.texts.defaults.TextsResourcesDefaultModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute command worker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecCommandTest {

    @Test
    void "execute echo command"() {
        def string = "Test"
        def worker = factory.create(String.format(echoCommand, string))
        worker()
        assertStringContent worker.out, string
    }

    @Test
    void "execute error command"() {
        def string = "Test"
        def worker = factory.create(String.format(errorCommand, string))
        shouldFailWith(CommandException) { worker() }
    }

    @Test
    void "serialize and execute echo command"() {
        def string = "Test"
        def worker = factory.create(String.format(echoCommand, string))
        def workerB = reserialize(worker)
        workerB()
        assertStringContent workerB.out, string
    }

    static String echoCommand

    static String errorCommand

    Injector injector

    ExecCommandWorkerFactory factory

    @BeforeClass
    static void setupCommands() {
        switch (System.getProperty("os.name")) {
            case {
                (it.indexOf("nix") >= 0 || it.indexOf("nux") >= 0)
            }:
                echoCommand = "echo -n %s"
                errorCommand = 'bash -c "echo Hello"'
                break
        }
    }

    @Before
    void createFactories() {
        injector = createInjector()
        factory = injector.getInstance ExecCommandWorkerFactory
    }

    Injector createInjector() {
        Guice.createInjector(new ExecCommandWorkerModule(),
                new TextsResourcesDefaultModule())
    }

    static {
        toStringStyle
    }
}
