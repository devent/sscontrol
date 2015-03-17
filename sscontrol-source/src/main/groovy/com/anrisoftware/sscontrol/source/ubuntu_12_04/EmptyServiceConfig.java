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
package com.anrisoftware.sscontrol.source.ubuntu_12_04;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.source.service.SourceServiceConfig;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;

/**
 * Empty service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class EmptyServiceConfig implements SourceServiceConfig {

    @Override
    public void deployService(SourceSetupService service) {
    }

    @Override
    public String getServiceName() {
        return "0";
    }

    @Override
    public String getProfile() {
        return Ubuntu_12_04_ScriptFactory.PROFILE_NAME;
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
