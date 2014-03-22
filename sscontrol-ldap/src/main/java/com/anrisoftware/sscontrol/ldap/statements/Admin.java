/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.statements;

import java.net.URISyntaxException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * The administrator user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Admin {

	private final AdminLogger log;

	private final String name;

	private final DomainComponent domain;

	private final Scripts scripts;

	private String password;

	private String description;

	/**
	 * @see AdminFactory#create(Map, DomainComponent, String)
	 */
	@Inject
	Admin(AdminLogger log, Scripts scripts, @Assisted Map<String, Object> args,
			@Assisted DomainComponent domain, @Assisted String name) {
		this.log = log;
		this.name = name;
		this.domain = domain;
		this.scripts = scripts;
		setPassword(args.get("password"));
		setDescription(args.get("description"));
	}

	public String getName() {
		return name;
	}

	public void setPassword(Object password) {
		log.checkPassword(password);
		this.password = password.toString();
	}

	public String getPassword() {
		return password;
	}

	public void setDescription(Object description) {
		this.description = description.toString();
	}

	public String getDescription() {
		return description;
	}

	public DomainComponent getDomain() {
		return domain;
	}

	public void script(Object resource) throws URISyntaxException {
		this.scripts.addResource(resource);
	}

	public Scripts getScripts() {
		return scripts;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append(domain)
				.toString();
	}
}
