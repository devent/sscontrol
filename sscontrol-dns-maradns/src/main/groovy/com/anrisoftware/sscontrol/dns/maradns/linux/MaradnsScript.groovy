/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.linux

import org.joda.time.Duration

import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * MaraDNS/service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MaradnsScript extends LinuxScript {

	def run() {
		super.run()
		beforeConfiguration()
	}

	/**
	 * Runs distribution specific configuration before
	 * the MaraDNS/configuration.
	 */
	abstract void beforeConfiguration()

	/**
	 * Returns the file of the {@code mararc} configuration file.
	 *
	 * <ul>
	 * <li>profile property key {@code configuration_file}</li>
	 * </ul>
	 *
	 * @see #getConfigurationDir()
	 * @see #getDefaultProperties()
	 */
	File getMararcFile() {
		profileFileProperty "configuration_file", configurationDir, defaultProperties
	}

	/**
	 * Returns the default bindings.
	 *
	 * <ul>
	 * <li>profile property key {@code default_binding_addresses}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	List getDefaultBinding() {
		profileListProperty "default_binding_addresses", defaultProperties
	}

	/**
	 * Returns the default TTL duration, for example {@code P1D}.
	 *
	 * <ul>
	 * <li>profile property key {@code default_ttl_duration}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Duration getDefaultTtl() {
		profileDurationProperty "default_ttl_duration", defaultProperties
	}

	/**
	 * Returns the default refresh duration, for example {@code PT8H}.
	 *
	 * <ul>
	 * <li>profile property key {@code default_refresh_duration}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Duration getDefaultRefresh() {
		profileDurationProperty "default_refresh_duration", defaultProperties
	}

	/**
	 * Returns the default retry duration, for example {@code PT2H}.
	 *
	 * <ul>
	 * <li>profile property key {@code default_retry_duration}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Duration getDefaultRetry() {
		profileDurationProperty "default_retry_duration", defaultProperties
	}

	/**
	 * Returns the default expire duration, for example {@code P4D}.
	 *
	 * <ul>
	 * <li>profile property key {@code default_expire_duration}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Duration getDefaultExpire() {
		profileDurationProperty "default_expire_duration", defaultProperties
	}

	/**
	 * Returns the default minimum TTL duration, for example {@code P1D}.
	 *
	 * <ul>
	 * <li>profile property key {@code default_minimum_duration}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Duration getDefaultMinimumTtl() {
		profileDurationProperty "default_minimum_duration", defaultProperties
	}
}
