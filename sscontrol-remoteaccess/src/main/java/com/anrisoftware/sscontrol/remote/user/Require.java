/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.user;

/**
 * Require user properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum Require {

    /**
     * Require user password.
     */
    password,

    /**
     * Require user key passphrase.
     */
    passphrase,

    /**
     * Require user authorized keys.
     */
    access,

    /**
     * Require user ID.
     */
    uid,

    /**
     * Require user home directory.
     */
    home,

    /**
     * Require user log-in shell.
     */
    login,

    /**
     * Require user comment.
     */
    comment,

    /**
     * Require user group.
     */
    group;
}
