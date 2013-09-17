/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-mail-postfix.
 * 
 * sscontrol-mail-postfix is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.courierdelivery.linux;

import static com.anrisoftware.sscontrol.mail.postfix.courierdelivery.linux.CourierMysqlDeliveryConfigLogger._.configuration_deployed_debug;
import static com.anrisoftware.sscontrol.mail.postfix.courierdelivery.linux.CourierMysqlDeliveryConfigLogger._.configuration_deployed_info;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link MysqlScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CourierMysqlDeliveryConfigLogger extends AbstractLogger {

	enum _ {

		configuration_deployed_debug("Configuration deployed '{}', to {}."),

		configuration_deployed_info(
				"Configuration deployed '{}', to mail script.");

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
	 * Create logger for {@link MysqlScript}.
	 */
	CourierMysqlDeliveryConfigLogger() {
		super(CourierMysqlDeliveryConfig.class);
	}

	void configurationDeployed(CourierMysqlDeliveryConfig script, File file) {
		if (isDebugEnabled()) {
			debug(configuration_deployed_debug, file, script);
		} else {
			debug(configuration_deployed_info, file);
		}
	}
}
