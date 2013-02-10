/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import com.anrisoftware.resources.templates.maps.TemplatesDefaultMapsModule;
import com.anrisoftware.resources.templates.templates.TemplatesResourcesModule;
import com.anrisoftware.resources.templates.worker.STDefaultPropertiesModule;
import com.anrisoftware.resources.templates.worker.STWorkerModule;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerModule;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerModule;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerModule;
import com.google.inject.AbstractModule;

/**
 * Binds the workers and resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		installWorkers();
		installResources();
	}

	private void installWorkers() {
		install(new ExecCommandWorkerModule());
		install(new ScriptCommandWorkerModule());
		install(new TokensTemplateWorkerModule());
	}

	private void installResources() {
		install(new TemplatesResourcesModule());
		install(new TemplatesDefaultMapsModule());
		install(new STWorkerModule());
		install(new STDefaultPropertiesModule());
	}

}
