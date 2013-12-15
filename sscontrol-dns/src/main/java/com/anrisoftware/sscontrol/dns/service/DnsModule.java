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
package com.anrisoftware.sscontrol.dns.service;

import com.anrisoftware.sscontrol.core.bindings.BindingsModule;
import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.dns.aliases.AliasesModule;
import com.anrisoftware.sscontrol.dns.arecord.ARecordModule;
import com.anrisoftware.sscontrol.dns.cnamerecord.CnameRecordModule;
import com.anrisoftware.sscontrol.dns.mxrecord.MxRecordModule;
import com.anrisoftware.sscontrol.dns.nsrecord.NsRecordModule;
import com.anrisoftware.sscontrol.dns.time.TimeModule;
import com.anrisoftware.sscontrol.dns.zone.ZoneModule;
import com.google.inject.AbstractModule;

/**
 * Binds the DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ZoneModule());
		install(new ListModule());
		install(new AliasesModule());
		install(new BindingsModule());
		install(new ARecordModule());
		install(new CnameRecordModule());
		install(new NsRecordModule());
		install(new MxRecordModule());
		install(new TimeModule());
	}
}
