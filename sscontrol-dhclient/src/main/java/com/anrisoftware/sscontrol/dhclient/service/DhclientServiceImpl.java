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

import static com.anrisoftware.sscontrol.dhclient.service.DhclientFactory.NAME;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.split;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.dhclient.statements.Declaration;
import com.anrisoftware.sscontrol.dhclient.statements.DeclarationFactory;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclaration;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclarationFactory;
import com.anrisoftware.sscontrol.dhclient.statements.RequestDeclarations;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory;
import com.google.inject.Provider;

/**
 * The Dhclient service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DhclientServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = -8433434141420234300L;

	private final DhclientServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	@Inject
	private TokensTemplateWorkerFactory tokensTemplateWorkerFactory;

	@Inject
	private ScriptCommandWorkerFactory scriptCommandWorkerFactory;

	private final DeclarationFactory declarationFactory;

	private final OptionDeclarationFactory optionDeclarationFactory;

	private ProfileService profile;

	private Declaration option;

	private final List<OptionDeclaration> sends;

	private final RequestDeclarations requests;

	private final List<OptionDeclaration> prepends;

	private final TemplatesFactory templatesFactory;

	@Inject
	DhclientServiceImpl(DhclientServiceImplLogger logger,
			DeclarationFactory declarationFactory,
			OptionDeclarationFactory optionDeclarationFactory,
			RequestDeclarations requests,
			Map<String, Provider<Script>> scripts, TemplatesFactory templates,
			@Named("dhclient-defaults-properties") Properties properties) {
		this.log = logger;
		this.declarationFactory = declarationFactory;
		this.optionDeclarationFactory = optionDeclarationFactory;
		this.scripts = scripts;
		this.templatesFactory = templates;
		this.sends = new ArrayList<OptionDeclaration>();
		this.requests = requests;
		this.prepends = new ArrayList<OptionDeclaration>();
		ContextProperties p = new ContextProperties(this, properties);
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

	/**
	 * Entry point in the Dhclient script.
	 * 
	 * @param closure
	 *            the Dhclient script statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Object dhclient(Closure<?> closure) {
		return this;
	}

	/**
	 * Adds a new request.
	 * 
	 * @param decl
	 *            the request declaration.
	 * 
	 * @return this {@link Service}.
	 */
	public Object requests(String decl) {
		requests.add(decl);
		return this;
	}

	/**
	 * Adds a new prepend.
	 * 
	 * @param option
	 *            the prepend option.
	 * 
	 * @param decl
	 *            the prepend declaration.
	 * 
	 * @return this {@link Service}.
	 */
	public Object prepend(String option, String decl) {
		OptionDeclaration declaration;
		declaration = optionDeclarationFactory.create(option, decl);
		prepends.add(declaration);
		log.prependAdded(this, declaration);
		return this;
	}

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Sets the profile for the service.
	 * 
	 * @param newProfile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	/**
	 * Returns the profile of the service.
	 * 
	 * @return the {@link ProfileService}.
	 */
	public ProfileService getProfile() {
		return profile;
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
	 * @return an unmodifiable {@link Iterable} of the {@link Declaration}
	 *         request declarations.
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

	@Override
	public Service call() throws ServiceException {
		String name = profile.getProfileName();
		Script script = scripts.get(name).get();
		Map<Class<?>, Object> workers = getWorkers();
		script.setProperty("workers", workers);
		script.setProperty("templatesFactory", templatesFactory);
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(NAME));
		script.setProperty("service", this);
		script.setProperty("name", name);
		script.run();
		return this;
	}

	private Map<Class<?>, Object> getWorkers() {
		Map<Class<?>, Object> workers = new HashMap<Class<?>, Object>();
		workers.put(TokensTemplateWorkerFactory.class,
				tokensTemplateWorkerFactory);
		workers.put(ScriptCommandWorkerFactory.class,
				scriptCommandWorkerFactory);
		return workers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("profile", profile).toString();
	}

}
