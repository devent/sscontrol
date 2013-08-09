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
package com.anrisoftware.sscontrol.dhclient.statements;

import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.dhclient.service.DhclientPropertiesProvider;

/**
 * Collection of request declarations.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class RequestDeclarations implements Serializable, Iterable<Declaration> {

	private final List<Declaration> requests;

	private final DeclarationFactory declarationFactory;

	private final RequestDeclarationsLogger log;

	@Inject
	RequestDeclarations(RequestDeclarationsLogger logger,
			DeclarationFactory declarationFactory, DhclientPropertiesProvider p) {
		this.log = logger;
		this.declarationFactory = declarationFactory;
		this.requests = new ArrayList<Declaration>();
		addDefaultRequests(p.get());
	}

	private void addDefaultRequests(ContextProperties p) {
		for (String req : p.getListProperty("default_requests")) {
			requests.add(declarationFactory.create(req));
		}
	}

	/**
	 * Adds new request.
	 * 
	 * @param decl
	 *            the request declaration.
	 */
	public void add(String decl) {
		if (decl.startsWith("!")) {
			remove(decl.substring(1));
		} else {
			Declaration declaration = declarationFactory.create(decl);
			log.reguestAdded(this, declaration);
		}
	}

	/**
	 * Removes the specified request name.
	 * 
	 * @param decl
	 *            the request declaration.
	 */
	public void remove(String decl) {
		Declaration req = declarationFactory.create(decl);
		if (requests.remove(req)) {
			log.reguestRemoved(this, req);
		} else {
			log.noRequestFoundForRemoval(this, req);
		}
	}

	@Override
	public Iterator<Declaration> iterator() {
		return unmodifiableList(requests).iterator();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(requests).toString();
	}
}
