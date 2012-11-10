/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
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

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.workers.api.WorkerService
import com.anrisoftware.sscontrol.workers.command.script.service.ScriptCommandWorkerService
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute the script command worker as a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ScriptCommandServiceTest extends ScriptCommandTest {

	@Test
	void "chmod files"() {
		super."chmod files"()
	}

	@Test
	void "serialize and chmod files"() {
		super."serialize and chmod files"()
	}

	@Test
	void "chown files"() {
		super."chown files"()
	}

	@Test
	void "ln files"() {
		super."ln files"()
	}

	@Before
	void createFactories() {
		injector = createInjector()
		WorkerService service = ServiceLoader.load(WorkerService).find {
			it.info == ScriptCommandWorkerService.NAME
		}
		service.parent = injector
		factory = service.getWorker()
	}

	Injector createInjector() {
		Guice.createInjector(templateModule)
	}
}
