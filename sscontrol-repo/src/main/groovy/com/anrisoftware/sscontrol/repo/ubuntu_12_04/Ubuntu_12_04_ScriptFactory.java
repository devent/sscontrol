/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.ubuntu_12_04;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Repo Ubuntu 12.04</i> provider.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceScriptFactory.class)
public class Ubuntu_12_04_ScriptFactory implements ServiceScriptFactory {

    /**
     * Name of the service.
     */
    public static final String NAME = "repo";

    /**
     * Name of the profile.
     */
    public static final String PROFILE_NAME = "ubuntu_12_04";

    /**
     * {@link ServiceScriptInfo} information identifying this service.
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

    private static final Module[] MODULES = new Module[] { new UbuntuModule() };

    private Injector injector;

    @Override
    public ServiceScriptInfo getInfo() {
        return INFO;
    }

    @Override
    public Object getScript() throws ServiceException {
        return injector.getInstance(UbuntuScript.class);
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(INFO).toString();
    }
}
