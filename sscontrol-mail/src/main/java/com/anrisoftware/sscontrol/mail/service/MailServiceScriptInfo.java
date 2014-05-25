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
package com.anrisoftware.sscontrol.mail.service;

import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Returns extended information about the mail service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class MailServiceScriptInfo extends ServiceScriptInfo {

	/**
	 * Returns the storage type of the mail service for the virtual domains and
	 * users. For example, postfix can store the virtual domains and users in
	 * hash files, in MySQL databases.
	 * 
	 * @return the storage type {@link String} name.
	 */
	public abstract String getStorage();
}
