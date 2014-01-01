/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceFactory.NAME;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.dhclient.statements.Declaration;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclaration;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclarationFactory;
import com.anrisoftware.sscontrol.dhclient.statements.RequestDeclarations;
import com.google.inject.Provider;

/**
 * Dhclient service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class DhclientServiceImpl extends AbstractService implements DhclientService {

    private final List<OptionDeclaration> sends;

    private final List<OptionDeclaration> prepends;

    @Inject
    private DhclientServiceImplLogger log;

    @Inject
    private Map<String, Provider<Script>> scripts;

    @Inject
    private OptionDeclarationFactory optionDeclarationFactory;

    @Inject
    private RequestDeclarations requests;

    private Declaration option;

    @Inject
    DhclientServiceImpl() {
        this.sends = new ArrayList<OptionDeclaration>();
        this.prepends = new ArrayList<OptionDeclaration>();
    }

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        Provider<Script> provider = scripts.get(profileName);
        log.checkScript(this, provider, profileName);
        return provider.get();
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Entry point in the Dhclient script.
     * 
     * @return this {@link Service}.
     */
    public Service dhclient(Object closure) {
        return this;
    }

    /**
     * Adds a new request.
     * 
     * @param decl
     *            the request declaration.
     */
    public void requests(String decl) {
        requests.add(decl);
    }

    @Override
    public void addRequest(Declaration request) {
        requests.add(request);
    }

    @Override
    public List<Declaration> getRequests() {
        return requests.getRequests();
    }

    /**
     * Adds a new prepend.
     * 
     * @param option
     *            the prepend option.
     * 
     * @param decl
     *            the prepend declaration.
     */
    public void prepend(String option, String decl) {
        OptionDeclaration declaration;
        declaration = optionDeclarationFactory.create(option, decl);
        prepends.add(declaration);
        log.prependAdded(this, declaration);
    }

    @Override
    public void setOption(Declaration option) {
        this.option = option;
    }

    @Override
    public Declaration getOption() {
        return option;
    }

    @Override
    public void addPrepend(OptionDeclaration declaration) {
        prepends.add(declaration);
    }

    @Override
    public List<OptionDeclaration> getPrepends() {
        return unmodifiableList(prepends);
    }

    @Override
    public void addSend(OptionDeclaration declaration) {
        sends.add(declaration);
    }

    @Override
    public List<OptionDeclaration> getSends() {
        return unmodifiableList(sends);
    }

}
