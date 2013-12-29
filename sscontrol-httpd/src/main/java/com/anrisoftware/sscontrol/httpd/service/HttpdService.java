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
package com.anrisoftware.sscontrol.httpd.service;

import java.util.List;
import java.util.Set;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Httpd service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface HttpdService extends Service {

    /**
     * Returns the httpd service name.
     */
    @Override
    String getName();

    /**
     * Returns a list of the IP addresses where to bind the DNS service.
     * 
     * @return the {@link Binding}.
     */
    Binding getBinding();

    /**
     * Returns all domains of the service.
     *
     * @return the {@link List} of the {@link Domain} domains.
     */
    List<Domain> getDomains();

    /**
     * Returns a set of the virtual domains of the service.
     *
     * @return the {@link Set} of the virtual {@link Domain} domains.
     */
    Set<Domain> getVirtualDomains();

    /**
     * Sets the debug logging.
     * 
     * @param debug
     *            the {@link DebugLogging}.
     */
    void setDebug(DebugLogging debug);

    /**
     * Returns debug logging.
     * 
     * @return the {@link DebugLogging} or {@code null}.
     */
    DebugLogging getDebug();

}
