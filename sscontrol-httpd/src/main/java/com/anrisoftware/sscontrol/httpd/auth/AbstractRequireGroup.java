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

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Required authentication group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AbstractRequireGroup implements AuthRequire {

	private AbstractRequireGroupLogger log;

	private String name;

	@Inject
	void setAbstractRequireGroupLogger(AbstractRequireGroupLogger logger) {
		this.log = logger;
	}

	public void setName(String name) {
		log.checkName(name);
		this.name = name.toString();
	}

	/**
	 * Returns the name of the group.
	 * 
	 * @return the group name.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
