/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.bindings;

import static org.apache.commons.lang3.StringUtils.trim;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;

/**
 * Parses binding arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BindingArgs {

	public static final String ADDRESS = "address";

	public static final String ADDRESSES = "addresses";

	@Inject
	private BindingLogger log;

	@Inject
	private StringToListFactory listFactory;

	public String address(Map<String, Object> args) {
		Object object = args.get(ADDRESS);
		log.checkAddress(object);
		return trim(object.toString());
	}

	public Collection<String> addresses(Map<String, Object> args) {
		Object object = args.get(ADDRESSES);
		log.checkAddress(object);
		return listFactory.create(object).getList();
	}
}
