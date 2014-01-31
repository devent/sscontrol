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
package com.anrisoftware.sscontrol.httpd.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Base resource authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AbstractAuth {

	private static final String GROUP = "group";

	private final String name;

	private final List<AuthRequire> requires;

	@Inject
	private AbstractAuthLogger log;

	@Inject
	private RequireValidUserFactory validUserFactory;

	@Inject
	private RequireValidGroupFactory validGroupFactory;

	private String location;

	private AuthType type;

	private AuthProvider provider;

	private SatisfyType satisfy;

	private final List<String> domains;

	/**
	 * Sets the name and default type, provider and satisfy type.
	 * 
	 * @param name
	 *            the name {@link String}.
	 */
	protected AbstractAuth(String name) {
		this.name = name;
		this.requires = new ArrayList<AuthRequire>();
		this.domains = new ArrayList<String>();
		this.type = AuthType.digest;
		this.provider = AuthProvider.file;
		this.satisfy = SatisfyType.all;
	}

	public String getName() {
		return name;
	}

	public void setLocation(String location) {
		log.checkLocation(location);
		this.location = location;
		if (domains.isEmpty()) {
			addDomain(location);
		}
	}

	public String getLocation() {
		return location;
	}

	public void domain(String domain) {
		addDomain(domain);
	}

	public void addDomain(String domain) {
		log.checkDomain(domain);
		domains.add(domain);
		log.domainAdded(this, domain);
	}

	public List<String> getDomains() {
		return domains;
	}

	public void setType(AuthType type) {
		log.checkType(type);
		this.type = type;
	}

	public AuthType getType() {
		return type;
	}

	public void setProvider(AuthProvider provider) {
		log.checkProvider(provider);
		this.provider = provider;
	}

	public AuthProvider getProvider() {
		return provider;
	}

	public void setSatisfy(SatisfyType type) {
		log.checkSatisfy(type);
		this.satisfy = type;
	}

	public SatisfyType getSatisfy() {
		return satisfy;
	}

	public void valid_user() {
		RequireValidUser require = validUserFactory.create();
		requires.add(require);
		log.requireValidUserAdded(this, require);
	}

	public void require(Object s) {
	}

	public void require(Map<String, Object> map) {
		if (map.containsKey(GROUP)) {
			RequireValidGroup require = validGroupFactory.create(map);
			requires.add(require);
			log.requireGroupAdded(this, require);
		}
	}

	public void addRequire(AuthRequire require) {
		this.requires.add(require);
	}

	public List<AuthRequire> getRequires() {
		return requires;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).toString();
	}
}
