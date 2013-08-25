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
package com.anrisoftware.sscontrol.httpd.service;

import static com.anrisoftware.sscontrol.httpd.service.HttpdFactory.NAME;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.httpd.statements.Domain;
import com.anrisoftware.sscontrol.httpd.statements.DomainFactory;

/**
 * Firewall service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class HttpdServiceImpl extends AbstractService {

	@Inject
	private HttpdServiceImplLogger log;

	@Inject
	private DomainFactory domainFactory;

	private final List<Domain> domains;

	HttpdServiceImpl() {
		this.domains = new ArrayList<Domain>();
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
	 * Returns the httpd service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Entry point for the httpd service script.
	 * 
	 * @param statements
	 *            the httpd statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Service httpd(Object statements) {
		return this;
	}

	/**
	 * @see DomainFactory#create(Map, String)
	 */
	public Domain domain(Map<String, Object> args, String name, Object s) {
		Domain domain = domainFactory.create(args, name);
		domains.add(domain);
		log.domainAdded(this, domain);
		return domain;
	}

	/**
	 * @see DomainFactory#createSsl(Map, String)
	 */
	public Domain ssl_domain(Map<String, Object> args, String name, Object s) {
		Domain domain = domainFactory.createSsl(args, name);
		domains.add(domain);
		log.sslDomainAdded(this, domain);
		return domain;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.toString();
	}

}