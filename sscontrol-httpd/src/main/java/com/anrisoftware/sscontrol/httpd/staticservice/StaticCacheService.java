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
import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Static files cache service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface StaticCacheService extends StaticService {

    /**
     * Returns the expires duration.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static-cache", alias: "/private", {
     *             expires "P1D"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the expires {@link Duration} duration or {@code null}.
     *
     * @throws ServiceException
     *             if there was an error parsing the duration.
     */
    Duration getExpiresDuration() throws ServiceException;

    /**
     * Returns to enable or disable the access log.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static-cache", alias: "/private", {
     *             access log: yes
     *         }
     *     }
     * }
     * </pre>
     *
     * @return {@code true} to enable the access log or {@code null}.
     */
    Boolean getEnabledAccessLog();

    /**
     * Returns the headers to add.
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static-cache", alias: "/private", {
     *             headers "Cache-Control", value: public"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link Map} of {@link String} headers to add or {@code null.}
     */
    Map<String, Object> getHeadersValues();

    /**
     * Returns the references of the defined web services to include in this
     * static files service.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "static-cache", alias: "/private", {
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
