/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd.
 * 
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.domain;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthFactory;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapFactory;
import com.anrisoftware.sscontrol.httpd.statements.redirect.Redirect;
import com.anrisoftware.sscontrol.httpd.statements.redirect.RedirectFactory;
import com.anrisoftware.sscontrol.httpd.statements.redirect.RedirectToWwwHttp;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain entry.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Domain {

	private static final String USE = "use";
	private static final String ROOT = "root";
	private static final String PORT = "port";
	private static final String ADDRESS = "address";

	@Inject
	private DomainLogger log;

	@Inject
	private RedirectFactory redirectFactory;

	@Inject
	private AuthFactory authFactory;

	@Inject
	private AuthLdapFactory authLdapFactory;

	private final String name;

	private final List<Redirect> redirects;

	private final List<AbstractAuth> auths;

	private final List<WebService> services;

	@Inject
	private Map<String, WebServiceFactory> serviceFactories;

	@Inject
	private DomainUser domainUser;

	private String address;

	private int port;

	private String documentRoot;

	private String useDomain;

	/**
	 * @see DomainFactory#create(Map, String)
	 */
	@Inject
	Domain(@Assisted Map<String, Object> args, @Assisted String name) {
		this(args, 80, name);
	}

	protected Domain(Map<String, Object> args, int port, String name) {
		this.name = name;
		this.redirects = new ArrayList<Redirect>();
		this.auths = new ArrayList<AbstractAuth>();
		this.services = new ArrayList<WebService>();
		this.port = port;
		if (args.containsKey(ADDRESS)) {
			this.address = (String) args.get(ADDRESS);
		}
		if (args.containsKey(PORT)) {
			this.port = (Integer) args.get(PORT);
		}
		if (args.containsKey(ROOT)) {
			this.documentRoot = (String) args.get(ROOT);
		}
		if (args.containsKey(USE)) {
			this.useDomain = (String) args.get(USE);
		}
	}

	public String getName() {
		return name;
	}

	public String getNamePattern() {
		return name.replaceAll("\\.", "\\\\.");
	}

	public String getFileName() {
		return format("100-robobee-%s.conf", getName());
	}

	/**
	 * Returns the directory of the site for this domain.
	 */
	public String getSiteDirectory() {
		if (useDomain != null) {
			return format("%s/web", useDomain);
		} else {
			return format("%s/web", getName());
		}
	}

	/**
	 * Returns the server alias of this directive. The server alias can be a
	 * sub-domain, for example www.domain.com.
	 */
	public String getServerAlias() {
		boolean www = findDirective(asList(new Class<?>[] {
				RedirectToWwwHttp.class, RedirectToWwwHttp.class }));
		return www ? format("www.%s", getName()) : getName();
	}

	private boolean findDirective(List<Class<?>> directives) {
		return redirects.containsAll(directives);
	}

	public void user(Map<String, Object> map, String name) {
		domainUser.setUser(name);
		domainUser.setGroup(map.get("group"));
	}

	public DomainUser getDomainUser() {
		return domainUser;
	}

	public void address(String address) {
		this.address = address;
		log.addressSet(this, address);
	}

	public String getAddress() {
		return address;
	}

	public void port(int port) {
		this.port = port;
		log.portSet(this, port);
	}

	public int getPort() {
		return port;
	}

	public void documentRoot(String root) {
		this.documentRoot = root;
		log.documentRootSet(this, root);
	}

	public String getDocumentRoot() {
		return documentRoot;
	}

	public void useDomain(String use) {
		this.useDomain = use;
		log.useDomainSet(this, use);
	}

	public String getUseDomain() {
		return useDomain;
	}

	public void redirect(Object s) {
	}

	public void to_www() {
		Redirect redirect = redirectFactory.createToWwwHttp(this);
		addRedirect(redirect);
		log.redirectToWwwAdded(this, redirect);
	}

	public void http_to_https() {
		Redirect redirect = redirectFactory.createHttpToHttps(this);
		addRedirect(redirect);
		log.redirectHttpToHttpsAdded(this, redirect);
	}

	public List<Redirect> getRedirects() {
		return redirects;
	}

	protected final void addRedirect(Redirect redirect) {
		redirects.add(redirect);
	}

	protected final RedirectFactory getRedirectFactory() {
		return redirectFactory;
	}

	public AbstractAuth auth(Map<String, Object> args, String name, Object s) {
		AbstractAuth auth = null;
		if (args.containsKey("provider")) {
			AuthProvider provider = (AuthProvider) args.get("provider");
			switch (provider) {
			case file:
				auth = authFactory.create(args, name);
				break;
			case ldap:
				auth = authLdapFactory.create(args, name);
				break;
			}
		}
		auth = auth == null ? authFactory.create(args, name) : auth;
		auths.add(auth);
		return auth;
	}

	public List<AbstractAuth> getAuths() {
		return auths;
	}

	public WebService setup(String name, Object s) {
		return setup(new HashMap<String, Object>(), name, s);
	}

	public WebService setup(Map<String, Object> map, String name, Object s) {
		WebServiceFactory factory = serviceFactories.get(name);
		WebService service = factory.create(map);
		services.add(service);
		log.servicesAdded(this, service);
		return service;
	}

	public List<WebService> getServices() {
		return services;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(address).append(port).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Domain rhs = (Domain) obj;
		return new EqualsBuilder().append(address, rhs.getAddress())
				.append(port, rhs.getPort()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append(domainUser).toString();
	}
}
