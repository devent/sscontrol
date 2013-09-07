package com.anrisoftware.sscontrol.ldap.statements;

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
		install(new FactoryModuleBuilder().implement(Admin.class, Admin.class)
				.build(AdminFactory.class));
	}

}
