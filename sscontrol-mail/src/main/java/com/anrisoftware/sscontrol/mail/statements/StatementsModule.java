/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.statements;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the statements factories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StatementsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(Domain.class, Domain.class).build(
						DomainFactory.class));
		install(new FactoryModuleBuilder().implement(Alias.class, Alias.class)
				.build(AliasFactory.class));
		install(new FactoryModuleBuilder().implement(Catchall.class,
				Catchall.class).build(CatchallFactory.class));
		install(new FactoryModuleBuilder().implement(User.class, User.class)
				.build(UserFactory.class));
		install(new FactoryModuleBuilder().implement(Database.class,
				Database.class).build(DatabaseFactory.class));
	}

}
