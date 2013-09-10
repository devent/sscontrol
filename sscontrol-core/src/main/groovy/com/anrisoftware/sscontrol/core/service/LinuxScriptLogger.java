/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-core.
 * 
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-core is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chmod_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.chown_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.conf_file_found_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.conf_file_found_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.deployed_conf_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.deployed_conf_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.enabled_repository_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.enabled_repository_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.group_add_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.group_add_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.group_add_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_files_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_files_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.link_files_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.properties_null;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.property_not_set;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.restarted_service_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.restarted_service_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.restarted_service_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_done_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_done_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.unpack_done_trace;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.user_add_debug;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.user_add_info;
import static com.anrisoftware.sscontrol.core.service.LinuxScriptLogger._.user_add_trace;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link LinuxScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LinuxScriptLogger extends AbstractLogger {

	enum _ {

		enabled_repository_debug("Enabled repository '{}' in {}, {}."),

		enabled_repository_info("Enabled repository '{}' in service {}."),

		install_packages_done_trace("Installed packages {} in {}, {}."),

		install_packages_done_debug("Installed packages {} in {}."),

		install_packages_done_info("Installed service packages: {}."),

		conf_file_found_debug("No configuration file found '{}' in {}."),

		conf_file_found_info("No configuration file found '{}'."),

		deployed_conf_debug("Deploy configuration to '{}' in {}: '{}'."),

		deployed_conf_info("Deploy configuration to '{}'."),

		restarted_service_trace("Restarted service {}, {}."),

		restarted_service_debug("Restarted service {}."),

		restarted_service_info("Restarted service {}."),

		properties_null("Properties cannot be null for key '%s' in %s."),

		property_not_set("Property '%s' is not set for %s."),

		chmod_done_trace("Change permissions done {} in {}, {}."),

		chmod_done_debug("Change permissions done {} in {}."),

		chmod_done_info("Change permissions done for {}."),

		chown_done_trace("Change owner done {} in {}, {}."),

		chown_done_debug("Change owner done {} in {}."),

		chown_done_info("Change owner done for {}."),

		group_add_trace("Add group '{}' in {}, {}."),

		group_add_debug("Add group '{}' in {}."),

		group_add_info("Add group '{}' to service '{}'."),

		user_add_trace("Add user '{}' in {}, {}."),

		user_add_debug("Add user '{}' in {}."),

		user_add_info("Add user '{}' to service '{}'."),

		link_files_done_trace("Link {} to {} done in {}, {}."),

		link_files_done_debug("Link {} to {} done in {}."),

		link_files_done_info(
				"Link files {} to targets {} done in service '{}'."),

		unpack_done_trace("Unpack {} to {} done in {}, {}."),

		unpack_done_debug("Unpack {} to {} done in {}."),

		unpack_done_info("Unpack file {} to {} done in service '{}'.");

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
	 * Create logger for {@link LinuxScript}.
	 */
	LinuxScriptLogger() {
		super(LinuxScript.class);
	}

	void installPackagesDone(LinuxScript script, Object worker, Object packages) {
		if (isTraceEnabled()) {
			trace(install_packages_done_trace, packages, script, worker);
		} else if (isDebugEnabled()) {
			debug(install_packages_done_debug, packages, script);
		} else {
			info(install_packages_done_info, packages);
		}
	}

	void changeModDone(LinuxScript script, Object worker, Object args) {
		if (isTraceEnabled()) {
			trace(chmod_done_trace, args, script, worker);
		} else if (isDebugEnabled()) {
			debug(chmod_done_debug, args, script);
		} else {
			info(chmod_done_info, args);
		}
	}

	void changeOwnerDone(LinuxScript script, Object worker, Object args) {
		if (isTraceEnabled()) {
			trace(chown_done_trace, args, script, worker);
		} else if (isDebugEnabled()) {
			debug(chown_done_debug, args, script);
		} else {
			info(chown_done_info, args);
		}
	}

	void noConfigurationFound(LinuxScript script, Object file) {
		if (isDebugEnabled()) {
			debug(conf_file_found_debug, file, script);
		} else {
			info(conf_file_found_info, file);
		}
	}

	void deployConfigurationDone(LinuxScript script, File file,
			String configuration) {
		if (isDebugEnabled()) {
			debug(deployed_conf_debug, file, script, configuration);
		} else {
			info(deployed_conf_info, file);
		}
	}

	void enableRepositoryDone(LinuxScript script, Object worker,
			String repository) {
		if (isDebugEnabled()) {
			debug(enabled_repository_debug, repository, script, worker);
		} else {
			info(enabled_repository_info, repository, script.getName());
		}
	}

	void restartServiceDone(LinuxScript script, Object worker) {
		if (isTraceEnabled()) {
			trace(restarted_service_trace, script, worker);
		} else if (isDebugEnabled()) {
			debug(restarted_service_debug, script);
		} else {
			info(restarted_service_info, script.getName());
		}
	}

	void addGroupDone(LinuxScript script, Object worker, Object group) {
		if (isTraceEnabled()) {
			trace(group_add_trace, group, script, worker);
		} else if (isDebugEnabled()) {
			debug(group_add_debug, group, script);
		} else {
			info(group_add_info, group, script.getName());
		}
	}

	void addUserDone(LinuxScript script, Object worker, Object user) {
		if (isTraceEnabled()) {
			trace(user_add_trace, user, script, worker);
		} else if (isDebugEnabled()) {
			debug(user_add_debug, user, script);
		} else {
			info(user_add_info, user, script.getName());
		}
	}

	void linkFilesDone(LinuxScript script, Object worker, Object files,
			Object targets) {
		if (isTraceEnabled()) {
			trace(link_files_done_trace, files, targets, script, worker);
		} else if (isDebugEnabled()) {
			debug(link_files_done_debug, files, targets, script);
		} else {
			info(link_files_done_info, files, targets, script.getName());
		}
	}

	void unpackDone(LinuxScript script, Object worker, Object file,
			Object target) {
		if (isTraceEnabled()) {
			trace(unpack_done_trace, file, target, script, worker);
		} else if (isDebugEnabled()) {
			debug(unpack_done_debug, file, target, script);
		} else {
			info(unpack_done_info, file, target, script.getName());
		}
	}

	void checkProperties(LinuxScript script, Object properties, String key) {
		notNull(properties, properties_null.toString(), key, script);
	}

	void checkPropertyKey(LinuxScript script, Object property, Object key) {
		notNull(property, property_not_set.toString(), key, script);
	}
}
