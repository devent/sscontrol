/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.statements;

import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Aliases}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AliasesLogger extends AbstractLogger {

	private static final String ALIAS_ADDED2 = "Alias '{}' added to dns service.";
	private static final String ALIAS_ADDED = "Alias {} added to {}.";
	private static final String ALIAS_NULL = "Alias must not be null for %s.";

	/**
	 * Creates a logger for {@link Aliases}.
	 */
	public AliasesLogger() {
		super(Aliases.class);
	}

	void checkAlias(Aliases aliases, Alias alias) {
		notNull(alias, ALIAS_NULL, aliases);
	}

	void aliasAdded(Aliases aliases, Alias alias) {
		if (log.isDebugEnabled()) {
			log.debug(ALIAS_ADDED, alias, aliases);
		} else {
			log.info(ALIAS_ADDED2, alias.getName());
		}
	}

}
