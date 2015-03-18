/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.source.gitolite.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService
import com.anrisoftware.sscontrol.source.gitolite.test.PreScriptTestEnvironment
import com.anrisoftware.sscontrol.source.service.SourceService

/**
 * @see GitoliteService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class GitoliteTest extends PreScriptTestEnvironment {

    @Test
    void "gitolite script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService sourceScript.resource, profile, preScript
        SourceService service = registry.getService("source")[0]

        assert service.services.size() == 1
        GitoliteService gitolite = service.services[0]
        assert gitolite.name == "gitolite"
        assert gitolite.overrideMode == OverrideMode.override
        assert gitolite.prefix == "/usr/local/gitosis"
        assert gitolite.dataPath == "/var/git"
        assert gitolite.user.size() == 4
        assert gitolite.user["user"] == "git"
        assert gitolite.user["group"] == "git"
        assert gitolite.user["uid"] == 99
        assert gitolite.user["gid"] == 99
        assert gitolite.adminKey.toString() =~ /.*yourname\.pub/
    }
}
