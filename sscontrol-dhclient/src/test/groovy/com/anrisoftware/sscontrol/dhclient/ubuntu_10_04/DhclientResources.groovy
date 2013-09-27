/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.dhclient.resources.ResourcesUtils

/**
 * Dhclient/Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum DhclientResources {

	profile("UbuntuProfile.groovy", DhclientResources.class.getResource("UbuntuProfile.groovy")),
	dhclientScript("Dhclient.groovy", DhclientResources.class.getResource("Dhclient.groovy")),
	dhclient("/etc/dhcp3/dhclient.conf", DhclientResources.class.getResource("dhclient.txt")),
	dhclientEmptyExpected("/etc/dhcp3/dhclient.conf", DhclientResources.class.getResource("dhclient_empty_expected.txt")),
	dhclientNonEmptyExpected("/etc/dhcp3/dhclient.conf", DhclientResources.class.getResource("dhclient_nonempty_expected.txt")),

	ResourcesUtils resources

	DhclientResources(String path, URL resource) {
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