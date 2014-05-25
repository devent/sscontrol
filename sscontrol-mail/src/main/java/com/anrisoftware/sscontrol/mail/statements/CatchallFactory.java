/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.statements;

/**
 * Factory to creater a new catch-all alias for the domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CatchallFactory {

	/**
	 * Creates the catch-all alias.
	 * 
	 * @param domain
	 *            the {@link Domain} of the alias.
	 * 
	 * @param destination
	 *            the destination {@link String}.
	 * 
	 * @return the {@link Catchall}.
	 */
	Catchall create(Domain domain, String destination);
}
