/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04;

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.NAME;
import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE;
import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.AuthFileConfig;
import com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2.AuthLdapConfig;
import com.anrisoftware.sscontrol.httpd.apache.apache.api.AuthConfig;
import com.anrisoftware.sscontrol.httpd.apache.linux.roundcube.RoundcubeModule;
import com.anrisoftware.sscontrol.httpd.apache.phpldapadmin.ubuntu_10_04.Ubuntu_10_04_PhpldapadminModule;
import com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu_10_04.Ubuntu_10_04_PhpmyadminModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the Apache/Ubuntu 10.04 services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuModule extends AbstractModule {

	@Override
	protected void configure() {
        install(new RoundcubeModule());
        install(new Ubuntu_10_04_PhpldapadminModule());
        install(new Ubuntu_10_04_PhpmyadminModule());
		bindScripts();
		bindAuthConfig();
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding(format("%s.%s", NAME, PROFILE)).to(
				UbuntuScript.class);
	}

	private void bindAuthConfig() {
		MapBinder<String, AuthConfig> map = newMapBinder(binder(),
				String.class, AuthConfig.class);
		map.addBinding(format("%s.%s", PROFILE, AuthFileConfig.NAME)).to(
				AuthFileConfig.class);
		map.addBinding(format("%s.%s", PROFILE, AuthLdapConfig.NAME)).to(
				AuthLdapConfig.class);
	}

        // map.addBinding(format("%s.%s", PROFILE, PhpmyadminConfig.NAME)).to(
        // PhpmyadminConfig.class);
        // map.addBinding(format("%s.%s", PROFILE, RoundcubeConfig.NAME)).to(
        // RoundcubeConfig.class);
        // map.addBinding(format("%s.%s", PROFILE, WordpressConfig.NAME)).to(
        // WordpressConfig.class);
}
