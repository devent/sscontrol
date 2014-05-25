/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04;

import static java.util.Arrays.asList;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.anrisoftware.sscontrol.core.service.AbstractScriptFactory;
import com.google.inject.Module;

/**
 * Postfix/Ubuntu 10.04 service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceScriptFactory.class)
public class Ubuntu_10_04_ScriptFactory extends AbstractScriptFactory {

	/**
	 * Name of the service.
	 */
	public static final String NAME = "postfix";

	/**
	 * Name of the profile.
	 */
	public static final String PROFILE_NAME = "ubuntu_10_04";

	/**
	 * Identifying this service.
	 */
	public static ServiceScriptInfo INFO = new ServiceScriptInfo() {

		@Override
		public String getServiceName() {
			return NAME;
		}

		@Override
		public String getProfileName() {
			return PROFILE_NAME;
		}
	};

	@Override
	public ServiceScriptInfo getInfo() {
		return INFO;
	}

	@Override
	protected Iterable<? extends Module> getModules() {
		return asList(new Module[] { new UbuntuModule() });
	}

	@Override
	protected Class<?> getScriptClass() {
		return UbuntuScript.class;
	}
}
