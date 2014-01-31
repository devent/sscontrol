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

import static com.anrisoftware.sscontrol.httpd.authldap.RequireLdapValidGroupLogger._.attribute_set;
import static com.anrisoftware.sscontrol.httpd.authldap.RequireLdapValidGroupLogger._.attribute_set_info;
import static com.anrisoftware.sscontrol.httpd.authldap.RequireLdapValidGroupLogger._.name_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link RequireLdapValidGroup}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class RequireLdapValidGroupLogger extends AbstractLogger {

	enum _ {

		name_null("Group name cannot be null."),

		attribute_set("Group attribute {} set for {}."),

		attribute_set_info("Group attribute '{}' set for group '{}'.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link RequireLdapValidGroup}.
	 */
	public RequireLdapValidGroupLogger() {
		super(RequireLdapValidGroup.class);
	}

	void checkName(Object name) {
		notNull(name, name_null.toString());
	}

	void attributeSet(RequireLdapValidGroup group, AuthAttribute attribute) {
		if (isDebugEnabled()) {
			debug(attribute_set, attribute, group);
		} else {
			info(attribute_set_info, attribute.getName(), group.getName());
		}
	}

}
