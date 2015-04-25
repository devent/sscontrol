/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting_2_3;

import static com.anrisoftware.sscontrol.httpd.frontaccounting_2_3.Frontaccounting_2_3_FactoryFactory.SERVICE_NAME;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.frontaccounting.AbstractFrontaccountingService;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>FrontAccounting 2.3</i> service.
 *
 * @see <a href="http://frontaccounting.com/>http://frontaccounting.com/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Frontaccounting_2_3_Service extends AbstractFrontaccountingService {

    /**
     * @see Frontaccounting_2_3_ServiceFactory#create(Map, Domain)
     */
    @Inject
    Frontaccounting_2_3_Service(@Assisted Map<String, Object> args,
            @Assisted Domain domain) {
        super(SERVICE_NAME, args, domain);
    }

}
