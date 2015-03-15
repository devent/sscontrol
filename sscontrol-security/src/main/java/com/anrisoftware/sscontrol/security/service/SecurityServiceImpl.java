/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.google.inject.Injector;

/**
 * Security service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class SecurityServiceImpl extends AbstractService implements
        SecurityService {

    private final List<SecService> services;

    @Inject
    private SecurityServiceImplLogger log;

    @Inject
    private Injector injector;

    @Inject
    private Map<String, SecServiceFactory> serviceFactories;

    @Inject
    private SecServicesProvider servicesProvider;

    SecurityServiceImpl() {
        this.services = new ArrayList<SecService>();
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

    public SecService service(String name, Object s) throws ServiceException {
        return service(new HashMap<String, Object>(), name, s);
    }

    public SecService service(Map<String, Object> map, String name, Object s)
            throws ServiceException {
        SecServiceFactory factory = serviceFactories.get(name);
        factory = findService(name, factory);
        log.checkService(this, factory, name);
        SecService service = factory.create(map);
        services.add(service);
        log.servicesAdded(this, service);
        return service;
    }

    private SecServiceFactory findService(final String name,
            SecServiceFactory factory) throws ServiceException {
        return factory != null ? factory : findService(name).getFactory();
    }

    private SecServiceFactoryFactory findService(final String name)
            throws ServiceException {
        SecServiceFactoryFactory find = servicesProvider
                .find(new SecServiceInfo() {

                    @Override
                    public String getServiceName() {
                        return name;
                    }
                });
        find.setParent(injector.getParent());
        return find;
    }

    @Override
    public List<SecService> getServices() {
        return services;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
