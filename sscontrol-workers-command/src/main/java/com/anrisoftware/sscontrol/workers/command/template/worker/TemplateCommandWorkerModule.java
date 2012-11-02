package com.anrisoftware.sscontrol.workers.command.template.worker;

import java.util.ServiceLoader;

import javax.inject.Singleton;

import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.api.WorkerService;
import com.anrisoftware.sscontrol.workers.command.exec.service.ExecCommandWorkerService;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the template command worker factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class TemplateCommandWorkerModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(
				TemplateCommandWorker.class, TemplateCommandWorker.class)
				.build(TemplateCommandWorkerFactory.class));
		bind(WorkerFactory.class).to(TemplateCommandWorkerFactory.class);
	}

	@Provides
	@Singleton
	ExecCommandWorkerFactory getExecCommandWorkerFactory()
			throws WorkerException {
		WorkerService service = getExecCommandWorkerService();
		return service.getWorker();
	}

	private WorkerService getExecCommandWorkerService() throws WorkerException {
		String name = ExecCommandWorkerService.NAME;
		for (WorkerService service : ServiceLoader.load(WorkerService.class)) {
			if (service.getInfo().equals(name)) {
				return service;
			}
		}
		throw new WorkerException(
				"Could not find the execute command worker service")
				.addContextValue("name", name);
	}
}
