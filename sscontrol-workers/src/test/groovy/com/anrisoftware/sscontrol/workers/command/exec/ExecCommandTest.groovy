package com.anrisoftware.sscontrol.workers.command.exec

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.anrisoftware.globalpom.utils.TestUtils
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerFactory;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerModule;
import com.google.inject.Guice
import com.google.inject.Injector

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
