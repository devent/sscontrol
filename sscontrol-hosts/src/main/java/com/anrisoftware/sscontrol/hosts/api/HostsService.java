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
package com.anrisoftware.sscontrol.hosts.api;

import java.util.Collection;
import java.util.List;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.hosts.host.Host;

/**
 * Hosts service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface HostsService extends Service {

    /**
     * Returns the hosts service name.
     */
    @Override
    String getName();

    /**
     * Returns the host entries.
     * 
     * @return a {@link List} of {@link Host} entries.
     */
    List<Host> getHosts();

    /**
     * Adds all hosts from the specified collection in front of the current
     * hosts.
     * 
     * @param hosts
     *            the {@link Collection}.
     */
    void addHostsHead(Collection<? extends Host> hosts);

}
