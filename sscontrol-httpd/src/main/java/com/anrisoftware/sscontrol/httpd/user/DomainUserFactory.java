/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.user;

import java.util.Map;

import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Factory to create domain user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DomainUserFactory {

    /**
     * Creates the domain user.
     * 
     * @param domain
     *            the {@link Domain} of the user.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link DomainUser}.
     */
    DomainUser create(Domain domain, Map<String, Object> args);
}
