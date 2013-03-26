/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.workers.command.script;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.System.getProperties;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * Installs the script command worker factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ScriptCommandWorkerModule extends AbstractModule {

	/**
	 * The name of the script command worker binding.
	 */
	public static final String NAME = "exec_command_worker";

	private static final URL resource = ScriptCommandWorkerModule.class
			.getResource("script_command_worker.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(ScriptCommandWorker.class,
				ScriptCommandWorker.class).build(
				ScriptCommandWorkerFactory.class));
		bindWorkerFactory();
	}

	private void bindWorkerFactory() {
		MapBinder<String, WorkerFactory> binder;
		binder = newMapBinder(binder(), String.class, WorkerFactory.class);
		binder.addBinding(NAME).to(ScriptCommandWorkerFactory.class);
	}

	@Provides
	@Singleton
	@Named("script-command-worker-properties")
	Properties getScriptCommandWorkerProperties() throws IOException {
		return new ContextPropertiesFactory(ScriptCommandWorker.class)
				.withProperties(getProperties()).fromResource(resource);
	}
}
