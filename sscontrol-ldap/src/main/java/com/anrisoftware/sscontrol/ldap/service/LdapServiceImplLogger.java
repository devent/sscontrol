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
package com.anrisoftware.sscontrol.ldap.service;

import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.admin_set_debug;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.admin_set_info;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.index_added_debug;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.index_added_info;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.profileSet;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.profileSetDebug;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.script_resource_added_debug;
import static com.anrisoftware.sscontrol.ldap.service.LdapServiceImplLogger._.script_resource_added_info;
import static org.apache.commons.lang3.StringUtils.substring;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.ldap.dbindex.DbIndex;
import com.anrisoftware.sscontrol.ldap.statements.Admin;

/**
 * Logging messages for {@link LdapServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LdapServiceImplLogger extends AbstractLogger {

	enum _ {

		profileSetDebug("Profile {} set for {}."),

		profileSet("Profile '{}' set for DNS service."),

		admin_set_debug("Admin {} set for {}."),

		admin_set_info("Admin '{}' set for service '{}'."),

		script_resource_added_debug("Script resource '{}' added for {}."),

		script_resource_added_info(
				"Script resource '{}' added for service '{}'."),

		index_added_debug("Index {} added for {}."),

		index_added_info("Index {} added for service '{}'.");

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
	 * Create logger for {@link LdapServiceImpl}.
	 */
	LdapServiceImplLogger() {
		super(LdapServiceImpl.class);
	}

	void profileSet(Service service, ProfileService profile) {
		if (isDebugEnabled()) {
			debug(profileSetDebug, profile, service);
		} else {
			info(profileSet, profile.getProfileName());
		}
	}

	void adminSet(LdapServiceImpl service, Admin admin) {
		if (isDebugEnabled()) {
			debug(admin_set_debug, admin, service);
		} else {
			info(admin_set_info, admin.getName(), service.getName());
		}
	}

	void scriptResourceAdded(LdapServiceImpl service, Object resource) {
		if (isDebugEnabled()) {
			String str = substring(resource.toString(), 0, 64);
			debug(script_resource_added_debug, str, service);
		} else {
			String str = substring(resource.toString(), 0, 64);
			info(script_resource_added_info, str, service.getName());
		}
	}

	void indexAdded(LdapServiceImpl service, DbIndex index) {
		if (isDebugEnabled()) {
			debug(index_added_debug, index, service);
		} else {
			debug(index_added_info, index.getNames(), service.getName());
		}
	}

}
