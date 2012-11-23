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

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule
import com.anrisoftware.resources.templates.worker.STWorkerModule
import com.anrisoftware.sscontrol.workers.command.script.worker.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.command.script.worker.ScriptCommandWorkerModule
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

	static system = SystemSelector.system.name

	Injector injector

	ScriptCommandWorkerFactory factory

	TemplatesFactory templatesFactory

	Templates templates

	@Test
	void "chmod files"() {
		withFiles "chmod", {
			def mod = "-w"
			def template = templates.getResource("chmod")
			def worker = factory.create template,
							system,
							"chmodCommand", "chmod",
							"mod", mod,
							"files", it.files
			worker()
			assert it.files[0].canWrite() == false
			assert it.files[1].canWrite() == false
		}, { }, 2
	}

	@Test
	void "serialize and chmod files"() {
		withFiles "chmod", {
			def mod = "-w"
			def templates = templatesFactory.create("ScriptCommandTemplates")
			def template = templates.getResource("chmod")
			def worker = factory.create template,
							system,
							"chmodCommand", "chmod",
							"mod", mod,
							"files", it.files
			def workerB = reserialize worker
			workerB()
			assert it.files[0].canWrite() == false
			assert it.files[1].canWrite() == false
		}, { }, 2
	}

	@Test
	void "chown files"() {
		withFiles "chown", {
			def owner = System.getProperty("user.name")
			def group = System.getProperty("user.name")
			def template = templates.getResource("chown")
			def worker = factory.create template,
							system,
							"chownCommand", "chown",
							"owner", owner,
							"ownerGroup", group,
							"files", it.files
			worker()
		}, { }, 2
	}

	@Test
	void "ln files"() {
		withFiles "ln", {
			def targets = it.files.inject([]) { list, value ->
				list << new File("${value.absolutePath}_target")
			}
			def mod = "-w"
			def template = templates.getResource("mkln")
			def worker = factory.create template,
							system,
							"lnCommand", "ln",
							"files", it.files,
							"targets", targets
			worker()
			targets.each { assert it.isFile() }
		}, { }, 2
	}

	@Before
	void createFactories() {
		injector = createInjector()
		factory = injector.getInstance ScriptCommandWorkerFactory
		templatesFactory = injector.getInstance TemplatesFactory
		templates = templatesFactory.create("ScriptCommandTemplates")
	}

	Injector createInjector() {
		Guice.createInjector(new ScriptCommandWorkerModule(),
						templateWorkerModule, templatesMapModule,
						templatesPropertiesModule)
	}

	/**
	 * Returns the module that binds the template worker.
	 */
	def getTemplateWorkerModule() {
		new STWorkerModule()
	}

	/**
	 * Returns the module that binds the template resources maps.
	 */
	def getTemplatesMapModule() {
		new TemplatesDefaultMapsModule()
	}

	/**
	 * Returns the module that binds the template properties.
	 */
	def getTemplatesPropertiesModule() {
		new STDefaultPropertiesModule()
	}
}
