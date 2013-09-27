/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.database.mysql.resource.ResourcesUtils

/**
 * MySQL/Ubuntu resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/sbin/restart", UbuntuResources.class.getResource("echo_command.txt")),

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
	}

	ResourcesUtils resources

	UbuntuResources(String path, URL resource) {
		this.resources = new ResourcesUtils(path: path, resource: resource)
	}

	String getPath() {
		resources.path
	}

	URL getResource() {
		resources.resource
	}

	File asFile(File parent) {
		resources.asFile parent
	}

	void createFile(File parent) {
		resources.createFile parent
	}

	void createCommand(File parent) {
		resources.createCommand parent
	}

	String replaced(File parent, def search, def replace) {
		resources.replaced parent, search, replace
	}

	String toString() {
		resources.toString()
	}
}
