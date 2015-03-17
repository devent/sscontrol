/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu_12_04;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.source.service.ServiceConfigFactory;
import com.anrisoftware.sscontrol.source.service.SourceServiceConfig;
import com.anrisoftware.sscontrol.source.service.SourceServiceConfigInfo;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Gitolite Ubuntu 12.04</i> configuration factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceConfigFactory.class)
public class Gitolite_Ubuntu_12_04_ConfigFactory implements ServiceConfigFactory {

    /**
     * <i>Ubuntu 12.04</i> profile name.
     */
    public static final String PROFILE_NAME = "ubuntu_12_04";

    /**
     * <i>Gitolite</i> service name.
     */
    public static final String SERVICE_NAME = "gitolite";

    /**
     * <i>ClamAV</i> service information.
     */
    @SuppressWarnings("serial")
    public static final SourceServiceConfigInfo INFO = new SourceServiceConfigInfo() {

        @Override
        public String getSourceName() {
            return SERVICE_NAME;
        }

        @Override
        public String getProfileName() {
            return PROFILE_NAME;
        }

        @Override
        public SourceSetupService getSourceService() {
            return null;
        }
    };

    private static final Module[] MODULES = new Module[] { new UbuntuModule() };

    private Injector injector;

    public Gitolite_Ubuntu_12_04_ConfigFactory() {
    }

    @Override
    public SourceServiceConfig getScript() throws ServiceException {
        return injector.getInstance(UbuntuGitoliteConfig.class);
    }

    @Override
    public SourceServiceConfigInfo getInfo() {
        return INFO;
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
