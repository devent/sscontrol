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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.hosts.api.HostsService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Host with address and aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Host implements Serializable {

    private static final String ALIASES = "aliases";

    private static final String HOSTNAME = "hostname";

    private final String address;

    private final List<String> aliases;

    private final String hostname;

    @Inject
    private HostLogger log;

    /**
     * @see HostFactory#create(String, String)
     */
    @AssistedInject
    Host(@Assisted("name") String name, @Assisted("address") String address) {
        this.address = address;
        this.hostname = name;
        this.aliases = new ArrayList<String>();
    }

    /**
     * @see HostFactory#create(HostsService, Map)
     */
    @AssistedInject
    Host(HostArgs aargs, @Assisted HostsService service,
            @Assisted Map<String, Object> args) {
        this.address = aargs.address(service, args);
        this.hostname = aargs.hostname(service, args);
        this.aliases = new ArrayList<String>();
        if (aargs.haveAliases(args)) {
            aliases.addAll(aargs.aliases(service, args));
        }
    }

    /**
     * Returns the host IP address.
     * 
     * @return the IP address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the name of the host.
     * 
     * @return the host name.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Returns the aliases of the host.
     * 
     * @return a {@link List} of the host aliases.
     */
    public List<String> getAliases() {
        return aliases;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
        log.aliasAdded(this, alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Host rhs = (Host) obj;
        return new EqualsBuilder().append(address, rhs.getAddress())
                .append(hostname, rhs.getHostname())
                .append(aliases, rhs.getAliases()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(address).append(hostname)
                .append(aliases).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(address)
                .append(HOSTNAME, hostname).append(ALIASES, aliases).toString();
    }

}
