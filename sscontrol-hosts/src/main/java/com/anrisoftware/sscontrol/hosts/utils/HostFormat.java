package com.anrisoftware.sscontrol.hosts.utils;

import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.Validate.isInstanceOf;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.hosts.service.Host;
import com.anrisoftware.sscontrol.hosts.service.HostFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class HostFormat extends Format {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = -5629960125042426704L;

	private static final String FORMAT_TEMPLATE_NAME = "host_format";

	private static final String BASE_NAME = "HostFormat";

	private static final String PARSE_TEMPLATE_NAME = "host_parse";

	private final Templates templates;

	private final Locale locale;

	private final HostFactory hostFactory;

	/**
	 * Sets the default locale for the format.
	 * 
	 * @param templatesFactory
	 *            the {@link TemplatesFactory}.
	 * 
	 * @param hostFactory
	 *            the {@link HostFactory} to create a new host.
	 * 
	 * @see Locale#getDefault()
	 */
	@AssistedInject
	HostFormat(TemplatesFactory templatesFactory, HostFactory hostFactory) {
		this(templatesFactory, hostFactory, Locale.getDefault());
	}

	/**
	 * Sets the specified locale for the format.
	 * 
	 * @param templatesFactory
	 *            the {@link TemplatesFactory}.
	 * 
	 * @param hostFactory
	 *            the {@link HostFactory} to create a new host.
	 * 
	 * @param locale
	 *            the {@link Locale} of the format.
	 */
	@AssistedInject
	HostFormat(TemplatesFactory templatesFactory, HostFactory hostFactory,
			@Assisted Locale locale) {
		this.templates = templatesFactory.create(BASE_NAME);
		this.hostFactory = hostFactory;
		this.locale = locale;
	}

	/**
	 * Format the specified {@link Host}.
	 * <p>
	 * The format follows the pattern:
	 * 
	 * <pre>
	 * ip:"&lt;address>";name:"&lt;hostname>";aliases:["&lt;alias>",...]
	 * </pre>
	 */
	@Override
	public StringBuffer format(Object obj, StringBuffer buff, FieldPosition pos) {
		isInstanceOf(Host.class, obj);
		TemplateResource template;
		template = templates.getResource(FORMAT_TEMPLATE_NAME, locale);
		Host host = (Host) obj;
		buff.append(template.getText("host", host));
		return buff;
	}

	public Host parse(String source) throws ParseException {
		return (Host) parseObject(source);
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		source = source.trim();
		TemplateResource template;
		template = templates.getResource(PARSE_TEMPLATE_NAME, locale);
		Pattern pattern = Pattern.compile(template.getText());
		Matcher matcher = pattern.matcher(source);
		if (!matcher.matches()) {
			pos.setErrorIndex(pos.getIndex());
			return null;
		}
		String address = matcher.group(1);
		String name = matcher.group(2);
		Host host = hostFactory.create(address).host(name);
		String[] aliases = split(matcher.group(3), ",");
		for (int i = 0; i < aliases.length; i++) {
			host.alias(remove(aliases[i], "\""));
		}
		pos.setIndex(matcher.end());
		return host;
	}
}
