/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceFactory.NAME;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.split;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.dhclient.statements.Declaration;
import com.anrisoftware.sscontrol.dhclient.statements.DeclarationFactory;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclaration;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclarationFactory;
import com.anrisoftware.sscontrol.dhclient.statements.RequestDeclarations;
import com.google.inject.Provider;

/**
 * The Dhclient service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class DhclientServiceImpl extends AbstractService {

	private final DhclientServiceImplLogger log;

	private final DeclarationFactory declarationFactory;

	private final OptionDeclarationFactory optionDeclarationFactory;

	private Declaration option;

	private final List<OptionDeclaration> sends;

	private final RequestDeclarations requests;

	private final List<OptionDeclaration> prepends;

	private final Map<String, Provider<Script>> scripts;

	@Inject
	DhclientServiceImpl(DhclientServiceImplLogger logger,
			DeclarationFactory declarationFactory,
			OptionDeclarationFactory optionDeclarationFactory,
			RequestDeclarations requests,
			Map<String, Provider<Script>> scripts,
			@Named("dhclient-defaults-properties") ContextProperties p) {
		this.scripts = scripts;
		this.log = logger;
		this.declarationFactory = declarationFactory;
		this.optionDeclarationFactory = optionDeclarationFactory;
		this.sends = new ArrayList<OptionDeclaration>();
		this.requests = requests;
		this.prepends = new ArrayList<OptionDeclaration>();
		setDefaultOption(p);
		setDefaultSends(p);
	}

	private void setDefaultOption(ContextProperties p) {
		option = declarationFactory.create(p.getProperty("default_option"));
	}

	private void setDefaultSends(ContextProperties p) {
		for (String decl : p.getListProperty("default_sends", ";,")) {
			String[] option = split(decl.trim(), " ");
			sends.add(optionDeclarationFactory.create(option[0], option[1]));
		}
	}

	@Override
	protected Script getScript(String profileName) {
		return scripts.get(profileName).get();
	}

	/**
	 * Returns the dhclient service name.
	 */
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

	/**
	 * Returns the option declaration.
	 * 
	 * @return the {@link Declaration} option declaration.
	 */
	public Declaration getOption() {
		return option;
	}

	/**
	 * Returns the prepend declarations.
	 * 
	 * @return an unmodifiable {@link List} of the {@link OptionDeclaration}
	 *         prepend declarations.
	 */
	public List<OptionDeclaration> getPrepends() {
		return unmodifiableList(prepends);
	}

	/**
	 * Returns the request declarations.
	 * 
	 * @return an unmodifiable {@link List} of the {@link Declaration} request
	 *         declarations.
	 */
	public List<Declaration> getRequests() {
		List<Declaration> list = new ArrayList<Declaration>();
		Iterator<Declaration> i = requests.iterator();
		while (i.hasNext()) {
			list.add(i.next());
		}
		return unmodifiableList(list);
	}

	/**
	 * Returns the send declarations.
	 * 
	 * @return an unmodifiable {@link List} of the {@link OptionDeclaration}
	 *         send declarations.
	 */
	public List<OptionDeclaration> getSends() {
		return unmodifiableList(sends);
	}

}
