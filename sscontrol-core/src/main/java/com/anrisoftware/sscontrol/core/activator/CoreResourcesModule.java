/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.activator;

import com.anrisoftware.resources.binary.binaries.BinariesResourcesModule;
import com.anrisoftware.resources.binary.maps.BinariesDefaultMapsModule;
import com.anrisoftware.resources.texts.maps.TextsDefaultMapsModule;
import com.anrisoftware.resources.texts.texts.TextsResourcesCharsetModule;
import com.anrisoftware.resources.texts.texts.TextsResourcesModule;
import com.google.inject.AbstractModule;

/**
 * Installs the core text resources.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CoreResourcesModule extends AbstractModule {

	@Override
	protected void configure() {
		installResources();
	}

	private void installResources() {
		install(new TextsResourcesModule());
		install(new TextsDefaultMapsModule());
		install(new TextsResourcesCharsetModule());
		install(new BinariesResourcesModule());
		install(new BinariesDefaultMapsModule());
	}
}
