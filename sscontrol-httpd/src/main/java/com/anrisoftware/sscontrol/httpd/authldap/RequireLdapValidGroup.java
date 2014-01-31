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
package com.anrisoftware.sscontrol.httpd.authldap;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.auth.AbstractRequireGroup;
import com.google.inject.assistedinject.Assisted;

/**
 * Required LDAP/group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequireLdapValidGroup extends AbstractRequireGroup {

	private static final String GROUP = "group";

	private RequireLdapValidGroupLogger log;

	@Inject
	private AuthAttributeFactory attributeFactory;

	private AuthAttribute attribute;

	private Map<String, Object> args;

	/**
	 * @see RequireLdapValidGroupFactory#create(Map)
	 */
	@Inject
	RequireLdapValidGroup(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setAuthRequireGroupLogger(RequireLdapValidGroupLogger logger) {
		this.log = logger;
		setName(args.get(GROUP));
		args = null;
	}

	private void setName(Object name) {
		log.checkName(name);
		setName(name.toString());
	}

	public void attribute(String name) {
		attribute(new HashMap<String, Object>(), name);
	}

	public void attribute(Map<String, Object> args, String name) {
		this.attribute = attributeFactory.create(args, name);
		log.attributeSet(this, attribute);
	}

	public AuthAttribute getAttribute() {
		return attribute;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("attribute", attribute).toString();
	}
}
