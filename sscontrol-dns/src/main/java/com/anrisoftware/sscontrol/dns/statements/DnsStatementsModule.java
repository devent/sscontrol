package com.anrisoftware.sscontrol.dns.statements;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the DNS service statements factories.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DnsStatementsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(DnsZone.class,
				DnsZone.class).build(DnsZoneFactory.class));
		install(new FactoryModuleBuilder().implement(ARecord.class,
				ARecord.class).build(ARecordFactory.class));
		install(new FactoryModuleBuilder().implement(NSRecord.class,
				NSRecord.class).build(NSRecordFactory.class));
		install(new FactoryModuleBuilder().implement(MXRecord.class,
				MXRecord.class).build(MXRecordFactory.class));
		install(new FactoryModuleBuilder().implement(CNAMERecord.class,
				CNAMERecord.class).build(CNAMERecordFactory.class));
	}
}
