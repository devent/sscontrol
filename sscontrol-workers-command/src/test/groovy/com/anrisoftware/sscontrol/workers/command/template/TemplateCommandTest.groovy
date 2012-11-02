package com.anrisoftware.sscontrol.workers.command.template

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.template.api.TemplateServiceInfo
import com.anrisoftware.sscontrol.template.service.StTemplateService
import com.anrisoftware.sscontrol.workers.command.template.worker.TemplateCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.command.template.worker.TemplateCommandWorkerModule
import com.anrisoftware.sscontrol.workers.command.template.worker.TemplateCommandWorkerTemplateServiceModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute the template command worker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class TemplateCommandTest extends TestUtils {

	static commandTemplate

	Injector injector

	TemplateCommandWorkerFactory factory

	@Test
	void "execute command"() {
		def string = "Test"
		def worker = factory.create commandTemplate, "echo", [arg1: string]
		worker()
		assertStringContent worker.out, string
	}

	@Test
	void "serialize and execute command"() {
		def string = "Test"
		def worker = factory.create commandTemplate, "echo", [arg1: string]
		def workerB = reserialize worker
		workerB()
		assertStringContent workerB.out, string
	}

	@BeforeClass
	static void setupCommands() {
		switch (System.getProperty("os.name")) {
			case { (it.indexOf("nix") >= 0 || it.indexOf("nux") >= 0) }:
				commandTemplate = TestUtils.resourceURL("unix.stg", TemplateCommandTest)
				break
		}
	}

	@Before
	void createFactories() {
		injector = createInjector()
		factory = injector.getInstance TemplateCommandWorkerFactory
	}

	Injector createInjector() {
		Guice.createInjector(
						new TemplateCommandWorkerModule(),
						templateModule
						)
	}

	def getTemplateModule() {
		new TemplateCommandWorkerTemplateServiceModule(
						new TemplateServiceInfo(
						StTemplateService.NAME))
	}
}
