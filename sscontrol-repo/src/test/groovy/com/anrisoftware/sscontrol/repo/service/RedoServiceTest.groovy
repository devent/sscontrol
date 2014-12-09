/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.repo.service.RepoServiceFactory.*
import static com.anrisoftware.sscontrol.repo.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * <i>Repo</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RedoServiceTest extends RedoServiceBase {

    @Test
    void "repo"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService repoScript.resource, profile

        RepoService service = registry.getService("repo")[0]
        assert service.proxy == "http://proxy.ubuntu.net"
        assert service.repositories.size() == 3
        assert service.repositories.containsAll([
            "http://foo.archive.ubuntu.com/ubuntu/",
            "http://bar.archive.ubuntu.com/ubuntu/",
            "http://baz.archive.ubuntu.com/ubuntu/"
        ])
        assert service.repositoriesDistribution.size() == 1
        assert service.repositoriesDistribution["http://baz.archive.ubuntu.com/ubuntu/"] == "precise"
        assert service.repositoriesComponents.size() == 1
        assert service.repositoriesComponents["http://baz.archive.ubuntu.com/ubuntu/"].size() == 2
        assert service.repositoriesComponents["http://baz.archive.ubuntu.com/ubuntu/"].containsAll("universe", "multiverse")
    }
}
