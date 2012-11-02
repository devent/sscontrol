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
			.getResource("/com/anrisoftware/sscontrol/template/st/template.properties");

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
