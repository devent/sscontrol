/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>Citadel</i> service.
 *
 * @see <a href="http://citadel.org">http://citadel.org</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CitadelService extends WebService {

    /**
     * Returns the binding addresses.
     * <p>
     *
     * <pre>
     * {["0.0.0.0": [504], "192.168.0.2"]: [504]}
     * </pre>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             bind "0.0.0.0", port: 504
     *             bind all, port: 504
     *             bind "192.168.0.2", ports: [504]
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} of the {@link String} addresses or {@code null}.
     */
    Map<String, List<Integer>> getBindingAddresses();

    /**
     * Returns the administrator user.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             admin "admin", password: "adminpass"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the administrator user {@link String} name or {@code null}.
     */
    String getAdminUser();

    /**
     * Returns the administrator user password.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             admin "admin", password: "adminpass"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the administrator user {@link String} password or {@code null}.
     */
    String getAdminPassword();

    /**
     * Returns the authentication method.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             auth method: AuthMethod.selfContained
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the authentication {@link AuthMethod} method or {@code null}.
     */
    AuthMethod getAuthMethod();

    /**
     * Returns the certificate authority resource.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             certificate ca: "cert.csr", file: "cert.crt", key: "cert.key"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the certificate authority {@link URI} resource or {@code null}.
     */
    URI getCertCa();

    /**
     * Returns the certificate file resource.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             certificate ca: "cert.csr", file: "cert.crt", key: "cert.key"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the certificate file {@link URI} resource or {@code null}.
     */
    URI getCertFile();

    /**
     * Returns the certificate key resource.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "citadel", alias: "/", {
     *             certificate ca: "cert.csr", file: "cert.crt", key: "cert.key"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the certificate key {@link URI} resource or {@code null}.
     */
    URI getCertKey();

}
