/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit;

/**
 * Authentication method.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum AuthMethod {

    /**
     * Users will be logged in and registered using forms in the web interface.
     */
    form,

    /**
     * Users will be logged in with HTTP authentication.
     */
    http,

    /**
     * Some generic authentication method that uses the <code>REMOTE_USER</code>
     * that is set to the name of the authenticated user.
     */
    generic,

    /**
     * Attempts to log in through <a href="https://rpxnow.com">rpxnow.com</a>.
     */
    rpx,
}
