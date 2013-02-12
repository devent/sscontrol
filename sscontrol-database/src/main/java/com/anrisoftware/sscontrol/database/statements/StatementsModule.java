package com.anrisoftware.sscontrol.database.statements;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Binds the database statements factories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.8
 */
public class StatementsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Database.class,
				Database.class).build(DatabaseFactory.class));
		install(new FactoryModuleBuilder().implement(User.class, User.class)
				.build(UserFactory.class));
	}
}
