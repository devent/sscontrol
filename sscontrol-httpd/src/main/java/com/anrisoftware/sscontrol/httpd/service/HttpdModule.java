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
package com.anrisoftware.sscontrol.httpd.service;

import com.anrisoftware.sscontrol.httpd.statements.auth.AuthModule;
import com.anrisoftware.sscontrol.httpd.statements.authfile.AuthFileModule;
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapModule;
import com.anrisoftware.sscontrol.httpd.statements.domain.DomainModule;
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminModule;
import com.anrisoftware.sscontrol.httpd.statements.redirect.RedirectModule;
import com.google.inject.AbstractModule;

/**
 * Installs the httpd script statements.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HttpdModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new DomainModule());
		install(new RedirectModule());
		install(new AuthModule());
		install(new AuthFileModule());
		install(new AuthLdapModule());
		install(new PhpmyadminModule());
	}
}
