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

import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.find_service;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.find_service_message;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.found_service_script_trace;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.loadTexts;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.profile_set_debug;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.profile_set_info;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.refservice_set_debug;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.refservice_set_info;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.search_script_factory_trace;
import static com.anrisoftware.sscontrol.core.service.AbstractServiceLogger._.service_name;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Logging messages for {@link AbstractService}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractServiceLogger extends AbstractLogger {

    enum _ {

        service_name,

        profile,

        service,

        find_service_message,

        find_service,

        profile_set_info,

        profile_set_debug,

        refservice_set_debug,

        refservice_set_info,

        search_script_factory_trace,

        found_service_script_trace;

        public static void loadTexts(TextsFactory factory) {
            String name = AbstractServiceLogger.class.getSimpleName();
            Texts texts = factory.create(name);
            for (_ value : values()) {
                value.text = texts.getResource(value.name()).getText();
            }
        }

        private String text;

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Create logger for {@link AbstractService}.
     */
    @Inject
    AbstractServiceLogger(TextsFactory textsFactory) {
        super(AbstractService.class);
    }

    @Inject
    void setTextsResources(TextsFactory factory) {
        loadTexts(factory);
    }

    void profileSet(AbstractService service, ProfileService profile) {
        if (isDebugEnabled()) {
            debug(profile_set_debug, profile, service);
        } else {
            info(profile_set_info, profile.getProfileName(), service.getName());
        }
    }

    ServiceException errorFindServiceScript(AbstractService service,
            ProfileService profile, String serviceName) {
        return logException(
                new ServiceException(find_service).add(service, service)
                        .add(profile, profile).add(service_name, serviceName),
                find_service_message, profile.getProfileName(), serviceName);
    }

    void refserviceSet(AbstractService service, String refservice) {
        if (isDebugEnabled()) {
            debug(refservice_set_debug, refservice, service);
        } else {
            info(refservice_set_info, refservice, service.getName());
        }
    }

    void searchScriptFactory(AbstractService service, String name) {
        trace(search_script_factory_trace, name, service);
    }

    void foundServiceScript(AbstractService service, ServiceScriptInfo info) {
        trace(found_service_script_trace, info, service);
    }

}
