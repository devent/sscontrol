/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.resetdomains;

import java.util.Map;

/**
 * Factory to create reset domains.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ResetDomainsFactory {

	/**
	 * Sets the arguments for reset domains.
	 * 
	 * @return the {@link ResetDomains}.
	 */
	ResetDomains create(Map<String, Object> args);
}
