/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import com.anrisoftware.sscontrol.core.bindings.BindingsModule;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingModule;
import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.database.statements.StatementsModule;
import com.google.inject.AbstractModule;

/**
 * Binds the database service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new StatementsModule());
		install(new DebugLoggingModule());
		install(new BindingsModule());
		install(new ListModule());
	}
}
