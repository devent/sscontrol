/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerModule;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerModule;
import com.anrisoftware.sscontrol.workers.text.match.MatchTextWorkerModule;
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
	}

	private void installWorkers() {
		install(new ExecCommandWorkerModule());
		install(new ScriptCommandWorkerModule());
		install(new TokensTemplateWorkerModule());
		install(new MatchTextWorkerModule());
	}
}
