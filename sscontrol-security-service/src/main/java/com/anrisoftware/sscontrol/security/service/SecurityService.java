/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import java.util.List;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Security service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SecurityService extends Service {

    /**
     * Returns the security service name.
     */
    @Override
    String getName();

    /**
     * Returns the security services.
     * <p>
     *
     * <pre>
     * security {
     *     service "fail2ban", {
     *     }
     * }
     * </pre>
     *
     * @return
     */
    List<SecService> getServices();
}
