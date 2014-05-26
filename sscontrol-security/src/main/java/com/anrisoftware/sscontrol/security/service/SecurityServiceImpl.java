/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.security.service;

import static com.anrisoftware.sscontrol.security.service.SecurityFactory.NAME;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.security.services.Service;
import com.anrisoftware.sscontrol.security.services.ServiceFactory;

/**
 * Security service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class SecurityServiceImpl extends AbstractService implements
        SecurityService {

    private final List<Service> services;

    @Inject
    private SecurityServiceImplLogger log;

    @Inject
    private DebugLoggingFactory debugFactory;

    @Inject
    private ServiceFactory serviceFactory;

    private DebugLogging debug;

    SecurityServiceImpl() {
        this.services = new ArrayList<Service>();
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

    /**
     * Entry point for the security service script.
     * 
     * @param statements
     *            the security statements.
     * 
     * @return this {@link SecurityService}.
     */
    public SecurityService security(Object statements) {
        return this;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public Service service(Map<String, Object> args, Object s) {
        Service service = serviceFactory.create(this, args);
        services.add(service);
        log.serviceAdded(this, service);
        return service;
    }

    @Override
    public List<Service> getServices() {
        return services;
    }

    /**
     * Sets the debug logging for the database server.
     *
     * @see DebugLoggingFactory#create(Map)
     */
    public void debug(Map<String, Object> args) {
        this.debug = debugFactory.create(args);
        log.debugSet(this, debug);
    }

    @Override
    public void setDebug(DebugLogging debug) {
        this.debug = debug;
    }

    @Override
    public DebugLogging getDebug() {
        return debug;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
