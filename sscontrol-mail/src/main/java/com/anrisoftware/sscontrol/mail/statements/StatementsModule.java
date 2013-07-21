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
		install(new FactoryModuleBuilder().implement(BindAddresses.class,
				BindAddresses.class).build(BindAddressesFactory.class));
		install(new FactoryModuleBuilder().implement(CertificateFile.class,
				CertificateFile.class).build(CertificateFileFactory.class));
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
