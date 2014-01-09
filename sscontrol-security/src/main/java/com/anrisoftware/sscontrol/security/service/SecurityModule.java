/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingModule;
import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.security.banning.BanningModule;
import com.anrisoftware.sscontrol.security.ignoring.IgnoringModule;
import com.anrisoftware.sscontrol.security.services.ServiceModule;
import com.google.inject.AbstractModule;

/**
 * Binds the security service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SecurityModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DebugLoggingModule());
        install(new ListModule());
        install(new ServiceModule());
        install(new IgnoringModule());
        install(new BanningModule());
    }
}
