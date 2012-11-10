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
package com.anrisoftware.sscontrol.workers.text.tokentemplate

import org.junit.Before

import com.anrisoftware.sscontrol.workers.api.WorkerService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.service.TokensTemplateWorkerService
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test the tokens template worker as a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TokensTemplateServiceTest extends TokensTemplateTest {

	@Before
	void createFactories() {
		injector = createInjector()
		WorkerService service = ServiceLoader.load(WorkerService).find {
			it.info == TokensTemplateWorkerService.NAME
		}
		service.parent = injector
		factory = service.getWorker()
	}

	Injector createInjector() {
		Guice.createInjector()
	}
}
