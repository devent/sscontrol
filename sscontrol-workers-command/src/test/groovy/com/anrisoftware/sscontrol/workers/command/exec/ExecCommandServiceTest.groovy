package com.anrisoftware.sscontrol.workers.command.exec

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.workers.api.WorkerService
import com.anrisoftware.sscontrol.workers.command.exec.service.ExecCommandWorkerService
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorkerFactory
import com.google.inject.Injector

/**
 * Test execute command worker as a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ExecCommandServiceTest extends ExecCommandTest {

	@Test
	void "execute echo command"() {
		super."execute echo command"()
	}

	@Test
	void "serialize and execute echo command"() {
		super."serialize and execute echo command"()
	}

	@Before
	void createFactories() {
		WorkerService service = ServiceLoader.load(WorkerService).find {
			it.info == ExecCommandWorkerService.NAME
		}
		factory = service.getWorker()
	}
}
