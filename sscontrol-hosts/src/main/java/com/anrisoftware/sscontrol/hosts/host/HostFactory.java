/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.host;

import java.util.Map;

import com.anrisoftware.sscontrol.hosts.api.HostsService;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface HostFactory {

    /**
     * Creates the new host.
     * 
     * @param service
     *            the {@link HostsService}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Host}.
     */
    Host create(HostsService service, Map<String, Object> args);

    /**
     * Create the host from the specified host name and address.
     * 
     * @param name
     *            the host {@link String} name.
     * 
     * @param address
     *            the {@link String} address.
     * 
     * @return the {@link Host}.
     */
    Host create(@Assisted("name") String name,
            @Assisted("address") String address);
}
