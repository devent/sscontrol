/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
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
