/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-template-st.
 *
 * sscontrol-template-st is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-template-st is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-template-st. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.template.st;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.anrisoftware.sscontrol.template.api.Template;
import com.anrisoftware.sscontrol.template.api.TemplateFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the template factory to use a <a
 * href=http://www.antlr.org/wiki/display/ST4/StringTemplate+4+Wiki+Home>String
 * Template</a> as the template engine.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class StTemplateModule extends AbstractModule {

	private static final URL defaultProperties = StTemplateModule.class
			.getResource("template.properties");

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Template.class,
				StTemplate.class).build(TemplateFactory.class));
	}

	@Provides
	@Named("st-template-properties")
	@Singleton
	Properties getTemplateProperties() throws IOException {
		return new ContextPropertiesFactory(StTemplate.class).withProperties(
				System.getProperties()).fromResource(defaultProperties);
	}
}
