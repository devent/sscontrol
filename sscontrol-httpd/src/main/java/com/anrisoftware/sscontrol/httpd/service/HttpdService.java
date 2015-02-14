/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * <i>Httpd</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface HttpdService extends Service {

    /**
     * Returns the httpd service name.
     */
    @Override
    String getName();

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "storage":
     *
     * <pre>
     * {["error": "foo"]}
     * </pre>
     *
     * <pre>
     * database {
     *     debug "error", level: 4
     *     debug "error", storage: "foo"
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> debugLogging(String key);

    /**
     * Returns the binding addresses.
     * <p>
     *
     * <pre>
     * {["0.0.0.0": [80], "192.168.0.2"]: [8082, 8084]}
     * </pre>
     *
     * <pre>
     * database {
     *     bind all, port: 80
     *     bind "192.168.0.2", ports: [8082, 8084]
     * }
     * </pre>
     *
     * @return the {@link List} of the {@link String} addresses or {@code null}.
     */
    Map<String, List<Integer>> getBindingAddresses();

    /**
     * Returns all domains of the service.
     *
     * @return the {@link List} of the {@link Domain} domains.
     */
    List<Domain> getDomains();

    /**
     * Returns a set of the virtual domains of the service.
     *
     * @return the {@link Set} of the virtual {@link Domain} domains.
     */
    Set<Domain> getVirtualDomains();

}
