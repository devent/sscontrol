/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
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
