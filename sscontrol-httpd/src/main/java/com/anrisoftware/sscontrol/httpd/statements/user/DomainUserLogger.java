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
package com.anrisoftware.sscontrol.httpd.statements.user;

import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.gid_null;
import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.gid_number;
import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.group_null;
import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.ref_null;
import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.uid_null;
import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.uid_number;
import static com.anrisoftware.sscontrol.httpd.statements.user.DomainUserLogger._.user_null;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging messages for {@link DomainUser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DomainUserLogger extends AbstractLogger {

	enum _ {

        user_null("User cannot be null or blank for %s."),

        group_null("Group cannot be null or blank for %s."),

        uid_null("User ID cannot not be null for %s."),

        uid_number("User ID cannot must be a number for %s."),

        gid_null("Group ID cannot not be null for %s."),

        gid_number("Group ID cannot must be a number for %s."),

        ref_null("Referenced domain cannot be null or blank for %s.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link DomainUser}.
	 */
	public DomainUserLogger() {
		super(DomainUser.class);
	}

    void checkUser(Domain domain, Object name) {
        notNull(name, user_null.toString(), domain);
        notBlank(name.toString(), user_null.toString(), domain);
	}

    void checkGroup(Domain domain, Object group) {
        notNull(group, group_null.toString(), domain);
        notBlank(group.toString(), group_null.toString(), domain);
	}

    void checkUid(Domain domain, Object uid) {
        notNull(uid, uid_null.toString(), domain);
        isInstanceOf(Number.class, uid, uid_number.toString(), domain);
    }

    void checkGid(Domain domain, Object gid) {
        notNull(gid, gid_null.toString(), domain);
        isInstanceOf(Number.class, gid, gid_number.toString(), domain);
    }

    void checkRefDomain(Domain domain, Object ref) {
        notNull(ref, ref_null.toString(), domain);
        notBlank(ref.toString(), ref_null.toString(), domain);
    }
}
