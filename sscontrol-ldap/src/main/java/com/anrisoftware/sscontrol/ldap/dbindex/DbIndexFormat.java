/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.dbindex;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.split;

import java.awt.Color;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.stringtemplate.v4.ST;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Formats LDAP indexes. The indexes have the index names and the index flags:
 * {@code pres}, {@code eq}, {@code approx}, {@code sub} and {@code none}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DbIndexFormat extends Format {

	private static final String TYPES = "types";

	private static final String NAMES = "names";

	private static final String TYPES_TEMPLATE = ":<types;separator=\"%s\">";

	private static final String NAMES_TEMPLATE = "<names;separator=\"%s\">";

	@Inject
	private DbIndexFormatLogger log;

	@Inject
	private DbIndexFactory factory;

	private final String typesTemplate;

	private final String namesTemplate;

	private final String separator;

	/**
	 * @see DbIndexFormatFactory#create()
	 */
	@AssistedInject
	DbIndexFormat() {
		this(",");
	}

	/**
	 * @see DbIndexFormatFactory#create(String)
	 */
	@AssistedInject
	DbIndexFormat(@Assisted String separator) {
		this.separator = separator;
		this.typesTemplate = String.format(TYPES_TEMPLATE, separator);
		this.namesTemplate = String.format(NAMES_TEMPLATE, separator);
	}

	/**
	 * Formats the specified database index to string representation.
	 * <p>
	 * <h2>Format</h2>
	 * <p>
	 * <ul>
	 * <li>{@code "index[:type]"}
	 * <li>{@code "\[index_a,index_b,...\][:\[type_a,type_b,...\]]"}
	 * </ul>
	 * 
	 * @param obj
	 *            the {@link DbIndex}.
	 */
	@Override
	public StringBuffer format(Object obj, StringBuffer buff, FieldPosition pos) {
		if (obj instanceof DbIndex) {
			formatIndex(buff, (DbIndex) obj);
		}
		return buff;
	}

	private void formatIndex(StringBuffer buff, DbIndex index) {
		Set<String> names = index.getNames();
		Set<IndexType> types = index.getTypes();
		if (names.size() == 1) {
			appendName(buff, names);
		} else {
			appendNames(buff, names);
		}
		if (types.size() == 1) {
			appendType(buff, types);
		} else if (types.size() > 1) {
			appendTypes(buff, types);
		}
	}

	private void appendTypes(StringBuffer buff, Set<IndexType> types) {
		ST st = new ST(typesTemplate);
		st.add(TYPES, types);
		buff.append(st.render());
	}

	private void appendType(StringBuffer buff, Set<IndexType> types) {
		Iterator<IndexType> it = types.iterator();
		it.hasNext();
		buff.append(":");
		buff.append(it.next().toString());
	}

	private void appendNames(StringBuffer buff, Set<String> names) {
		ST st = new ST(namesTemplate);
		st.add(NAMES, names);
		buff.append(st.render());
	}

	private void appendName(StringBuffer buff, Set<String> names) {
		Iterator<String> it = names.iterator();
		it.hasNext();
		buff.append(it.next());
	}

	/**
	 * Parses the specified string to database index.
	 * <p>
	 * <h2>Format</h2>
	 * <p>
	 * <ul>
	 * <li>{@code "index[:type]"}
	 * <li>{@code "index_a,index_b,...[:type_a,type_b,...]"}
	 * </ul>
	 * 
	 * @see Color#decode(String)
	 */
	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return parse(source, pos);
	}

	/**
	 * @see #parse(String, ParsePosition)
	 */
	public DbIndex parse(String source) throws ParseException {
		ParsePosition pos = new ParsePosition(0);
		DbIndex result = parse(source, pos);
		if (pos.getIndex() == 0) {
			throw log.errorParseIndex(source, pos);
		}
		return result;
	}

	/**
	 * Parses the specified string to database index.
	 * <p>
	 * <h2>Format</h2>
	 * <p>
	 * <ul>
	 * <li>{@code "index[:type]"}
	 * <li>{@code "index_a,index_b,...[:type_a,type_b,...]"}
	 * </ul>
	 * 
	 * @param source
	 *            the source {@link String}.
	 * 
	 * @param pos
	 *            the index {@link ParsePosition} position from where to start
	 *            parsing.
	 * 
	 * @return the parsed {@link DbIndex}.
	 * 
	 * @throws ParseException
	 *             if the string is not in the correct format.
	 */
	public DbIndex parse(String source, ParsePosition pos) {
		try {
			source = source.substring(pos.getIndex());
			String[] split = split(source, ':');
			Set<String> names = new HashSet<String>(splitNames(split));
			Set<IndexType> types = new HashSet<IndexType>();
			if (split.length > 1) {
				parseTypes(split, types);
			}
			pos.setErrorIndex(-1);
			pos.setIndex(pos.getIndex() + source.length());
			return factory.create(names, types);
		} catch (NumberFormatException e) {
			pos.setIndex(0);
			pos.setErrorIndex(0);
			return null;
		}
	}

	private List<String> splitNames(String[] split) {
		return asList(split(split[0], separator));
	}

	private void parseTypes(String[] split, Set<IndexType> types) {
		String[] typesstr = split(split[1], separator);
		for (String type : typesstr) {
			types.add(IndexType.parse(type));
		}
	}

}
