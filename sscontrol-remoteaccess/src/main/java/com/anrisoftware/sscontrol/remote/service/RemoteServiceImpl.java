/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.service;

import static com.anrisoftware.sscontrol.remote.service.RemoteFactory.NAME;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.bindings.Address;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.bindings.BindingArgs;
import com.anrisoftware.sscontrol.core.bindings.BindingFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.remote.user.User;
import com.anrisoftware.sscontrol.remote.user.UserFactory;

/**
 * Remote access service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class RemoteServiceImpl extends AbstractService implements RemoteService {

    private static final String USER_NAME = "name";

    private final List<User> users;

    @Inject
    private RemoteServiceImplLogger log;

    @Inject
    private UserFactory userFactory;

    @Inject
    private Binding binding;

    @Inject
    private BindingArgs bindingArgs;

    RemoteServiceImpl() {
        this.users = new ArrayList<User>();
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
     * Returns the remote access service name.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Sets the IP addresses or host names to where to bind the remote service.
     *
     * @see BindingFactory#create(Map, String...)
     */
    public void bind(Map<String, Object> args) throws ServiceException {
        List<Address> addresses = bindingArgs.createAddress(this, args);
        binding.addAddress(addresses);
        log.bindingSet(this, binding);
    }

    /**
     * Sets the IP addresses or host names to where to bind the remote service.
     *
     * @see BindingFactory#create(BindingAddress)
     */
    public void bind(BindingAddress address) throws ServiceException {
        binding.addAddress(address);
        log.bindingSet(this, binding);
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    /**
     * Entry point for the remote access service script.
     *
     * @param statements
     *            the remote access statements.
     *
     * @return this {@link Service}.
     */
    public Service remote(Object statements) {
        return this;
    }

    public void user(Map<String, Object> args, String name) {
        user(args, name, null);
    }

    public User user(Map<String, Object> args, String name, Object s) {
        args.put(USER_NAME, name);
        User user = userFactory.create(this, args);
        log.userAdded(this, user);
        users.add(user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    /**
     * @see BindingAddress#local
     */
    public BindingAddress getLocal() {
        return BindingAddress.local;
    }

    /**
     * @see BindingAddress#all
     */
    public BindingAddress getAll() {
        return BindingAddress.all;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
