/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link AbstractService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractServiceLogger extends AbstractLogger {

	private static final String SERVICE_NAME = "service name";
	private static final String PROFILE = "profile";
	private static final String SERVICE = "service";
	private static final String FIND_SERVICE2 = "Error find service script '{}' for service '{}'.";
	private static final String FIND_SERVICE = "Error find service script";
	private static final String PROFILE_SET_INFO = "profile_set_info";
	private static final String PROFILE_SET_DEBUG = "profile_set_debug";
	private static final String NAME = AbstractServiceLogger.class
			.getSimpleName();

	private final Texts texts;

	/**
	 * Create logger for {@link AbstractService}.
	 */
	@Inject
	AbstractServiceLogger(TextsFactory textsFactory) {
		super(AbstractService.class);
		this.texts = textsFactory.create(NAME);
	}

	private String getText(String name) {
		return texts.getResource(name).getText();
	}

	void profileSet(AbstractService service, ProfileService profile) {
		if (log.isDebugEnabled()) {
			log.debug(getText(PROFILE_SET_DEBUG), profile, service);
		} else {
			log.info(getText(PROFILE_SET_INFO), profile.getProfileName(),
					service.getName());
		}
	}

	ServiceException errorFindServiceScript(AbstractService service,
			ProfileService profile, String serviceName) {
		return logException(
				new ServiceException(FIND_SERVICE).add(SERVICE, service)
						.add(PROFILE, profile).add(SERVICE_NAME, serviceName),
				FIND_SERVICE2, profile.getProfileName(), serviceName);
	}

}
