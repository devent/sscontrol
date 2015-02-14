/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_14_04;

import static com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_14_04.Ubuntu_14_04_ScriptFactory.NAME;
import static com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_14_04.Ubuntu_14_04_ScriptFactory.PROFILE;
import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.httpd.nginx.generalproxy.ubuntu_14_04.Ubuntu_14_04_GeneralProxyModule;
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScriptModule;
import com.anrisoftware.sscontrol.httpd.nginx.proxypass.ubuntu_14_04.Ubuntu_14_04_ProxyPassModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Nginx Ubuntu 14.04</i> service module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new NginxScriptModule());
        install(new Ubuntu_14_04_GeneralProxyModule());
        install(new Ubuntu_14_04_ProxyPassModule());
        bindScripts();
    }

    private void bindScripts() {
        MapBinder<String, Script> binder;
        binder = newMapBinder(binder(), String.class, Script.class);
        binder.addBinding(format("%s.%s", NAME, PROFILE))
                .to(UbuntuScript.class);
    }

}
