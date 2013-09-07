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
package com.anrisoftware.sscontrol.ldap.service;

import static com.anrisoftware.sscontrol.ldap.service.LdapFactory.NAME;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.split;
import groovy.lang.Script;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.ldap.dbindex.DbIndex;
import com.anrisoftware.sscontrol.ldap.dbindex.DbIndexFactory;
import com.anrisoftware.sscontrol.ldap.dbindex.IndexType;
import com.anrisoftware.sscontrol.ldap.statements.Admin;
import com.anrisoftware.sscontrol.ldap.statements.AdminFactory;
import com.anrisoftware.sscontrol.ldap.statements.Scripts;

/**
 * LDAP/service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LdapServiceImpl extends AbstractService {

	@Inject
	private LdapServiceImplLogger log;

	@Inject
	private AdminFactory adminFactory;

	private Admin admin;

	@Inject
	private Scripts scripts;

	@Inject
	private DbIndexFactory indexFactory;

	private final List<DbIndex> indices;

	LdapServiceImpl() {
		this.indices = new ArrayList<DbIndex>();
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
	 * Returns the {@code ldap} service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Entry point for the LDAP service script.
	 * 
	 * @param statements
	 *            the LDAP statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Service ldap(Object statements) {
		return this;
	}

	public void admin(Map<String, Object> args, String name) {
		this.admin = adminFactory.create(args, name);
		log.adminSet(this, admin);
	}

	public Admin getAdmin() {
		return admin;
	}

	public IndexType getPresent() {
		return IndexType.present;
	}

	public IndexType getApprox() {
		return IndexType.approx;
	}

	public IndexType getEquality() {
		return IndexType.equality;
	}

	public IndexType getNone() {
		return IndexType.none;
	}

	public IndexType getSubstring() {
		return IndexType.substring;
	}

	public void index(Map<String, Object> args, String namess) {
		Set<String> names = new HashSet<String>(asList(split(namess, ',')));
		Set<IndexType> types = new HashSet<IndexType>();
		if (args.containsKey("type")) {
			Object typeo = args.get("type");
			if (typeo instanceof IndexType) {
				types.add((IndexType) typeo);
			} else if (typeo instanceof Collection) {
				@SuppressWarnings("unchecked")
				Collection<IndexType> typesc = (Collection<IndexType>) typeo;
				types.addAll(typesc);
			}
		}
		DbIndex index = indexFactory.create(names, types);
		indices.add(index);
		log.indexAdded(this, index);
	}

	public List<DbIndex> getIndices() {
		return indices;
	}

	public void script(Object resource) throws URISyntaxException {
		this.scripts.addResource(resource);
		log.scriptResourceAdded(this, resource);
	}

	public Scripts getScripts() {
		return scripts;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.toString();
	}

}
