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
package com.anrisoftware.sscontrol.httpd.auth;

import java.util.List;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Authentication service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthService extends WebService {

    /**
     * Returns the authentication name.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-service", auth: "Private Directory", {
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the authentication {@link String} name;
     */
    String getAuth();

    /**
     * Returns the authentication location.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-service", location: "/private", {
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the authentication {@link String} location;
     */
    String getLocation();

    /**
     * Returns the required groups.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-service", auth: "Private Directory", location: "/private", {
     *             group "foogroupappend", update: RequireUpdate.append, {
     *             }
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} list of {@link AuthGroup} groups or
     *         {@code null.}
     */
    List<AuthGroup> getGroups();

}
