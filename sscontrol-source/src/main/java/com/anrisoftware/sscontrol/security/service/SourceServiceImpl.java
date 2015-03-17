/*
 * Copyright 2015-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source.
 *
 * sscontrol-source is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import static com.anrisoftware.sscontrol.security.service.SourceFactory.NAME;
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
import com.anrisoftware.sscontrol.source.service.SourceService;
import com.anrisoftware.sscontrol.source.service.SourceServiceFactory;
import com.anrisoftware.sscontrol.source.service.SourceServiceFactoryFactory;
import com.anrisoftware.sscontrol.source.service.SourceServiceInfo;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;
import com.google.inject.Injector;

/**
 * Source code management service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class SourceServiceImpl extends AbstractService implements SourceService {

    private final List<SourceSetupService> services;

    @Inject
    private SourceServiceImplLogger log;

    @Inject
    private Injector injector;

    @Inject
    private Map<String, SourceServiceFactory> serviceFactories;

    @Inject
    private SourceServicesProvider servicesProvider;

    SourceServiceImpl() {
        this.services = new ArrayList<SourceSetupService>();
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
     * Entry point for the source code management service script.
     *
     * @param statements
     *            the security statements.
     *
     * @return this {@link SourceService}.
     */
    public SourceService source(Object statements) {
        return this;
    }

    public SourceSetupService setup(String name, Object s)
            throws ServiceException {
        return setup(new HashMap<String, Object>(), name, s);
    }

    public SourceSetupService setup(Map<String, Object> map, String name,
            Object s) throws ServiceException {
        SourceServiceFactory factory = serviceFactories.get(name);
        factory = findService(name, factory);
        log.checkService(this, factory, name);
        SourceSetupService service = factory.create(map);
        services.add(service);
        log.servicesAdded(this, service);
        return service;
    }

    private SourceServiceFactory findService(final String name,
            SourceServiceFactory factory) throws ServiceException {
        return factory != null ? factory : findService(name).getFactory();
    }

    private SourceServiceFactoryFactory findService(final String name)
            throws ServiceException {
        SourceServiceFactoryFactory find = servicesProvider
                .find(new SourceServiceInfo() {

                    @Override
                    public String getServiceName() {
                        return name;
                    }
                });
        find.setParent(injector.getParent());
        return find;
    }

    @Override
    public List<SourceSetupService> getServices() {
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
