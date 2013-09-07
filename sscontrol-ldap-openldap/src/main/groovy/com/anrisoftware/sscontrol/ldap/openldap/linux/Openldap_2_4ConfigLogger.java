/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd-apache.
 * 
 * sscontrol-httpd-apache is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.linux;

import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.base_deployed_debug;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.base_deployed_info;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.base_deployed_trace;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.database_deployed_debug;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.database_deployed_info;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.database_deployed_trace;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.ldap_deployed_debug;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.ldap_deployed_info;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.ldap_deployed_trace;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.system_deployed_debug;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.system_deployed_info;
import static com.anrisoftware.sscontrol.ldap.openldap.linux.Openldap_2_4ConfigLogger._.system_deployed_trace;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link Openldap_2_4Config}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Openldap_2_4ConfigLogger extends AbstractLogger {

	enum _ {

		database_deployed_trace(
				"Deploy database configuration to '{}' in {}, {}."),

		database_deployed_debug("Deploy database configuration to '{}' in {}."),

		database_deployed_info(
				"Deploy database configuration to '{}' in script '{}'."),

		base_deployed_trace("Deploy base configuration to '{}' in {}, {}."),

		base_deployed_debug("Deploy base configuration to '{}' in {}."),

		base_deployed_info("Deploy base configuration to '{}' in script '{}'."),

		system_deployed_trace("Deploy system's ACL's to '{}' in {}, {}."),

		system_deployed_debug("Deploy system's ACL's to '{}' in {}."),

		system_deployed_info("Deploy system's ACL's to '{}' in script '{}'."),

		ldap_deployed_trace("Deploy ldap's ACL's to '{}' in {}, {}."),

		ldap_deployed_debug("Deploy ldap's ACL's to '{}' in {}."),

		ldap_deployed_info("Deploy ldap's ACL's to '{}' in script '{}'.");

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
	 * Creates a logger for {@link Openldap_2_4Config}.
	 */
	public Openldap_2_4ConfigLogger() {
		super(Openldap_2_4Config.class);
	}

	void databaseConfigDeployed(LinuxScript script, File file, Object worker) {
		if (isTraceEnabled()) {
			trace(database_deployed_trace, file, script, worker);
		} else if (isDebugEnabled()) {
			debug(database_deployed_debug, file, script, worker);
		} else {
			info(database_deployed_info, file, script.getName());
		}
	}

	void baseConfigDeployed(LinuxScript script, File file, Object worker) {
		if (isTraceEnabled()) {
			trace(base_deployed_trace, file, script, worker);
		} else if (isDebugEnabled()) {
			debug(base_deployed_debug, file, script, worker);
		} else {
			info(base_deployed_info, file, script.getName());
		}
	}

	void systemConfigDeployed(LinuxScript script, File file, Object worker) {
		if (isTraceEnabled()) {
			trace(system_deployed_trace, file, script, worker);
		} else if (isDebugEnabled()) {
			debug(system_deployed_debug, file, script, worker);
		} else {
			info(system_deployed_info, file, script.getName());
		}
	}

	void ldapConfigDeployed(LinuxScript script, File file, Object worker) {
		if (isTraceEnabled()) {
			trace(ldap_deployed_trace, file, script, worker);
		} else if (isDebugEnabled()) {
			debug(ldap_deployed_debug, file, script, worker);
		} else {
			info(ldap_deployed_info, file, script.getName());
		}
	}
}
