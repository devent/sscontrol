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
package com.anrisoftware.sscontrol.httpd.staticservice;

import java.util.List;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Static files service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface StaticService extends WebService {

    /**
     * Returns the location.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static", location: "/private", {
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} location.
     */
    String getLocation();

    /**
     * Returns the index file names.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static", location: "/private", {
     *             index files: "index.\$geo.html, index.htm, index.html"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} list of index file {@link String} names or
     *         {@code null}.
     */
    List<String> getIndexFiles();

    /**
     * Returns the index mode.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static", location: "/private", {
     *             index mode: IndexMode.auto
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the index {@link IndexMode} mode or {@code null}.
     */
    IndexMode getIndexMode();

    /**
     * Returns the references of the defined web services to include in this
     * static files service.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static", location: "/private", {
     *             include refs: "webdav-test1.com, auth-test1.com"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} list of {@link String} references or
     *         {@code null}.
     */
    List<String> getIncludeRefs();
}
