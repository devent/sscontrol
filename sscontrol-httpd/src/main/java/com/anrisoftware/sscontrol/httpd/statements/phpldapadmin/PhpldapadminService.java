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
package com.anrisoftware.sscontrol.httpd.statements.phpldapadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.google.inject.assistedinject.Assisted;

/**
 * Phpldapadmin service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PhpldapadminService implements WebService {

	public static final String NAME = "phpldapadmin";

	@Inject
	private PhpldapadminServiceLogger log;

	@Inject
	private LdapServerFactory serverFactory;

	private final List<LdapServer> servers;

	@Inject
	PhpldapadminService(@Assisted Map<String, Object> map) {
		this.servers = new ArrayList<LdapServer>();
	}

	@Override
	public String getName() {
		return NAME;
	}

	public void server(String name) {
		server(new HashMap<String, Object>(), name);
	}

	public void server(Map<String, Object> args, String name) {
		LdapServer server = serverFactory.create(args, name);
		servers.add(server);
		log.serverAdded(this, server);
	}

	public List<LdapServer> getServers() {
		return servers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(NAME).toString();
	}

}
