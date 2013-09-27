/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.dns.maradns.resources.ResourcesUtils

/**
 * MaraDNS/Ubuntu resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

	aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
	restartCommand("/etc/init.d/maradns", UbuntuResources.class.getResource("echo_command.txt")),
	addRepositoryCommand("/usr/bin/add-apt-repository", UbuntuResources.class.getResource("echo_command.txt")),
	confDir("/etc/maradns", null),
	packagingConfigurationDirectory("/etc/apt", null),

	static copyUbuntuFiles(File parent) {
		aptitudeCommand.createCommand parent
		restartCommand.createCommand parent
		addRepositoryCommand.createCommand parent
		confDir.asFile parent mkdirs()
		packagingConfigurationDirectory.asFile parent mkdirs()
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