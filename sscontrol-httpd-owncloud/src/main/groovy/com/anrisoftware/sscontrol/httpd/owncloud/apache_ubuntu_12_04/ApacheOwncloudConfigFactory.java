/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.apache_ubuntu_12_04;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigFactory;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigInfo;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>ownCloud</i> configuration factory for <i>Apache Ubuntu 12.04</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceConfigFactory.class)
public class ApacheOwncloudConfigFactory implements ServiceConfigFactory {

    /**
     * <i>Ubuntu 12.04</i> profile name.
     */
    public static final String PROFILE_NAME = "ubuntu_12_04";

    /**
     * <i>ownCloud</i> service name.
     */
    public static final String WEB_NAME = "owncloud_7";

    /**
     * <i>Apache</i> service name.
     */
    public static final String APACHE_NAME = "apache";

    /**
     * <i>ownCloud</i> service information.
     */
    @SuppressWarnings("serial")
    public static final ServiceConfigInfo INFO = new ServiceConfigInfo() {

        @Override
        public String getServiceName() {
            return APACHE_NAME;
        }

        @Override
        public String getWebName() {
            return WEB_NAME;
        }

        @Override
        public String getProfileName() {
            return PROFILE_NAME;
        }

        @Override
        public WebService getWebService() {
            return null;
        }
    };

    private static final Module[] MODULES = new Module[] { new ApacheModule() };

    private Injector injector;

    public ApacheOwncloudConfigFactory() {
    }

    @Override
    public ServiceConfig getScript() throws ServiceException {
        return injector.getInstance(UbuntuOwncloudConfig.class);
    }

    @Override
    public ServiceConfigInfo getInfo() {
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
