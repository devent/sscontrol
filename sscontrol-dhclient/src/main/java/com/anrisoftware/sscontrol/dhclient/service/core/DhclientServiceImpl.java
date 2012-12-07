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
package com.anrisoftware.sscontrol.dhclient.service.core;

import static com.anrisoftware.sscontrol.dhclient.service.core.DhclientFactory.NAME;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.dhclient.service.statements.Declaration;
import com.anrisoftware.sscontrol.dhclient.service.statements.OptionDeclaration;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory;
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory;
import com.google.inject.Provider;

class DhclientServiceImpl extends GroovyObjectSupport implements Service {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 8026832603525631371L;

	private final DhclientServiceImplLogger log;

	private final Map<String, Provider<Script>> scripts;

	private ProfileService profile;

	private List<Declaration> options;

	private List<OptionDeclaration> sends;

	private List<Declaration> requests;

	private List<OptionDeclaration> prepends;

	private final TemplatesFactory templatesFactory;

	@Inject
	private TokensTemplateWorkerFactory tokensTemplateWorkerFactory;

	@Inject
	private ScriptCommandWorkerFactory scriptCommandWorkerFactory;

	@Inject
	DhclientServiceImpl(DhclientServiceImplLogger logger,
			Map<String, Provider<Script>> scripts, TemplatesFactory templates,
			@Named("hostname-service-properties") Properties properties) {
		this.log = logger;
		this.scripts = scripts;
		this.templatesFactory = templates;
	}

	public Object hostname(Closure<?> closure) {
		return this;
	}

	public Object set_hostname(String name) {
		log.checkHostname(this, name);
		hostname = name;
		log.hostnameSet(this, name);
		return this;
	}

	public String getHostname() {
		return hostname;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	public ProfileService getProfile() {
		return profile;
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
		return new ToStringBuilder(this).toString();
	}

}
