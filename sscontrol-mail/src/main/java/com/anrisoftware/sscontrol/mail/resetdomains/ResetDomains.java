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
package com.anrisoftware.sscontrol.mail.resetdomains;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.google.inject.assistedinject.Assisted;

/**
 * Reset before adding the specified domains/users/aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ResetDomains {

	private static final String ALIASES = "aliases";
	private static final String USERS = "users";
	private static final String DOMAINS = "domains";

	private Map<String, Object> args;

	private ResetDomainsLogger log;

	private boolean resetDomains;

	private boolean resetUsers;

	private boolean resetAliases;

	@Inject
	ResetDomains(@Assisted Map<String, Object> args) {
		this.args = args;
		this.resetDomains = false;
		this.resetUsers = false;
		this.resetAliases = false;
	}

	@Inject
	void setResetDomainsLogger(ResetDomainsLogger logger) {
		this.log = logger;
		if (args.containsKey(DOMAINS)) {
			setResetDomains(args.get(DOMAINS));
		}
		if (args.containsKey(USERS)) {
			setResetUsers(args.get(USERS));
		}
		if (args.containsKey(ALIASES)) {
			setResetAliases(args.get(ALIASES));
		}
		args = null;
	}

	private void setResetDomains(Object flag) {
		log.checkFlag(flag);
		if (flag instanceof YesNoFlag) {
			this.resetDomains = ((YesNoFlag) flag).asBoolean();
		} else {
			this.resetDomains = (Boolean) flag;
		}
		setResetUsers(resetDomains);
		setResetAliases(resetDomains);
	}

	public boolean isResetDomains() {
		return resetDomains;
	}

	private void setResetUsers(Object flag) {
		log.checkFlag(flag);
		if (flag instanceof YesNoFlag) {
			this.resetUsers = ((YesNoFlag) flag).asBoolean();
		} else {
			this.resetUsers = (Boolean) flag;
		}
		setResetAliases(resetDomains);
	}

	public boolean isResetUsers() {
		return resetUsers;
	}

	private void setResetAliases(Object flag) {
		log.checkFlag(flag);
		if (flag instanceof YesNoFlag) {
			this.resetAliases = ((YesNoFlag) flag).asBoolean();
		} else {
			this.resetAliases = (Boolean) flag;
		}
	}

	public boolean isResetAliases() {
		return resetAliases;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("reset domains", resetDomains)
				.append("reset users", resetUsers)
				.append("reset aliases", resetAliases).toString();
	}
}
