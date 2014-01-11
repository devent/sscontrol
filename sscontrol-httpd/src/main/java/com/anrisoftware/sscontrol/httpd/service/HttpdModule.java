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

import com.anrisoftware.globalpom.format.byteformat.ByteFormatModule;
import com.anrisoftware.sscontrol.core.bindings.BindingsModule;
import com.anrisoftware.sscontrol.core.database.DatabaseModule;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingModule;
import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthModule;
import com.anrisoftware.sscontrol.httpd.statements.authfile.AuthFileModule;
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapModule;
import com.anrisoftware.sscontrol.httpd.statements.domain.DomainModule;
import com.anrisoftware.sscontrol.httpd.statements.memory.MemoryModule;
import com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminModule;
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminModule;
import com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyServiceModule;
import com.anrisoftware.sscontrol.httpd.statements.redirect.RedirectModule;
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeModule;
import com.anrisoftware.sscontrol.httpd.statements.user.DomainUserModule;
import com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressModule;
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
        install(new ByteFormatModule());
		install(new DomainModule());
		install(new RedirectModule());
		install(new AuthModule());
		install(new AuthFileModule());
		install(new AuthLdapModule());
		install(new PhpmyadminModule());
		install(new PhpldapadminModule());
        install(new RoundcubeModule());
        install(new WordpressModule());
        install(new ProxyServiceModule());
        install(new DebugLoggingModule());
        install(new ListModule());
        install(new DatabaseModule());
        install(new BindingsModule());
        install(new DomainUserModule());
        install(new MemoryModule());
	}
}
