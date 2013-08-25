package com.anrisoftware.sscontrol.httpd.statements;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the httpd script statements factories.
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
		install(new FactoryModuleBuilder().implement(Redirect.class,
				Redirect.class).build(RedirectFactory.class));
	}

}
