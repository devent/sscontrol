/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hosts.service;

import static com.anrisoftware.sscontrol.hosts.service.HostsServiceFactory.NAME;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.hosts.api.HostsService;
import com.anrisoftware.sscontrol.hosts.host.Host;
import com.anrisoftware.sscontrol.hosts.host.HostFactory;

/**
 * Hosts service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class HostsServiceImpl extends AbstractService implements HostsService {

    private static final String HOST_ADDRESS = "address";

    private static final String HOSTS = "hosts";

    private final HostsServiceImplLogger log;

    private final List<Host> hosts;

    @Inject
    private HostFactory hostFactory;

    @Inject
    HostsServiceImpl(HostsServiceImplLogger logger) {
        this.log = logger;
        this.hosts = new ArrayList<Host>();
    }

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        ServiceScriptFactory scriptFactory = findScriptFactory(NAME);
        return (Script) scriptFactory.getScript();
    }

    /**
     * Because we load the script from a script service the dependencies are
     * already injected.
     */
    @Override
    protected void injectScript(Script script) {
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Starts the hosts configuration.
     * 
     * @return this {@link HostsServiceImpl}.
     */
    public Object hosts(Object closure) {
        return this;
    }

    /**
     * Adds a new host entry with the specified IP address.
     */
    public void ip(Map<String, Object> args, String address) {
        args.put(HOST_ADDRESS, address);
        Host host = hostFactory.create(this, args);
        hosts.add(host);
        log.hostAdded(this, host);
    }

    @Override
    public List<Host> getHosts() {
        return hosts;
    }

    @Override
    public void addHostsHead(Collection<? extends Host> hosts) {
        int index = 0;
        for (Host host : hosts) {
            this.hosts.add(index++, host);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append(HOSTS, hosts).toString();
    }

}
