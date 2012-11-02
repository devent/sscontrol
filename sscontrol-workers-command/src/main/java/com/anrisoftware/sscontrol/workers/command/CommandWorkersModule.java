package com.anrisoftware.sscontrol.workers.command;

import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorker;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the command worker classes.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class CommandWorkersModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(ExecCommandWorker.class,
				ExecCommandWorker.class).build(ExecCommandWorkerFactory.class));
	}
}
