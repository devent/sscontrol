/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.repo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Repository service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RepoService extends Service {

    /**
     * Returns the repository service name.
     */
    @Override
    String getName();

    /**
     * Returns the repository proxy.
     * <p>
     * <h4>Example</h4>
     *
     * <pre>
     * repo {
     *     proxy "http://proxy.ubuntu.net"
     * }
     * </pre>
     *
     * @return the proxy {@link String} name or {@code null}.
     */
    String getProxy();

    /**
     * Returns the signing keys.
     * <p>
     * Example returns the map
     * 
     * <pre>
     * [
     *      ["foo signing key": "http://keyserver.net/key-foo"],
     *      ["bar signing key": "http://keyserver.net/key-bar"],
     * ]
     * </pre>
     *
     * <pre>
     * repo {
     *     sign "foo signing key", key: "http://keyserver.net/key-foo"
     *     sign "bar signing key", key: "http://keyserver.net/key-bar"
     * }
     * </pre>
     *
     * @return the repositories {@link Set} set or {@code null}.
     */
    Map<String, Object> getSignKeys();

    /**
     * Returns the repositories.
     * <p>
     * Example returns the set
     * {@code ["http://foo.archive.ubuntu.com/ubuntu/", "http://bar.archive.ubuntu.com/ubuntu/"]}
     *
     * <pre>
     * repo {
     *     repository "http://foo.archive.ubuntu.com/ubuntu/"
     *     repository "http://bar.archive.ubuntu.com/ubuntu/"
     * }
     * </pre>
     *
     * @return the repositories {@link Set} set or {@code null}.
     */
    Set<String> getRepositories();

    /**
     * Returns the list of to enable repository components.
     * <p>
     * Example returns the list {@code ["foo", "bar"]}
     *
     * <pre>
     * repo {
     *     enable components: "foo, bar"
     * }
     * </pre>
     *
     * @return the repositories {@link List} set or {@code null}.
     */
    List<String> getEnableComponents();

    /**
     * Returns the repositories distribution.
     * <p>
     * Example returns the map
     * {@code ["http://baz.archive.ubuntu.com/ubuntu/": "precise"]}
     *
     * <pre>
     * repo {
     *     repository "http://baz.archive.ubuntu.com/ubuntu/", distribution: "precise", components: "universe, multiverse"
     * }
     * </pre>
     *
     * @return the repositories distribution {@link Map} map or {@code null}.
     */
    Map<String, String> getRepositoriesDistribution();

    /**
     * Returns the repositories components.
     * <p>
     * Example returns the map
     * {@code ["http://baz.archive.ubuntu.com/ubuntu/": ["universe", "multiverse"]]}
     *
     * <pre>
     * repo {
     *     repository "http://baz.archive.ubuntu.com/ubuntu/", distribution: "precise", components: "universe, multiverse"
     * }
     * </pre>
     *
     * @return the repositories components {@link Map} map or {@code null}.
     */
    Map<String, List<String>> getRepositoriesComponents();

}
