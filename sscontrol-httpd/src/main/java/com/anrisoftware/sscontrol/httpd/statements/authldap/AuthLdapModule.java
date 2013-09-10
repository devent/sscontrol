/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd.
 * 
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.authldap;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the authentication factories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthLdapModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(AuthLdap.class,
				AuthLdap.class).build(AuthLdapFactory.class));
		install(new FactoryModuleBuilder().implement(AuthAttribute.class,
				AuthAttribute.class).build(AuthAttributeFactory.class));
		install(new FactoryModuleBuilder().implement(AuthHost.class,
				AuthHost.class).build(AuthHostFactory.class));
		install(new FactoryModuleBuilder().implement(Credentials.class,
				Credentials.class).build(CredentialsFactory.class));
		install(new FactoryModuleBuilder().implement(
				RequireLdapValidGroup.class, RequireLdapValidGroup.class)
				.build(RequireLdapValidGroupFactory.class));
	}

}