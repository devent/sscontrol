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
 * sscontrol-dns is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.zone;

import static com.anrisoftware.sscontrol.dns.zone.AbstractRecordLogger._.duration_null;
import static com.anrisoftware.sscontrol.dns.zone.AbstractRecordLogger._.invalid_operation;
import static com.anrisoftware.sscontrol.dns.zone.AbstractRecordLogger._.invalid_operation_message;
import static com.anrisoftware.sscontrol.dns.zone.AbstractRecordLogger._.ttl_set_debug;
import static com.anrisoftware.sscontrol.dns.zone.AbstractRecordLogger._.ttl_set_info;
import static org.apache.commons.lang3.Validate.notNull;

import org.slf4j.Logger;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.dns.time.TimeDuration;

/**
 * Logger for the zone records.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractRecordLogger extends AbstractLogger {

	enum _ {

		ttl_set_info("TTL time {} set for the zone record {}."),

		ttl_set_debug("TTL time {} set for {}."),

		duration_null("Duration time cannot be null for %s."),

		invalid_operation("Invalid operation"),

		record("record"),

		operation("operation"),

		invalid_operation_message("Invalid operation '{}' for {}.");

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
	 * Creates a new {@link Logger} for the given {@link AbstractRecord}.
	 */
	AbstractRecordLogger() {
		super(AbstractRecord.class);
	}

	void ttlSet(AbstractRecord record, TimeDuration ttl) {
		if (isDebugEnabled()) {
			debug(ttl_set_debug, ttl, record);
		} else {
			info(ttl_set_info, ttl, record.getClass().getSimpleName());
		}
	}

	void checkDuration(AbstractRecord record, Object duration) {
		notNull(duration, duration_null.toString(), record);
	}

	void invalidOperation(AbstractRecord record, String operation)
			throws ServiceException {
		throw logException(
				new ServiceException(invalid_operation).add(record, record)
						.add(operation, operation), invalid_operation_message,
				operation, record);
	}

}
