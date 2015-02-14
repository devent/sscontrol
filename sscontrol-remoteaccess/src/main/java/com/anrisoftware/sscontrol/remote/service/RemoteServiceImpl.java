/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
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
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTable;
import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTableFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
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

    private static final String USER_NAME = "user";
    private static final String DEBUG_KEY = "debug";

    private final List<User> users;

    @Inject
    private RemoteServiceImplLogger log;

    @Inject
    private UserFactory userFactory;

    private StatementsTable statementsTable;

    private BindingAddressesStatementsTable bindingAddresses;

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

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(this, NAME);
        table.addAllowed(DEBUG_KEY);
        table.setAllowArbitraryKeys(true, DEBUG_KEY);
        this.statementsTable = table;
    }

    @Inject
    public final void setBindingAddressesStatementsTable(
            BindingAddressesStatementsTableFactory factory) {
        this.bindingAddresses = factory.create(this, NAME);
    }

    /**
     * Returns the remote access service name.
     */
    @Override
    public String getName() {
        return NAME;
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

    @Override
    public Map<String, Object> debugLogging(String key) {
        return statementsTable.tableKeys(DEBUG_KEY, key);
    }

    @Override
    public Map<String, List<Integer>> getBindingAddresses() {
        return bindingAddresses.getBindingAddresses();
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
     * @see BindingAddress#all
     */
    public BindingAddress getAll() {
        return BindingAddress.all;
    }

    /**
     * @see BindingAddress#local
     */
    public BindingAddress getLocal() {
        return BindingAddress.local;
    }

    public Object methodMissing(String name, Object args) {
        try {
            return statementsTable.methodMissing(name, args);
        } catch (StatementsException e) {
            return bindingAddresses.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
