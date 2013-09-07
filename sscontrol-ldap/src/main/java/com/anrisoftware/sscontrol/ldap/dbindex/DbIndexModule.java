package com.anrisoftware.sscontrol.ldap.dbindex;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the database index factory.
 * 
 * @see DbIndexFactory
 * @see DbIndexFormatFactory
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DbIndexModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(DbIndex.class,
				DbIndex.class).build(DbIndexFactory.class));
		install(new FactoryModuleBuilder().implement(DbIndexFormat.class,
				DbIndexFormat.class).build(DbIndexFormatFactory.class));
	}

}
