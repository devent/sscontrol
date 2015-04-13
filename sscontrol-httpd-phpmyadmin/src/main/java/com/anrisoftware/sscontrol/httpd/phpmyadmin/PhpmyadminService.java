/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.phpmyadmin;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * <i>phpMyAdmin</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PhpmyadminService extends WebService {

    /**
     * Returns the administrator user name.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             admin "root", password: "rootpass"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the administrator user {@link String} name.
     */
    String getAdminUser();

    /**
     * Returns the administrator user password.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             admin "root", password: "rootpass"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the administrator user {@link String} password.
     */
    String getAdminPassword();

    /**
     * Returns the control user name.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             control "phpmyadmin", password: "somepass", database: "phpmyadmin"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the control user {@link String} name.
     */
    String getControlUser();

    /**
     * Returns the control user password.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             control "phpmyadmin", password: "somepass", database: "phpmyadmin"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the control user {@link String} password.
     */
    String getControlPassword();

    /**
     * Returns the control user database name.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             control "phpmyadmin", password: "somepass", database: "phpmyadmin"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the control user database {@link String} name.
     */
    String getControlDatabase();

    /**
     * Returns the database server.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             server "127.0.0.1", port: 3306
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the database {@link String} server.
     */
    String getServer();

    /**
     * Returns the database server port.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "phpadmin.test1.com", address: "192.168.0.50", {
     *         setup "phpmyadmin", alias: "phpmyadmin", {
     *             server "127.0.0.1", port: 3306
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the database server {@link Integer} port or {@code null}.
     */
    Integer getServerPort();

}
