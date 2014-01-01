/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import static java.util.ServiceLoader.load;
import groovy.lang.Script;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileProperties;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;

/**
 * Sets globally available variables for the Groovy script.
 * 
 * <ul>
 * <li>{@code system:} the system profile properties;
 * <li>{@code profile:} the profile properties of the script;
 * <li>{@code service:} this service;
 * <li>{@code name:} the name of the service.
 * </ul>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractService implements Service {

    private Injector injector;

    private ProfileService profile;

    private AbstractServiceLogger log;

    private String refservice;

    @Inject
    void setAbstractServiceLogger(AbstractServiceLogger logger) {
        this.log = logger;
    }

    @Inject
    void setInjector(Injector injector) {
        this.injector = injector;
    }

    /**
     * Sets the profile for the service.
     * 
     * @param profile
     *            the {@link ProfileService}.
     */
    public void setProfile(ProfileService profile) {
        this.profile = profile;
        log.profileSet(this, profile);
    }

    /**
     * Returns the profile of the service.
     * 
     * @return the {@link ProfileService}.
     */
    public ProfileService getProfile() {
        return profile;
    }

    @Override
    public Service call() throws ServiceException {
        String profileName = profile.getProfileName();
        Script script = getScript(profileName);
        script.setProperty("profile", profile.getEntry(getName()));
        script.setProperty("service", this);
        script.setProperty("name", getName());
        injectScript(script);
        script.run();
        return this;
    }

    /**
     * Injects the dependencies of the script.
     * 
     * @param script
     *            the {@link Script}.
     */
    protected void injectScript(Script script) {
        injector.injectMembers(script);
    }

    /**
     * Returns the script to the specified profile.
     * 
     * @param profileName
     *            the name of the profile.
     * 
     * @return the {@link Script}.
     * 
     * @throws ServiceException
     *             if there were some error returning the script.
     */
    protected abstract Script getScript(String profileName)
            throws ServiceException;

    /**
     * Finds the script factory with the specified service name.
     * 
     * @param name
     *            the service name {@link String}.
     * 
     * @return the {@link List} of {@link ServiceScriptFactory}.
     * 
     * @throws ServiceException
     *             if no script factory with the specified name was found.
     */
    protected final ServiceScriptFactory findScriptFactory(String name)
            throws ServiceException {
        log.searchScriptFactory(this, name);
        ProfileService profile = getProfile();
        ServiceLoader<ServiceScriptFactory> loader = load(ServiceScriptFactory.class);
        for (ServiceScriptFactory scriptFactory : loader) {
            ServiceScriptInfo info = scriptFactory.getInfo();
            log.foundServiceScript(this, info);
            if (serviceScriptCompare(info, name, profile)) {
                scriptFactory.setParent(injector);
                return scriptFactory;
            }
        }
        throw log.errorFindServiceScript(this, profile, name);
    }

    /**
     * Compares the service script name to the specified service information.
     * 
     * @param info
     *            the {@link ServiceScriptInfo}.
     * 
     * @param serviceName
     *            the name of the service.
     * 
     * @param profile
     *            the service {@link ProfileService}.
     * 
     * @return {@code true} if the service script that is specified by the
     *         service script information is the correct one for the service.
     */
    protected boolean serviceScriptCompare(ServiceScriptInfo info,
            String serviceName, ProfileService profile) {
        ProfileProperties entry = getProfile().getEntry(serviceName);
        Object service = findService(entry);
        return isServiceScriptEquals(info, profile, service);
    }

    private Object findService(ProfileProperties entry) {
        Object service = entry.get("service");
        service = service == null ? getName() : service;
        if (getRefservice() != null) {
            Map<String, String> map = asServicesMap(entry);
            service = map.get(getRefservice());
        }
        return service;
    }

    /**
     * Compares the service script name to the specified service information.
     * 
     * @param info
     *            the {@link ServiceScriptInfo}.
     * 
     * @param profile
     *            the service {@link ProfileService}.
     * 
     * @param service
     *            the service.
     * 
     * @return {@code true} if the service script that is specified by the
     *         service script information is the correct one for the service.
     */
    protected boolean isServiceScriptEquals(ServiceScriptInfo info,
            ProfileService profile, Object service) {
        return info.getProfileName().equals(profile.getProfileName())
                && info.getServiceName().equals(service);
    }

    @SuppressWarnings({ "unchecked" })
    private Map<String, String> asServicesMap(ProfileProperties entry) {
        Object obj = entry.get("service");
        return (Map<String, String>) obj;
    }

    public void refservice(String refservice) {
        this.refservice = refservice;
        log.refserviceSet(this, refservice);
    }

    @Override
    public String getRefservice() {
        return refservice;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append("name",
                getName());
        if (profile != null) {
            builder.append("profile", profile.getProfileName());
        }
        return builder.toString();
    }
}
