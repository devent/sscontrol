/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.redmine.core.Redmine_2_TrackingScriptConfig

/**
 * <i>Ubuntu 12.04 Redmine</i> tracking script configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_TrackingScriptConfig extends Redmine_2_TrackingScriptConfig {

    @Inject
    private RedminePropertiesProvider propertiesProvider

    @Override
    ContextProperties getRedmineProperties() {
        propertiesProvider.get()
    }

    @Override
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

    @Override
    String getProfile() {
        RedmineConfigFactory.PROFILE_NAME
    }
}
