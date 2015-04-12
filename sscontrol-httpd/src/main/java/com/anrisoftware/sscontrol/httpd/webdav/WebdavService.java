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
package com.anrisoftware.sscontrol.httpd.webdav;

import java.util.List;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>WebDAV</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface WebdavService extends WebService {

    /**
     * Returns the allowed methods.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "webdav", location: "/private", {
     *             methods "PUT, DELETE, MKCOL, COPY, MOVE"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} of {@link String} methods or {@code null}.
     */
    List<String> getMethods();

    /**
     * Returns the user access permissions.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-service", location: "/private", {
     *             access user: "rw", group: "rw", all: "r"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} permissions or {@code null}.
     */
    String getUserAccess();

    /**
     * Returns the group access permissions.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-service", location: "/private", {
     *             access user: "rw", group: "rw", all: "r"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} permissions or {@code null}.
     */
    String getGroupAccess();

    /**
     * Returns the all others access permissions.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-service", location: "/private", {
     *             access user: "rw", group: "rw", all: "r"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} permissions or {@code null}.
     */
    String getAllAccess();

}
