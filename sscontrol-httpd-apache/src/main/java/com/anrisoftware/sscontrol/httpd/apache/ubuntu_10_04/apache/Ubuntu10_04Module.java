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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache;

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.NAME;
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.PROFILE;
import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScriptModule;
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.AuthConfig;
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.apache.linux.roundcube.RoundcubeModule;
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpldapadmin.PhpldapadminConfig;
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.phpmyadmin.PhpmyadminConfig;
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.roundcube.RoundcubeConfig;
import com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.wordpress.WordpressConfig;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the Apache/Ubuntu 10.04 services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu10_04Module extends AbstractModule {

	@Override
	protected void configure() {
		install(new ApacheScriptModule());
        install(new RoundcubeModule());
		bindScripts();
		bindAuthConfig();
		bindServiceConfig();
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding(format("%s.%s", NAME, PROFILE)).to(
				Ubuntu_10_04Script.class);
	}

	private void bindAuthConfig() {
		MapBinder<String, AuthConfig> map = newMapBinder(binder(),
				String.class, AuthConfig.class);
		map.addBinding(format("%s.%s", PROFILE, AuthFileConfig.NAME)).to(
				AuthFileConfig.class);
		map.addBinding(format("%s.%s", PROFILE, AuthLdapConfig.NAME)).to(
				AuthLdapConfig.class);
	}

	private void bindServiceConfig() {
		MapBinder<String, ServiceConfig> map = newMapBinder(binder(),
				String.class, ServiceConfig.class);
		map.addBinding(format("%s.%s", PROFILE, PhpmyadminConfig.NAME)).to(
				PhpmyadminConfig.class);
		map.addBinding(format("%s.%s", PROFILE, PhpldapadminConfig.NAME)).to(
				PhpldapadminConfig.class);
        map.addBinding(format("%s.%s", PROFILE, RoundcubeConfig.NAME)).to(
                RoundcubeConfig.class);
        map.addBinding(format("%s.%s", PROFILE, WordpressConfig.NAME)).to(
                WordpressConfig.class);
	}
}
