/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.ldap.dbindex.DbIndexFormatFactory;

/**
 * OpenLDAP service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class OpenldapScript extends LinuxScript {

	@Inject
	private OpenldapScriptLogger log

	@Inject
	private DbIndexFormatFactory indexFormatFactory

	@Override
	def run() {
		super.run()
		distributionSpecificConfiguration()
	}

	/**
	 * Run the distribution specific configuration.
	 */
	abstract distributionSpecificConfiguration()

	/**
	 * Returns the template resource containing LDAP/commands.
	 */
	abstract TemplateResource getLdapCommandsTemplate()

	/**
	 * Returns command to add LDAP entries {@code ldapadd}.
	 *
	 * <ul>
	 * <li>profile property {@code "ldapadd_command"}</li>
	 * </ul>
	 */
	String getLdapaddCommand() {
		profileProperty "ldapadd_command", defaultProperties
	}

	/**
	 * Returns OpenLDAP password utility {@code slappasswd}.
	 *
	 * <ul>
	 * <li>profile property {@code "slappasswd_command"}</li>
	 * </ul>
	 */
	String getSlappasswdCommand() {
		profileProperty "slappasswd_command", defaultProperties
	}

	/**
	 * Returns the directory under which the LDAP modules are found.
	 *
	 * <ul>
	 * <li>profile property {@code "module_directory"}</li>
	 * </ul>
	 */
	File getModuleDirectory() {
		profileProperty("module_directory", defaultProperties) as File
	}

	/**
	 * Returns the directory under which the LDAP database is saved.
	 *
	 * <ul>
	 * <li>profile property {@code "database_directory"}</li>
	 * </ul>
	 */
	File getDatabaseDirectory() {
		profileProperty("database_directory", defaultProperties) as File
	}

	/**
	 * Returns the directory under which the default schemas are found. If the
	 * path is not absolute then it is assume to be under the configuration
	 * directory.
	 *
	 * <ul>
	 * <li>profile property {@code "schemas_directory"}</li>
	 * </ul>
	 */
	File getSchemasDir() {
		propertyFile "schemas_directory", defaultProperties
	}

	/**
	 * Returns the database configuration file. If the
	 * path is not absolute then it is assume to be under the configuration
	 * directory. For example {@code "db.ldif"}.
	 *
	 * <ul>
	 * <li>profile property {@code "database_config_file"}</li>
	 * </ul>
	 */
	File getDatabaseConfigFile() {
		propertyFile "database_config_file", defaultProperties
	}

	/**
	 * Returns the base configuration file. If the
	 * path is not absolute then it is assume to be under the configuration
	 * directory. For example {@code "base.ldif"}.
	 *
	 * <ul>
	 * <li>profile property {@code "base_config_file"}</li>
	 * </ul>
	 */
	File getBaseConfigFile() {
		propertyFile "base_config_file", defaultProperties
	}

	/**
	 * Returns the system's ACL's configuration file. If the
	 * path is not absolute then it is assume to be under the configuration
	 * directory. For example {@code "config.ldif"}.
	 *
	 * <ul>
	 * <li>profile property {@code "system_acls_config_file"}</li>
	 * </ul>
	 */
	File getSystemACLConfigFile() {
		propertyFile "system_acls_config_file", defaultProperties
	}

	/**
	 * Returns the LDAP's ACL's configuration file. If the
	 * path is not absolute then it is assume to be under the configuration
	 * directory. For example {@code "acl.ldif"}.
	 *
	 * <ul>
	 * <li>profile property {@code "ldap_acls_config_file"}</li>
	 * </ul>
	 */
	File getLdapACLConfigFile() {
		propertyFile "ldap_acls_config_file", defaultProperties
	}

	/**
	 * Returns a list of the default schemas.
	 *
	 * <ul>
	 * <li>profile property {@code "default_schemas"}</li>
	 * </ul>
	 */
	List getDefaultSchemas() {
		profileListProperty "default_schemas", defaultProperties
	}

	/**
	 * Returns the size of the cache in giga bytes.
	 *
	 * <ul>
	 * <li>profile property {@code "cache_size_gbytes"}</li>
	 * </ul>
	 */
	int getCacheSizeGbytes() {
		profileNumberProperty "cache_size_gbytes", defaultProperties
	}

	/**
	 * Returns the size of the cache in bytes.
	 *
	 * <ul>
	 * <li>profile property {@code "cache_size_bytes"}</li>
	 * </ul>
	 */
	int getCacheSizeBytes() {
		profileNumberProperty "cache_size_bytes", defaultProperties
	}

	/**
	 * Returns the number of the cache segments.
	 *
	 * <ul>
	 * <li>profile property {@code "cache_segments"}</li>
	 * </ul>
	 */
	int getCacheSegments() {
		profileNumberProperty "cache_segments", defaultProperties
	}

	/**
	 * Returns the maximum number of lock objects.
	 *
	 * <ul>
	 * <li>profile property {@code "max_lock_objects"}</li>
	 * </ul>
	 */
	int getMaxLockObjects() {
		profileNumberProperty "max_lock_objects", defaultProperties
	}

	/**
	 * Returns the maximum number of locks.
	 *
	 * <ul>
	 * <li>profile property {@code "max_locks"}</li>
	 * </ul>
	 */
	int getMaxLocks() {
		profileNumberProperty "max_locks", defaultProperties
	}

	/**
	 * Returns the maximum number of lockers.
	 *
	 * <ul>
	 * <li>profile property {@code "max_lockers"}</li>
	 * </ul>
	 */
	int getMaxLockers() {
		profileNumberProperty "max_lockers", defaultProperties
	}

	/**
	 * Returns the checkpoint size in kilo bytes.
	 *
	 * <ul>
	 * <li>profile property {@code "checkpoint_kbytes"}</li>
	 * </ul>
	 */
	int getCheckpointKbytes() {
		profileNumberProperty "checkpoint_kbytes", defaultProperties
	}

	/**
	 * Returns the checkpoint minutes.
	 *
	 * <ul>
	 * <li>profile property {@code "checkpoint_minutes"}</li>
	 * </ul>
	 */
	int getCheckpointMinutes() {
		profileNumberProperty "checkpoint_minutes", defaultProperties
	}

	/**
	 * Returns a list of default indexes to be created. The index entries
	 * are separated by a semi column {@code ;}. For example:
	 * {@code "uid:present;equality,cn;sn;mail:present;equality;approx;substring,objectClass:equality"}.
	 *
	 * <ul>
	 * <li>profile property {@code "checkpoint_minutes"}</li>
	 * </ul>
	 *
	 * @see DbIndex
	 */
	List getDefaultIndexes() {
		profileTypedListProperty "checkpoint_minutes", defaultProperties, indexFormatFactory.create(";")
	}
}
