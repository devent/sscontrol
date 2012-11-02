/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hostname.service;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import com.anrisoftware.resources.st.StResourcesModule;
import com.anrisoftware.resources.st.maps.TemplatesDefaultMapsModule;
import com.anrisoftware.resources.st.worker.STTemplateDefaultPropertiesModule;
import com.anrisoftware.sscontrol.hostname.ubuntu.Ubuntu_10_04Worker;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

class HostnameModule extends AbstractModule {

	@Override
	protected void configure() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
		binder.addBinding("ubuntu_10.04").to(Ubuntu_10_04Worker.class);
		install(new StResourcesModule());
		install(new TemplatesDefaultMapsModule());
		install(new STTemplateDefaultPropertiesModule());
	}

}
