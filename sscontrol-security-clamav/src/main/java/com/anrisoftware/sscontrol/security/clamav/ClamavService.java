/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.security.service.SecService;

/**
 * <i>ClamAV</i> service.
 *
 * @see <a href="http://www.clamav.net/">http://www.clamav.net/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ClamavService extends SecService {

    /**
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["log": 1]}
     * </pre>
     *
     * <pre>
     * service "clamav", {
     *     debug "log", level: 1
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
     * {["0.0.0.0": [80]}
     * </pre>
     *
     * <pre>
     * database {
     *     bind all, port: 80
     * }
     * </pre>
     *
     * @return the {@link List} of the {@link String} addresses or {@code null}.
     */
    Map<String, List<Integer>> getBindingAddresses();
}
