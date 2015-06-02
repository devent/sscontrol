/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.groovy.languagestatements;

import static com.anrisoftware.sscontrol.core.groovy.languagestatements.LanguageStatement.LANGUAGE_KEY;
import static com.anrisoftware.sscontrol.core.groovy.languagestatements.LanguageStatement.LOCALES_KEY;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.format.locale.LocaleFormatFactory;
import com.anrisoftware.globalpom.posixlocale.PosixLocale;
import com.anrisoftware.globalpom.posixlocale.PosixLocaleFormatFactory;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Language statements.
 *
 * <pre>
 * language "en" // or
 * language Locale.US // or
 * language "en", locales: "de" // or
 * language "en", locales: "de, pt" // or
 * language "en", locales: ["de_DE.ISO-88591, pt_BR.ISO-88591"] // or
 * language "en", locales: [Locale.FRANCE, Locale.GERMANY]
 * </pre>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LanguageStatements implements Serializable {

    private static final String SERVICE = "service";

    private final Object service;

    private final String name;

    @Inject
    private LocaleFormatFactory localeFormatFactory;

    @Inject
    private PosixLocaleFormatFactory posixLocaleFormatFactory;

    private StatementsMap statementsMap;

    /**
     * @see LanguageStatementsFactory#create(Object, String)
     */
    @Inject
    LanguageStatements(@Assisted Object service, @Assisted String name) {
        this.service = service;
        this.name = name;
    }

    /**
     * Injects the statements map.
     *
     * @param factory
     *            the {@link StatementsMapFactory}.
     */
    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(service, name);
        map.addAllowed(LANGUAGE_KEY);
        map.setAllowValue(true, LANGUAGE_KEY);
        map.addAllowedKeys(LANGUAGE_KEY, LOCALES_KEY);
        this.statementsMap = map;
    }

    /**
     * Returns that the site language.
     *
     * <pre>
     * language "de" // or
     * language Locale.US // or
     * </pre>
     *
     * @return the language {@link Locale} locale or {@code null}.
     *
     * @throws ParseException
     *             if there was an error parsing the locale.
     */
    public Locale getLanguage() throws ParseException {
        Object value = statementsMap.value(LANGUAGE_KEY);
        if (value == null) {
            return null;
        }
        return converLocale(value);
    }

    /**
     * Returns the locale for the site.
     *
     * <pre>
     * language locales: "de, pt" // or
     * language locales: [PosixLocale.FRANCE, PosixLocale.GERMANY]
     * </pre>
     *
     * @return the {@link List} list of the site {@link PosixLocale} POSIX
     *         locales or {@code null}.
     *
     * @throws ParseException
     *             if there was an error parsing the locale.
     */
    public List<PosixLocale> getLocales() throws ParseException {
        List<Object> values;
        values = statementsMap.mapValueAsList(LANGUAGE_KEY, LOCALES_KEY);
        if (values == null) {
            return null;
        }
        List<PosixLocale> locales = new ArrayList<PosixLocale>();
        for (Object value : values) {
            locales.add(converPostfixLocale(value));
        }
        return locales;
    }

    /**
     * Put the statement value into the map.
     *
     * @throws StatementsException
     *             if the statement is not allowed for the map.
     */
    public Object methodMissing(String name, Object obj)
            throws StatementsException {
        return statementsMap.methodMissing(name, obj);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service.toString())
                .toString();
    }

    private Locale converLocale(Object value) throws ParseException {
        if (value instanceof Locale) {
            return (Locale) value;
        } else if (value instanceof String) {
            return localeFormatFactory.create().parse((String) value);
        } else {
            return localeFormatFactory.create().parse(value.toString());
        }
    }

    private PosixLocale converPostfixLocale(Object value) throws ParseException {
        if (value instanceof PosixLocale) {
            return (PosixLocale) value;
        } else if (value instanceof String) {
            return posixLocaleFormatFactory.create().parse((String) value);
        } else {
            return posixLocaleFormatFactory.create().parse(value.toString());
        }
    }

}
