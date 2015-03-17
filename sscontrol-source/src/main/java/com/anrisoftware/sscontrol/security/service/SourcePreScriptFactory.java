/*
 * Copyright 2015-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source.
 *
 * sscontrol-source is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicePreScript;
import com.anrisoftware.sscontrol.core.api.ServicePreScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Factory providing pre-script service for <i>Httpd</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServicePreScriptFactory.class)
public class SourcePreScriptFactory implements ServicePreScriptFactory {

    private static final ServiceScriptInfo INFO = new ServiceScriptInfo() {

        @Override
        public String getServiceName() {
            return SourceFactory.NAME;
        }

        @Override
        public String getProfileName() {
            return null;
        }
    };

    private static final Module[] MODULES = new Module[] { new SourcePreScriptModule() };

    private Injector injector;

    @Override
    public ServicePreScript getPreScript() throws ServiceException {
        SourcePreScript service;
        service = injector.getInstance(SourcePreScript.class);
        return service;
    }

    @Override
    public ServiceScriptInfo getInfo() {
        return INFO;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

}
