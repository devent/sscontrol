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

import java.util.List;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.dhclient.statements.Declaration;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclaration;

/**
 * Dhclient service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DhclientService extends Service {

    /**
     * Returns the dhclient service name.
     */
    @Override
    String getName();

    /**
     * Sets the option declaration.
     * 
     * @param option
     *            the {@link Declaration} option declaration.
     */
    void setOption(Declaration option);

    /**
     * Returns the option declaration.
     * 
     * @return the {@link Declaration} option declaration.
     */
    Declaration getOption();

    /**
     * Adds the prepend declaration.
     * 
     * @param declaration
     *            the prepend {@link OptionDeclaration}.
     */
    void addPrepend(OptionDeclaration declaration);

    /**
     * Returns the prepend declarations.
     * 
     * @return an unmodifiable {@link List} of the {@link OptionDeclaration}
     *         prepend declarations.
     */
    List<OptionDeclaration> getPrepends();

    /**
     * Adds the request declaration.
     * 
     * @param declaration
     *            the request {@link Declaration}.
     */
    void addRequest(Declaration request);

    /**
     * Returns the request declarations.
     * 
     * @return an unmodifiable {@link List} of the {@link Declaration} request
     *         declarations.
     */
    List<Declaration> getRequests();

    /**
     * Adds the send declaration.
     * 
     * @param declaration
     *            the send {@link OptionDeclaration}.
     */
    void addSend(OptionDeclaration declaration);

    /**
     * Returns the send declarations.
     * 
     * @return an unmodifiable {@link List} of the {@link OptionDeclaration}
     *         send declarations.
     */
    List<OptionDeclaration> getSends();

}
