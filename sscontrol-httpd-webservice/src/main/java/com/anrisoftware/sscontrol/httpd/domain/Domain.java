/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.domain;

import java.util.Map;

import com.anrisoftware.sscontrol.httpd.user.DomainUser;

/**
 * Domain entry.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Domain {

    /**
     * Returns the domain name.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     * }
     * </pre>
     *
     * @return the domain {@link String} name.
     */
    String getName();

    /**
     * Returns the domain address.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     * }
     * </pre>
     *
     * @return the domain {@link String} address.
     */
    String getAddress();

    /**
     * Returns the domain unique identifier.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", id: "test1id", {
     * }
     * </pre>
     *
     * @return the domain {@link String} identifier or {@code null}.
     */
    String getId();

    /**
     * Returns the domain port.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", port: 8080, {
     * }
     * </pre>
     *
     * @return the domain {@link Integer} port.
     */
    int getPort();

    /**
     * Returns the identifier of the domain which parameters should be used.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", use: "domainid", {
     * }
     * </pre>
     *
     * @return the domain {@link String} identifier or {@code null}.
     */
    String getUseDomain();

    /**
     * Returns the domain root directory.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", root: "/root", {
     * }
     * </pre>
     *
     * @return the domain root directory {@link String} path.
     */
    String getDocumentRoot();

    /**
     * Returns the domain local user.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     * 
     * }
     * </pre>
     *
     * @param user
     *            the {@link DomainUser}.
     */
    DomainUser getDomainUser();

    /**
     * Returns the debug level settings for the modules:
     * <ul>
     * <li>roundcube</li>
     * <li>php</li>
     * </ul>
     *
     * Example:
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     *      debug "php", level: 1
     * }
     * </pre>
     *
     * @return the debug logging {@link Map} settings or {@code null}.
     */
    Map<String, Object> getDebug();

}
