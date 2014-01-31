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

import java.util.Map;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Require valid group for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequireValidGroup extends AbstractRequireGroup {

	private static final String GROUP = "group";

	private RequireValidGroupLogger log;

	private Map<String, Object> args;

	/**
	 * @see AuthRequireFactory#group(Map)
	 */
	@Inject
	RequireValidGroup(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setAuthRequireGroupLogger(RequireValidGroupLogger logger) {
		this.log = logger;
		setName(args.get(GROUP));
		args = null;
	}

	private void setName(Object name) {
		log.checkName(name);
		setName(name.toString());
	}
}
