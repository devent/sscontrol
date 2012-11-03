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
package com.anrisoftware.sscontrol.workers.command.template

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.workers.api.WorkerService
import com.anrisoftware.sscontrol.workers.command.template.service.TemplateCommandWorkerService
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute the template command worker as a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class TemplateCommandServiceTest extends TemplateCommandTest {

	@Test
	void "execute command"() {
		super."execute command"()
	}

	@Test
	void "serialize and execute command"() {
		super."serialize and execute command"()
	}

	@Before
	void createFactories() {
		injector = createInjector()
		WorkerService service = ServiceLoader.load(WorkerService).find {
			it.info == TemplateCommandWorkerService.NAME
		}
		service.parent = injector
		factory = service.getWorker()
	}

	Injector createInjector() {
		Guice.createInjector(templateModule)
	}
}
