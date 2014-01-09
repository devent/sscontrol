/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.services;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.security.banning.Backend;
import com.anrisoftware.sscontrol.security.banning.Banning;
import com.anrisoftware.sscontrol.security.banning.BanningFactory;
import com.anrisoftware.sscontrol.security.banning.Type;
import com.anrisoftware.sscontrol.security.ignoring.Ignoring;
import com.anrisoftware.sscontrol.security.ignoring.IgnoringFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Service to secure.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Service {

    private static final String TYPE = "type";

    private static final String BACKEND = "backend";

    private static final String BANNING = "banning";

    private static final String IGNORING_ADDRESSES = "ignoring addresses";

    private static final String NOTIFY_ADDRESS = "notify address";

    private static final String NAME = "name";

    private final com.anrisoftware.sscontrol.core.api.Service service;

    private final String name;

    @Inject
    private ServiceArgsLogger log;

    @Inject
    private IgnoringFactory ignoringFactory;

    @Inject
    private BanningFactory banningFactory;

    private String notifyAddress;

    private Ignoring ignoring;

    private Banning banning;

    private Backend nextBackend;

    private Type nextType;

    /**
     * @see ServiceFactory#create(com.anrisoftware.sscontrol.core.api.Service,
     *      Map)
     */
    @Inject
    Service(ServiceArgs aargs,
            @Assisted com.anrisoftware.sscontrol.core.api.Service service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.name = aargs.name(service, args);
        if (aargs.haveNotify(args)) {
            this.notifyAddress = aargs.notify(service, name, args);
        }
    }

    public com.anrisoftware.sscontrol.core.api.Service getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public String getNotifyAddress() {
        return notifyAddress;
    }

    public void ignore(Map<String, Object> args) {
        Ignoring ignoring = ignoringFactory.create(service, args);
        log.ignoreSet(this, service, ignoring);
        this.ignoring = ignoring;
    }

    public void setIgnoring(Ignoring ignoring) {
        this.ignoring = ignoring;
    }

    public Ignoring getIgnoring() {
        return ignoring;
    }

    public void banning(Map<String, Object> args) {
        if (nextBackend != null) {
            args.put(BACKEND, nextBackend);
        }
        if (nextType != null) {
            args.put(TYPE, nextType);
        }
        Banning banning = banningFactory.create(service, args);
        log.banningSet(this, service, banning);
        this.banning = banning;
        this.nextBackend = null;
        this.nextType = null;
    }

    public void setBanning(Banning banning) {
        this.banning = banning;
    }

    public Banning getBanning() {
        return banning;
    }

    public void polling() {
        this.nextBackend = Backend.polling;
    }

    public void auto() {
        this.nextBackend = Backend.auto;
    }

    public void deny() {
        this.nextType = Type.deny;
    }

    public void reject() {
        this.nextType = Type.reject;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append(NAME, name);
        builder = notifyAddress != null ? builder.append(NOTIFY_ADDRESS,
                notifyAddress) : builder;
        builder = ignoring != null ? builder.append(IGNORING_ADDRESSES,
                ignoring) : builder;
        builder = banning != null ? builder.append(BANNING, banning) : builder;
        return builder.toString();
    }
}
