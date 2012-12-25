package com.anrisoftware.sscontrol.firewall.statements;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the firewall service statement factories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class FirewallStatementsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(
				AllowDefault.class, AllowDefault.class)
				.build(AllowDefaultFactory.class));
		install(new FactoryModuleBuilder().implement(
				DenyDefault.class, DenyDefault.class).build(
				DenyDefaultFactory.class));
		install(new FactoryModuleBuilder().implement(AllowPort.class,
				AllowPort.class)
				.build(AllowPortFactory.class));
		install(new FactoryModuleBuilder().implement(AllowFrom.class,
				AllowFrom.class)
				.build(AllowFromFactory.class));
		install(new FactoryModuleBuilder().implement(DenyPort.class,
				DenyPort.class).build(DenyPortFactory.class));
		install(new FactoryModuleBuilder().implement(DenyFrom.class,
				DenyFrom.class).build(DenyFromFactory.class));
		install(new FactoryModuleBuilder().implement(Port.class, Port.class)
				.build(PortFactory.class));
		install(new FactoryModuleBuilder().implement(Address.class,
				Address.class).build(AddressFactory.class));
	}
}
