/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-database.
 * 
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.statements;

import static org.apache.commons.lang3.StringUtils.trim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Binding for DNS/server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Binding implements Serializable {

	private static final String ADDRESS = "address";

	private static final String ADDRESSES = "addresses";

	@Inject
	private BindingLogger log;

	@Inject
	private StringToListFactory listFactory;

	private Map<String, Object> args;

	private List<String> addresses;

	private String[] array;

	@AssistedInject
	Binding() {
		this.addresses = new ArrayList<String>();
	}

	/**
	 * @see BindingFactory#create(Map)
	 */
	@AssistedInject
	Binding(@Assisted Map<String, Object> args, @Assisted String[] array) {
		this.args = args;
		this.array = array;
		this.addresses = new ArrayList<String>();
	}

	@Inject
	void setBindLogger(BindingLogger logger) {
		this.log = logger;
		if (args == null) {
			return;
		}
		if (args.containsKey(ADDRESS)) {
			addAddress(args.get(ADDRESS));
		}
		if (args.containsKey(ADDRESSES)) {
			addAddresses(args.get(ADDRESSES));
		}
		if (array != null) {
			addAddresses(array);
		}
		array = null;
		args = null;
	}

	private void addAddresses(Object object) {
		log.checkAddress(object);
		addresses = listFactory.create(object).getList();
	}

	private void addAddress(Object object) {
		log.checkAddress(object);
		addresses.add(trim(object.toString()));
	}

	private void addAddresses(String[] array) {
		for (String string : array) {
			addresses.add(trim(string));
		}
	}

	public List<String> getAddresses() {
		return addresses;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(ADDRESS, addresses).toString();
	}

}
