package com.anrisoftware.sscontrol.template.st;

import java.net.URL;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.compiler.STException;
import org.stringtemplate.v4.misc.STMessage;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.template.api.Template;
import com.anrisoftware.sscontrol.template.api.TemplateException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Template that is using a <a
 * href=http://www.antlr.org/wiki/display/ST4/StringTemplate+4+Wiki+Home>String
 * Template</a> as the template engine.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class StTemplate implements Template {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 9004376597114715535L;

	static final String DELIMITER_STOP_CHAR_PROPERTY = "template_delimiter_stop_character";

	static final String DELIMITER_START_CHAR_PROPERTY = "template_delimiter_start_character";

	static final String ENCODING_PROPERTY = "template_encoding";

	private final StTemplateLogger log;

	private final URL resource;

	private final ContextProperties properties;

	private transient STGroupFile groupFile;

	private transient TemplateException error;

	/**
	 * Opens the ST template group file with default properties.
	 * 
	 * @param logger
	 *            the {@link StTemplateLogger} for logging messages.
	 * 
	 * @param resource
	 *            the {@link URL} of the template file.
	 * 
	 * @param defaultProperties
	 *            the default properties.
	 * 
	 * @throws TemplateException
	 *             if there was an error creating the template.
	 */
	@AssistedInject
	StTemplate(StTemplateLogger logger,
			@Named("st-template-properties") Properties defaultProperties,
			@Assisted URL resource) throws TemplateException {
		this(logger, defaultProperties, resource, new Properties());
	}

	/**
	 * Opens the ST template group file.
	 * 
	 * @param logger
	 *            the {@link StTemplateLogger} for logging messages.
	 * 
	 * @param resource
	 *            the {@link URL} of the template file.
	 * 
	 * @param defaultProperties
	 *            the default properties.
	 * 
	 * @param properties
	 *            {@link Properties} for the template group file. Have the
	 *            properties:
	 *            <dl>
	 * 
	 *            <dt>{@value #ENCODING_PROPERTY}</dt>
	 *            <dd>The encoding of the group file. Defaults to the encoding
	 *            utf-8.</dd>
	 * 
	 *            <dt>{@value #DELIMITER_START_CHAR_PROPERTY}</dt>
	 *            <dd>The character to start the template. Defaults to the
	 *            character {@code <}</dd>
	 * 
	 *            <dt>{@value #DELIMITER_STOP_CHAR_PROPERTY}</dt>
	 *            <dd>The character to end the template. Defaults to the
	 *            character {@code >}</dd>
	 * 
	 *            </dl>
	 * 
	 * @throws TemplateException
	 *             if there was an error creating the template.
	 */
	@AssistedInject
	StTemplate(StTemplateLogger logger,
			@Named("st-template-properties") Properties defaultProperties,
			@Assisted URL resource, @Assisted Properties properties)
			throws TemplateException {
		this.log = logger;
		this.resource = resource;
		defaultProperties.putAll(properties);
		this.properties = new ContextProperties(this, defaultProperties);
		readResolve();
	}

	private Object readResolve() throws TemplateException {
		createGroupFile();
		return this;
	}

	private void createGroupFile() throws TemplateException {
		this.groupFile = openGroupFile(resource);
		groupFile.setListener(new STErrorListener() {

			@Override
			public void runTimeError(STMessage msg) {
				error = log.errorProcessTemplate(StTemplate.this, msg);
			}

			@Override
			public void internalError(STMessage msg) {
				error = log.errorProcessTemplate(StTemplate.this, msg);
			}

			@Override
			public void compileTimeError(STMessage msg) {
				error = log.errorProcessTemplate(StTemplate.this, msg);
			}

			@Override
			public void IOError(STMessage msg) {
				error = log.errorProcessTemplate(StTemplate.this, msg);
			}
		});
	}

	private STGroupFile openGroupFile(URL templateUrl) throws TemplateException {
		String encoding;
		char startChar;
		char stopChar;
		encoding = properties.getProperty(ENCODING_PROPERTY);
		startChar = properties.getCharProperty(DELIMITER_START_CHAR_PROPERTY);
		stopChar = properties.getCharProperty(DELIMITER_STOP_CHAR_PROPERTY);
		try {
			return new STGroupFile(templateUrl, encoding, startChar, stopChar);
		} catch (STException e) {
			throw log.errorOpenGroupFile(this, e);
		}
	}

	@Override
	public String process(String name, Object... data) throws TemplateException {
		ST template = createTemplate(name, data);
		String rendered = renderTemplate(template);
		log.templateProcessed(this);
		return rendered;
	}

	private ST createTemplate(String templateName, Object... data)
			throws TemplateException {
		ST template = groupFile.getInstanceOf(templateName);
		log.checkTemplateCreated(this, template);
		throwErrors();
		for (int i = 0; i < data.length; i++) {
			template.add(data[i].toString(), data[++i]);
		}
		return template;
	}

	private String renderTemplate(ST template) throws TemplateException {
		String rendered = template.render();
		throwErrors();
		return rendered;
	}

	private void throwErrors() throws TemplateException {
		if (error != null) {
			throw error;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(resource).toString();
	}

}
