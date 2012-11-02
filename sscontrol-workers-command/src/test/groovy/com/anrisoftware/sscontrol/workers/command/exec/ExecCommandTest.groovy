package com.anrisoftware.sscontrol.workers.command.exec

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorkerModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Test execute command worker.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ExecCommandTest extends TestUtils {

	static echoCommand

	Injector injector

	ExecCommandWorkerFactory factory

	@Test
	void "execute echo command"() {
		def string = "Test"
		def worker = factory.create(String.format(echoCommand, string))
		worker()
		assertStringContent worker.out, string
	}

	@Test
	void "serialize and execute echo command"() {
		def string = "Test"
		def worker = factory.create(String.format(echoCommand, string))
		def workerB = reserialize worker
		workerB()
		assertStringContent workerB.out, string
	}

	@BeforeClass
	static void setupCommands() {
		switch (System.getProperty("os.name")) {
			case { (it.indexOf("nix") >= 0 || it.indexOf("nux") >= 0) }:
				echoCommand = "echo -n %s"
				break
		}
	}

	@Before
	void createFactories() {
		injector = createInjector()
		factory = injector.getInstance ExecCommandWorkerFactory
	}

	Injector createInjector() {
		Guice.createInjector(new ExecCommandWorkerModule())
	}
}
