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

import static com.anrisoftware.sscontrol.source.ubuntu_12_04.Ubuntu_12_04_ScriptFactory.PROFILE_NAME;
import static com.anrisoftware.sscontrol.source.ubuntu_12_04.Ubuntu_12_04_ScriptFactory.SERVICE_NAME;
import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.security.service.SourceServiceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Security Ubuntu 12.04</i> services module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        bindScripts();
        bindEmptyServiceConfig();
    }

    private void bindScripts() {
        MapBinder<String, Script> binder;
        binder = newMapBinder(binder(), String.class, Script.class);
        binder.addBinding(format("%s.%s", SERVICE_NAME, PROFILE_NAME)).to(
                UbuntuScript.class);
    }

    private void bindEmptyServiceConfig() {
        MapBinder<String, SourceServiceConfig> map = newMapBinder(binder(),
                String.class, SourceServiceConfig.class);
        map.addBinding(format("%s.%s", PROFILE_NAME, "0")).to(
                EmptyServiceConfig.class);
    }
}
