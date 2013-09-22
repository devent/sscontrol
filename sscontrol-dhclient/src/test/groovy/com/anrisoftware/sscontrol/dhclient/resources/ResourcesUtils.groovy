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
package com.anrisoftware.sscontrol.dhclient.resources

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

/**
 * Loads resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ResourcesUtils {

	String path

	URL resource

	File asFile(File parent) {
		new File(parent, path)
	}

	void createFile(File parent) {
		assert resource : "Resource cannot be null for ${resource}"
		copyURLToFile resource, new File(parent, path)
	}

	void createCommand(File parent) {
		assert resource : "Resource cannot be null for ${resource}"
		copyResourceToCommand resource, new File(parent, path)
	}

	String replaced(File parent, def search, def replace) {
		String text = readFileToString(this.asFile(parent))
		text.replaceAll(search.toString(), replace)
	}

	String toString() {
		assert resource : "Resource cannot be null for ${resource}"
		resourceToString resource
	}
}
