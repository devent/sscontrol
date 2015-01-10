/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.ubuntu_14_04;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.security.service.SecService;
import com.anrisoftware.sscontrol.security.service.ServiceConfig;

/**
 * Empty service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class EmptyServiceConfig implements ServiceConfig {

    @Override
    public void deployService(SecService service) {
    }

    @Override
    public String getServiceName() {
        return "0";
    }

    @Override
    public String getProfile() {
        return Ubuntu_14_04_ScriptFactory.PROFILE_NAME;
    }

    @Override
    public void setScript(LinuxScript script) {
    }

    @Override
    public LinuxScript getScript() {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }

}
