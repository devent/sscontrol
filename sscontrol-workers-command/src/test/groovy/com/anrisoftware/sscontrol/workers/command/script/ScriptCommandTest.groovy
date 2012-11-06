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

import static com.anrisoftware.globalpom.utils.TestUtils.*

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.template.api.TemplateServiceInfo
import com.anrisoftware.sscontrol.template.service.StTemplateService
import com.anrisoftware.sscontrol.workers.command.script.worker.ScriptCommandWorker
import com.anrisoftware.sscontrol.workers.command.script.worker.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.command.script.worker.ScriptCommandWorkerModule
import com.anrisoftware.sscontrol.workers.command.template.worker.TemplateCommandWorkerTemplateServiceModule
import com.anrisoftware.sscontrol.workers.command.utils.SystemSelector
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute the template command worker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ScriptCommandTest {

	static system = SystemSelector.system.name

	static chmodTemplate = resourceURL("chmod.stg", ScriptCommandWorker)

	static mklnTemplate = resourceURL("mkln.stg", ScriptCommandWorker)

	Injector injector

	ScriptCommandWorkerFactory factory

	@Test
	void "chmod files"() {
		withFiles 2, "chmod", {
			def mod = "-w"
			def worker = factory.create chmodTemplate, system,
							[chmodCommand: "chmod", mod: mod, files: it.files]
			worker()
			assert it.files[0].canWrite() == false
			assert it.files[1].canWrite() == false
		}
	}

	@Test
	void "ln files"() {
		withFiles 2, "ln", {
			def targets = it.files.inject([]) { list, value ->
				list << new File("${value.absolutePath}_target")
			}
			def mod = "-w"
			def worker = factory.create mklnTemplate, system,
							[lnCommand: "ln", files: it.files, targets: targets]
			worker()
			targets.each { assert it.isFile() }
		}
	}

	@Before
	void createFactories() {
		injector = createInjector()
		factory = injector.getInstance ScriptCommandWorkerFactory
	}

	Injector createInjector() {
		Guice.createInjector(
						new ScriptCommandWorkerModule(),
						templateModule
						)
	}

	def getTemplateModule() {
		new TemplateCommandWorkerTemplateServiceModule(
						new TemplateServiceInfo(
						StTemplateService.NAME))
	}
}
