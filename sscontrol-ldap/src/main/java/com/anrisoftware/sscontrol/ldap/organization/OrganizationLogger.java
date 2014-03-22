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
package com.anrisoftware.sscontrol.ldap.organization;

import static com.anrisoftware.sscontrol.ldap.organization.OrganizationLogger._.admin_set_debug;
import static com.anrisoftware.sscontrol.ldap.organization.OrganizationLogger._.admin_set_info;
import static com.anrisoftware.sscontrol.ldap.organization.OrganizationLogger._.description_null;
import static com.anrisoftware.sscontrol.ldap.organization.OrganizationLogger._.domain_blank;
import static com.anrisoftware.sscontrol.ldap.organization.OrganizationLogger._.domain_null;
import static com.anrisoftware.sscontrol.ldap.organization.OrganizationLogger._.name_blank;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.ldap.statements.Admin;

/**
 * Logging messages for {@link Organization}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class OrganizationLogger extends AbstractLogger {

	enum _ {

		name_blank("Name cannot be blank."),

		domain_null("Domain cannot be null."),

		domain_blank("Domain cannot be blank."),

		admin_set_debug("Admin {} set for {}."),

		admin_set_info("Admin '{}' set for service '{}'."),

		description_null("Description cannot be null.");

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
	 * Creates a logger for {@link Organization}.
	 */
	public OrganizationLogger() {
		super(Organization.class);
	}

	void checkName(String name) {
		notBlank(name, name_blank.toString());
	}

	void checkDomain(Object domain) {
		notNull(domain, domain_null.toString());
		notBlank(domain.toString(), domain_blank.toString());
	}

	void adminSet(Organization org, Admin admin) {
		if (isDebugEnabled()) {
			debug(admin_set_debug, admin, org);
		} else {
			info(admin_set_info, admin.getName(), org.getName());
		}
	}

	void checkDescription(Object description) {
		notNull(description, description_null.toString());
	}

}
