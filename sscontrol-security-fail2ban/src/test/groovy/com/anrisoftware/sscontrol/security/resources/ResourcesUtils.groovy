/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.resources

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
        assert resource : "Resource cannot be null for ${path}"
        copyURLToFile resource, new File(parent, path)
    }

    void createCommand(File parent) {
        assert resource : "Resource cannot be null for ${path}"
        copyResourceToCommand resource, new File(parent, path)
    }

    String replaced(File parent, def search, def replace) {
        String text = readFileToString(this.asFile(parent))
        text.replaceAll(search.toString(), replace)
    }

    String toString() {
        assert resource : "Resource cannot be null for ${path}"
        resourceToString resource
    }
}
