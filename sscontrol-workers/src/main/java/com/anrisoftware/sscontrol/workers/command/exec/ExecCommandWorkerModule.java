package com.anrisoftware.sscontrol.workers.command.exec;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the execute command worker factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ExecCommandWorkerModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(ExecCommandWorker.class,
				ExecCommandWorker.class).build(ExecCommandWorkerFactory.class));
	}
}
